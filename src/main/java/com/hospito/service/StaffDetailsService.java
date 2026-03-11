package com.hospito.service;

import com.hospito.entity.Staff;
import com.hospito.repository.StaffRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class StaffDetailsService implements UserDetailsService {
    private final StaffRepository  staffRepository;

    public StaffDetailsService(StaffRepository staffRepository) {
        this.staffRepository = staffRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Staff staff = staffRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User NOt found with username: " + username));
        return new User(staff.getUsername(), staff.getPassword(), Collections.singleton(new SimpleGrantedAuthority("ROLE_"+staff.getRole())));
    }
}
