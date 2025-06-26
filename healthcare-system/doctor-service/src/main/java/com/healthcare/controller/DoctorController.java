package com.healthcare.controller;

import com.healthcare.dto.*;
import com.healthcare.service.DoctorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * REST controller for doctor-related operations
 * Exposes endpoints for doctor authentication, registration, and profile management
 */
@RestController
@RequestMapping("/api/doctors")
@RequiredArgsConstructor
public class DoctorController {

    private final DoctorService doctorService;

    /**
     * Authenticate doctor
     * @param authRequest Login credentials
     * @return Authentication response with JWT token
     */
    @PostMapping("/auth/login")
    public ResponseEntity<AuthResponse> authenticate(@RequestBody @Valid AuthRequest authRequest) {
        return ResponseEntity.ok(doctorService.authenticate(authRequest));
    }

    /**
     * Register new doctor
     * @param doctorDTO Doctor registration details
     * @return Registered doctor details
     */
    @PostMapping("/auth/register")
    public ResponseEntity<DoctorDTO> register(@RequestBody @Valid DoctorDTO doctorDTO) {
        return ResponseEntity.ok(doctorService.register(doctorDTO));
    }

    /**
     * Get doctor by ID
     * @param id Doctor ID
     * @return Doctor details
     */
    @GetMapping("/{id}")
    public ResponseEntity<DoctorDTO> getDoctor(@PathVariable Long id) {
        return ResponseEntity.ok(doctorService.getDoctorById(id));
    }

    /**
     * Update doctor profile
     * @param id Doctor ID
     * @param doctorDTO Updated doctor details
     * @return Updated doctor details
     */
    @PutMapping("/{id}")
    public ResponseEntity<DoctorDTO> updateDoctor(
            @PathVariable Long id,
            @RequestBody @Valid DoctorDTO doctorDTO) {
        return ResponseEntity.ok(doctorService.updateDoctor(id, doctorDTO));
    }

    /**
     * Update doctor availability slots
     * @param id Doctor ID
     * @param request Availability request
     * @return Updated doctor details
     */
    @PutMapping("/{id}/availability")
    public ResponseEntity<DoctorDTO> updateAvailability(
            @PathVariable Long id,
            @RequestBody @Valid AvailabilityRequest request) {
        return ResponseEntity.ok(doctorService.updateAvailability(id, request));
    }

    /**
     * Find doctors by specialization
     * @param specialization Medical specialization
     * @return List of matching doctors
     */
    @GetMapping("/specialization/{specialization}")
    public ResponseEntity<List<DoctorDTO>> findBySpecialization(
            @PathVariable String specialization) {
        return ResponseEntity.ok(doctorService.findBySpecialization(specialization));
    }

    /**
     * Find available doctors in time range
     * @param startTime Start time (ISO format)
     * @param endTime End time (ISO format)
     * @return List of available doctors
     */
    @GetMapping("/available")
    public ResponseEntity<List<DoctorDTO>> findAvailableDoctors(
            @RequestParam LocalDateTime startTime,
            @RequestParam LocalDateTime endTime) {
        return ResponseEntity.ok(doctorService.findAvailableDoctors(startTime, endTime));
    }

    /**
     * Get top rated doctors
     * @param limit Number of doctors to return
     * @return List of top rated doctors
     */
    @GetMapping("/top-rated")
    public ResponseEntity<List<DoctorDTO>> getTopRatedDoctors(
            @RequestParam(defaultValue = "10") int limit) {
        return ResponseEntity.ok(doctorService.getTopRatedDoctors(limit));
    }

    /**
     * Delete doctor
     * @param id Doctor ID
     * @return No content response
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDoctor(@PathVariable Long id) {
        doctorService.deleteDoctor(id);
        return ResponseEntity.noContent().build();
    }
}
