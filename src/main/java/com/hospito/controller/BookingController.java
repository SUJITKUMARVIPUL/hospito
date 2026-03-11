package com.hospito.controller;

import com.hospito.dto.ApiResponse;
import com.hospito.entity.Booking;
import com.hospito.entity.Guest;
import com.hospito.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/bookings")
public class BookingController {
    private final BookingService bookingService;

    @PostMapping("/createBooking")
    public ResponseEntity<ApiResponse<Booking>> createBooking(@RequestParam Long guestId,@RequestParam Long roomId
            ,@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkIn
            ,@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOut
        ){
        Booking booking = bookingService.createBooking(guestId, roomId, checkIn, checkOut);
        return ResponseEntity.ok(ApiResponse.success(booking,"Guest check-in successfully!"));
    }

    @GetMapping("/allBookingsByNumber")
    public List<Booking> getBookings(@RequestParam String contact){
        List<Booking> bookings = bookingService.allBookingsByNumber(contact);
//        List<Booking> list = bookings.stream().filter(booking -> booking.getCheckInDate().isEqual(LocalDate.now())).toList();
        return bookings;
    }

    @GetMapping("/currentlyStayingGuests")
    public List<Booking> getCurrentlyStayingGuests(){
        return bookingService.getCurrentlyStayingGuests();
    }

    @GetMapping("/guestsThatHaveToCheckedOutToday")
    public List<Booking> guestsThatHaveToCheckedOutToday(){
        return bookingService.guestsThatHaveToCheckedOutToday();
    }


    @PutMapping("checkIn")
    public Booking checkIn(@RequestParam String contact){
        List<Booking> bookings = bookingService.allBookingsByNumber(contact);
        Booking booking = bookings.stream().filter(book -> book.getCheckInDate().isEqual(LocalDate.now()))
                .toList().getFirst();
        booking.setCheckedIn(true);
        return booking;
    }

    @PostMapping("/check-out/{bookingId}")
    public ResponseEntity<ApiResponse<Booking>> checkOut(@PathVariable Long bookingId){
        Booking booking = bookingService.checkOut(bookingId);
        return ResponseEntity.ok(ApiResponse.success(booking,"Check-out completed.Room marked for cleaning"));
    }

    @DeleteMapping("/cancelBooking/{bookingId}")
    public ResponseEntity<ApiResponse<String>> cancelBooking(@PathVariable Long bookingId){
        String massage = bookingService.cancelBooking(bookingId);
        return ResponseEntity.ok(ApiResponse.success(massage,massage));
    }
}
