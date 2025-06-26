package com.healthcare.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Entity class representing a Doctor in the system
 * Contains doctor's personal information, specialization, and schedule
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "doctors")
public class Doctor {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "First name is required")
    @Column(name = "first_name")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Column(name = "last_name")
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    @Column(unique = true)
    private String email;

    @NotBlank(message = "Password is required")
    private String password;

    @NotBlank(message = "Specialization is required")
    private String specialization;

    @Column(name = "license_number")
    private String licenseNumber;

    @Column(name = "years_of_experience")
    private Integer yearsOfExperience;

    @Column(name = "phone_number")
    private String phoneNumber;

    @ElementCollection
    @CollectionTable(name = "doctor_qualifications", 
                    joinColumns = @JoinColumn(name = "doctor_id"))
    @Column(name = "qualification")
    private Set<String> qualifications = new HashSet<>();

    @ElementCollection
    @CollectionTable(name = "doctor_available_slots", 
                    joinColumns = @JoinColumn(name = "doctor_id"))
    @Column(name = "time_slot")
    private Set<LocalDateTime> availableSlots = new HashSet<>();

    private String about;

    @Column(name = "consultation_fee")
    private Double consultationFee;

    @Column(name = "is_available")
    private boolean isAvailable = true;

    @Column(name = "rating")
    private Double rating;

    @Column(name = "total_patients")
    private Integer totalPatients = 0;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    /**
     * Add a time slot to available slots
     * @param timeSlot The time slot to add
     */
    public void addTimeSlot(LocalDateTime timeSlot) {
        availableSlots.add(timeSlot);
    }

    /**
     * Remove a time slot from available slots
     * @param timeSlot The time slot to remove
     */
    public void removeTimeSlot(LocalDateTime timeSlot) {
        availableSlots.remove(timeSlot);
    }

    /**
     * Add a qualification
     * @param qualification The qualification to add
     */
    public void addQualification(String qualification) {
        qualifications.add(qualification);
    }

    /**
     * Remove a qualification
     * @param qualification The qualification to remove
     */
    public void removeQualification(String qualification) {
        qualifications.remove(qualification);
    }
}
