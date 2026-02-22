package com.hospito.repository;

import com.hospito.entity.Staff;
import com.hospito.entity.num.StaffRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StaffRepository extends JpaRepository<Staff, Long> {
    Optional<Staff> findByUsername(String username);
    Optional<Staff> findByEmail(String email);
    List<Staff> findByRole(StaffRole role);
    List<Staff> findByIsActiveTrue();

    boolean existsByUsername(String username);
    boolean existsByPhoneNumber(String phoneNumber);
    boolean existsByEmail(String email);
}
