package com.hospito.service;

import com.hospito.entity.Booking;
import com.hospito.entity.Guest;
import com.hospito.entity.Room;
import com.hospito.entity.num.RoomStatus;
import com.hospito.exception.HospitoException;
import com.hospito.repository.BookingRepository;
import com.hospito.repository.GuestRepository;
import com.hospito.repository.RoomRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingService {
    private final RoomRepository roomRepository;
    private final GuestRepository guestRepository;
    private final BookingRepository bookingRepository;


    @Transactional
    public Booking createBooking(Long guestId, Long roomId, LocalDate checkIn,LocalDate checkOut){
        if(checkIn.isAfter(checkOut) || checkIn.isEqual(checkOut)){
            throw new HospitoException("Check-out date must be after check-in date.", HttpStatus.BAD_REQUEST);
        }
        List<Booking> conflicts = bookingRepository.findOverlappingBookings(roomId,checkIn,checkOut);
        if(!conflicts.isEmpty()){
            throw new HospitoException("Room is already booked for these dates",HttpStatus.CONFLICT);
        }

        Room room = roomRepository.findById(roomId)
                .orElseThrow(
                        ()->new HospitoException("Room does not exists",HttpStatus.NOT_FOUND)
                );

        Guest guest = guestRepository.findById(guestId)
                .orElseThrow(
                        ()->new HospitoException("Guest does not exists",HttpStatus.NOT_FOUND)
                );

        if(!room.getStatus().equals(RoomStatus.AVAILABLE)){
            throw new HospitoException("Room status is not AVAILABLE",HttpStatus.CONFLICT);
        }

        long days = ChronoUnit.DAYS.between(checkIn,checkOut);
        Double totalAmount = days * room.getPricePerNight();

        Booking booking = Booking.builder()
                .guest(guest)
                .room(room)
                .checkInDate(checkIn)
                .checkOutDate(checkOut)
                .totalAmount(totalAmount)
                .isCheckedOut(false)
                .build();

        if(checkIn.equals(LocalDate.now())){
            booking.setCheckedIn(true);
        }
        return bookingRepository.save(booking);
    }

    public List<Booking> allBookingsByNumber(String phoneNumber){
        //it will give all bokking by this phone Number
        List<Booking> bookings = bookingRepository.findByGuestPhoneNumber(phoneNumber);
        if(bookings.isEmpty()){
            throw new HospitoException("No booking found for given phone number",HttpStatus.NOT_FOUND);
        }
        return bookings;
    }

    public List<Booking> getCurrentlyStayingGuests(){
        List<Booking> allBookings = bookingRepository.findAll();
        return allBookings.stream().filter(booking -> booking.isCheckedIn() && !booking.isCheckedOut())
                .toList();
    }

    public List<Booking> guestsThatHaveToCheckedOutToday(){
        return getCurrentlyStayingGuests().stream().filter(booking -> booking.getCheckOutDate().equals(LocalDate.now())).toList();
    }

    @Transactional
    public Booking checkOut(Long bookingId){
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(
                        () -> new HospitoException("Booking not exists", HttpStatus.NOT_FOUND)
                );
        if(!booking.isCheckedIn()){
            throw new HospitoException("Guest not checked IN",HttpStatus.CONFLICT);
        }
        if(booking.isCheckedOut()){
            throw new HospitoException("Guest already checked out",HttpStatus.CONFLICT);
        }
        booking.setCheckedOut(true);
        Room room = booking.getRoom();
        room.setStatus(RoomStatus.UNDER_MAINTENANCE);
        roomRepository.save(room);
        return bookingRepository.save(booking);
    }

    @Transactional
    public String cancelBooking(Long bookingId){
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(
                        ()-> new HospitoException("Booking not exists", HttpStatus.NOT_FOUND)
                );
        //we will check first booking checkIn date is earlier than 1 day
        if(booking.getCheckInDate().isAfter(LocalDate.now().plusDays(1L))){
            bookingRepository.delete(booking);
            return "Booking has been cancelled";
        }
        return "Booking can't be cancelled";
    }


}
