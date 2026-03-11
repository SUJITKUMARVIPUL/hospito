package com.hospito.controller;

import com.hospito.dto.ApiResponse;
import com.hospito.entity.Guest;
import com.hospito.service.GuestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/guests")
@RequiredArgsConstructor
public class GuestController {

    private final GuestService guestService;

    // 1. Register a new guest
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Guest>> registerGuest(@RequestBody Guest guest) {
        Guest savedGuest = guestService.registerGuest(guest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(savedGuest, "Guest registered successfully."));
    }

    // 2. Get all guests (for the guest directory)
    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<Guest>>> getAllGuests() {
        List<Guest> guests = guestService.getAllGuests();
        return ResponseEntity.ok(ApiResponse.success(guests, "Guest list retrieved."));
    }

    // 3. Find guest by ID
    @GetMapping("/guestById/{id}")
    public ResponseEntity<ApiResponse<Guest>> getGuestById(@PathVariable Long id) {
        Guest guest = guestService.getGuestById(id);
        return ResponseEntity.ok(ApiResponse.success(guest, "Guest found."));
    }

    // 4. Search guest by Email or Phone (Very useful for the Search bar)
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Guest>> searchGuest(@RequestParam String contact) {
        Guest guest = guestService.findGuestByEmailOrPhoneNumber(contact);
        return ResponseEntity.ok(ApiResponse.success(guest, "Guest record found."));
    }

    // 5. Update guest profile
    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse<Guest>> updateGuest(@PathVariable Long id, @RequestBody Guest guestDetails) {
        Guest updatedGuest = guestService.updateGuest(id, guestDetails);
        return ResponseEntity.ok(ApiResponse.success(updatedGuest, "Guest profile updated."));
    }

    // 6. Remove guest record
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteGuest(@PathVariable Long id) {
        guestService.deleteGuestById(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Guest record deleted."));
    }
}
