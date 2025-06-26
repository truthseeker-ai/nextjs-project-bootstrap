package com.healthcare.security;

import com.healthcare.model.Doctor;
import com.healthcare.repository.DoctorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Custom UserDetailsService implementation for doctor authentication
 * Loads doctor-specific user data for Spring Security
 */
@Service
@RequiredArgsConstructor
public class DoctorUserDetailsService implements UserDetailsService {

    private final DoctorRepository doctorRepository;

    /**
     * Load doctor by email (username)
     * @param email Doctor's email
     * @return UserDetails containing doctor's authentication information
     * @throws UsernameNotFoundException if doctor not found
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Doctor doctor = doctorRepository.findByEmail(email)
            .orElseThrow(() -> 
                new UsernameNotFoundException("Doctor not found with email: " + email));

        return org.springframework.security.core.userdetails.User
            .withUsername(doctor.getEmail())
            .password(doctor.getPassword())
            .authorities("ROLE_DOCTOR")
            .accountExpired(false)
            .accountLocked(false)
            .credentialsExpired(false)
            .disabled(false)
            .build();
    }
}
