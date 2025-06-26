package com.healthcare.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for authentication response
 * Contains JWT token and basic doctor information
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private String email;
    private String firstName;
    private String lastName;
    private String specialization;
    private String licenseNumber;
    private boolean isAvailable;
    private String message;

    /**
     * Create success response
     * @param token JWT token
     * @param doctor Doctor DTO
     * @return AuthResponse
     */
    public static AuthResponse success(String token, DoctorDTO doctor) {
        return AuthResponse.builder()
                .token(token)
                .email(doctor.getEmail())
                .firstName(doctor.getFirstName())
                .lastName(doctor.getLastName())
                .specialization(doctor.getSpecialization())
                .licenseNumber(doctor.getLicenseNumber())
                .isAvailable(doctor.isAvailable())
                .message("Successfully authenticated")
                .build();
    }

    /**
     * Create error response
     * @param message Error message
     * @return AuthResponse
     */
    public static AuthResponse error(String message) {
        return AuthResponse.builder()
                .message(message)
                .build();
    }
}
