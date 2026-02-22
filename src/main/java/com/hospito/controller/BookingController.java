package com.hospito.controller;

import com.hospito.dto.ApiResponse;
import com.hospito.entity.Booking;
import com.hospito.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/bookings")
public class BookingController {
    private final BookingService bookingService;

    @PostMapping("/check-in")
    public ResponseEntity<ApiResponse<Booking>> checkIn(@RequestParam Long guestId,@RequestParam Long roomId
            ,@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkIn
            ,@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOut
        ){
        Booking booking = bookingService.createBooking(guestId, roomId, checkIn, checkOut);
        return ResponseEntity.ok(ApiResponse.success(booking,"Guest check-in successfully!"));
    }

    @PostMapping("/{bookingId}/check-out")
    public ResponseEntity<ApiResponse<Booking>> checkOut(@PathVariable Long bookingId){
        Booking booking = bookingService.checkOut(bookingId);
        return ResponseEntity.ok(ApiResponse.success(booking,"Check-out completed.Room marked for cleaning"));
    }
}
