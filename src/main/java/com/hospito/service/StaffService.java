package com.hospito.service;

import com.hospito.entity.Staff;
import com.hospito.entity.num.StaffRole;
import com.hospito.exception.HospitoException;
import com.hospito.repository.StaffRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StaffService {
    private final StaffRepository staffRepository;
    private final PasswordEncoder passwordEncoder;

    public Staff registerStaff(Staff staff){
        if(staffRepository.existsByUsername(staff.getUsername())){
            throw new HospitoException(staff.getUsername()+" already taken!", HttpStatus.CONFLICT);
        }
        if(staffRepository.existsByPhoneNumber(staff.getPhoneNumber())){
            throw new HospitoException(staff.getPhoneNumber()+" already exist!", HttpStatus.CONFLICT);
        }
        if(staffRepository.existsByEmail(staff.getEmail())){
            throw new HospitoException(staff.getEmail()+" already exist!", HttpStatus.CONFLICT);
        }
        staff.setPassword(passwordEncoder.encode(staff.getPassword()));
        staff.setRole(StaffRole.ONBOARDING);
        return staffRepository.save(staff);
    }

    public Staff getStaffById(Long staffId){
        return staffRepository.findById(staffId)
                .orElseThrow(() -> new HospitoException("Staff with id "+staffId+" not found!", HttpStatus.NOT_FOUND));
    }

    public List<Staff> getStaffByRole(StaffRole staffRole){
        return staffRepository.findByRole(staffRole);
    }

    public Staff updateStaffRole(Long staffId,StaffRole newRole){
        Staff staff = getStaffById(staffId);
        staff.setRole(newRole);
        return staffRepository.save(staff);
    }

    public void deactivateStaff(Long staffId){
        Staff staff = getStaffById(staffId);
        staff.setIsActive(false);
        staffRepository.save(staff);
    }

    public Staff login(String usernameOrEmail,String password){
        Staff staff = staffRepository.findByUsername(usernameOrEmail)
                .or(()->staffRepository.findByEmail(usernameOrEmail))
                .orElseThrow(
                        ()->new HospitoException("Staff not found with "+usernameOrEmail,HttpStatus.NOT_FOUND)
                );
        if (!staff.getIsActive()){
            throw new HospitoException("Account is inactive.Contact Admin", HttpStatus.FORBIDDEN);
        }
        if(!password.equals(staff.getPassword())){
            throw new HospitoException("Wrong password!",HttpStatus.UNAUTHORIZED);
        }
        return staff;
    }
}
