package com.hospito.repository;

import com.hospito.entity.Booking;
import com.hospito.entity.Guest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByGuestPhoneNumber(String guestNumber);

    @Query("SELECT b FROM Booking b WHERE b.room.id = :roomId " +
            "AND (:checkIn < b.checkOutDate AND :checkOut > b.checkInDate)")
    List<Booking> findOverlappingBookings(Long roomId, LocalDate checkIn,LocalDate checkOut);
}
