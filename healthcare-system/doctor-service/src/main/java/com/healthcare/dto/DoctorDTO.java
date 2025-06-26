package com.healthcare.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * DTO for transferring Doctor data
 * Used for registration and profile updates
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DoctorDTO {
    
    private Long id;
    
    @NotBlank(message = "First name is required")
    private String firstName;
    
    @NotBlank(message = "Last name is required")
    private String lastName;
    
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;
    
    private String password;
    
    @NotBlank(message = "Specialization is required")
    private String specialization;
    
    private String licenseNumber;
    
    @Positive(message = "Years of experience must be positive")
    private Integer yearsOfExperience;
    
    private String phoneNumber;
    
    private Set<String> qualifications;
    
    private Set<LocalDateTime> availableSlots;
    
    private String about;
    
    @Positive(message = "Consultation fee must be positive")
    private Double consultationFee;
    
    private boolean isAvailable;
    
    private Double rating;
    
    private Integer totalPatients;

    /**
     * Convert Doctor entity to DoctorDTO
     * @param doctor Doctor entity
     * @return DoctorDTO
     */
    public static DoctorDTO fromEntity(com.healthcare.model.Doctor doctor) {
        return DoctorDTO.builder()
                .id(doctor.getId())
                .firstName(doctor.getFirstName())
                .lastName(doctor.getLastName())
                .email(doctor.getEmail())
                .specialization(doctor.getSpecialization())
                .licenseNumber(doctor.getLicenseNumber())
                .yearsOfExperience(doctor.getYearsOfExperience())
                .phoneNumber(doctor.getPhoneNumber())
                .qualifications(doctor.getQualifications())
                .availableSlots(doctor.getAvailableSlots())
                .about(doctor.getAbout())
                .consultationFee(doctor.getConsultationFee())
                .isAvailable(doctor.isAvailable())
                .rating(doctor.getRating())
                .totalPatients(doctor.getTotalPatients())
                .build();
    }

    /**
     * Convert DoctorDTO to Doctor entity
     * @return Doctor entity
     */
    public com.healthcare.model.Doctor toEntity() {
        com.healthcare.model.Doctor doctor = new com.healthcare.model.Doctor();
        doctor.setId(this.id);
        doctor.setFirstName(this.firstName);
        doctor.setLastName(this.lastName);
        doctor.setEmail(this.email);
        if (this.password != null) {
            doctor.setPassword(this.password);
        }
        doctor.setSpecialization(this.specialization);
        doctor.setLicenseNumber(this.licenseNumber);
        doctor.setYearsOfExperience(this.yearsOfExperience);
        doctor.setPhoneNumber(this.phoneNumber);
        doctor.setQualifications(this.qualifications);
        doctor.setAvailableSlots(this.availableSlots);
        doctor.setAbout(this.about);
        doctor.setConsultationFee(this.consultationFee);
        doctor.setAvailable(this.isAvailable);
        doctor.setRating(this.rating);
        doctor.setTotalPatients(this.totalPatients);
        return doctor;
    }
}
