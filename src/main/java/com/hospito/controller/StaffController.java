package com.hospito.controller;

import com.hospito.dto.ApiResponse;
import com.hospito.entity.Staff;
import com.hospito.entity.num.StaffRole;
import com.hospito.service.StaffService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/staff")
@RequiredArgsConstructor
public class StaffController {

    private final StaffService staffService;

    // 1. Onboard a new staff member
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Staff>> registerStaff(@RequestBody Staff staff) {
        Staff createdStaff = staffService.registerStaff(staff);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(createdStaff, "Staff member onboarded successfully."));
    }

    // 2. Staff Login
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<Staff>> login(@RequestParam String username, @RequestParam String password) {
        Staff staff = staffService.login(username, password);
        return ResponseEntity.ok(ApiResponse.success(staff, "Login successful. Welcome, " + staff.getFullName()));
    }

    // 3. Get all staff by role (e.g., Get all Housekeeping)
    @GetMapping("/role/{role}")
    public ResponseEntity<ApiResponse<List<Staff>>> getStaffByRole(@PathVariable StaffRole role) {
        List<Staff> staffList = staffService.getStaffByRole(role);
        return ResponseEntity.ok(ApiResponse.success(staffList, "Staff list for role " + role + " retrieved."));
    }

    // 4. Update a staff member's role (Admin Action)
    @PatchMapping("/{id}/role")
    public ResponseEntity<ApiResponse<Staff>> updateRole(@PathVariable Long id, @RequestParam StaffRole newRole) {
        Staff updatedStaff = staffService.updateStaffRole(id, newRole);
        return ResponseEntity.ok(ApiResponse.success(updatedStaff, "Role updated to " + newRole));
    }

    // 5. Deactivate account (Soft Delete)
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deactivateStaff(@PathVariable Long id) {
        staffService.deactivateStaff(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Staff account deactivated."));
    }
}