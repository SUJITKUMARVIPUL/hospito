package com.hospito.entity;

import com.hospito.entity.num.StaffRole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Staff {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String fullName;

    @Column(unique = true,nullable = false)
    private String username;
    private String email;
    private String phoneNumber;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private StaffRole role;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = false;

    @PrePersist
    protected void onCreate() {
        // 1. If role is missing in JSON, set to RECEPTIONIST
//        if (this.role == null) {
//            this.role = StaffRole.ONBOARDING;
//        }
        if (this.isActive == null) {
            this.isActive = false;
        }
    }
}
