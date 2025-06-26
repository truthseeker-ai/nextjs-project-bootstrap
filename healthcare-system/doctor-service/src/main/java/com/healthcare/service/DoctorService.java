package com.healthcare.service;

import com.healthcare.dto.*;
import com.healthcare.model.Doctor;
import com.healthcare.repository.DoctorRepository;
import com.healthcare.security.JwtTokenProvider;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Service class for handling doctor-related business logic
 * Manages doctor authentication, registration, and profile operations
 */
@Service
@RequiredArgsConstructor
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * Authenticate doctor and generate JWT token
     * @param authRequest Login credentials
     * @return AuthResponse containing JWT token and doctor details
     */
    public AuthResponse authenticate(AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                authRequest.getEmail(),
                authRequest.getPassword()
            )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtTokenProvider.generateToken(authentication);
        
        Doctor doctor = doctorRepository.findByEmail(authRequest.getEmail())
            .orElseThrow(() -> new EntityNotFoundException("Doctor not found"));

        return AuthResponse.success(jwt, DoctorDTO.fromEntity(doctor));
    }

    /**
     * Register a new doctor
     * @param doctorDTO Doctor registration details
     * @return Registered doctor DTO
     */
    @Transactional
    public DoctorDTO register(DoctorDTO doctorDTO) {
        if (doctorRepository.existsByEmail(doctorDTO.getEmail())) {
            throw new IllegalArgumentException("Email already registered");
        }

        Doctor doctor = doctorDTO.toEntity();
        doctor.setPassword(passwordEncoder.encode(doctorDTO.getPassword()));
        Doctor savedDoctor = doctorRepository.save(doctor);
        return DoctorDTO.fromEntity(savedDoctor);
    }

    /**
     * Update doctor's availability slots
     * @param doctorId Doctor ID
     * @param request Availability request containing time slots
     * @return Updated doctor DTO
     */
    @Transactional
    public DoctorDTO updateAvailability(Long doctorId, AvailabilityRequest request) {
        Doctor doctor = doctorRepository.findById(doctorId)
            .orElseThrow(() -> new EntityNotFoundException("Doctor not found"));

        if (request.isClearExisting()) {
            doctor.getAvailableSlots().clear();
        }

        Set<LocalDateTime> newSlots = request.getTimeSlots().stream()
            .filter(TimeSlotDTO::isValid)
            .flatMap(slot -> List.of(slot.generateTimeSlots()).stream())
            .collect(Collectors.toSet());

        doctor.getAvailableSlots().addAll(newSlots);
        Doctor updatedDoctor = doctorRepository.save(doctor);
        return DoctorDTO.fromEntity(updatedDoctor);
    }

    /**
     * Get doctor by ID
     * @param id Doctor ID
     * @return Doctor DTO
     */
    public DoctorDTO getDoctorById(Long id) {
        Doctor doctor = doctorRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Doctor not found"));
        return DoctorDTO.fromEntity(doctor);
    }

    /**
     * Update doctor profile
     * @param id Doctor ID
     * @param doctorDTO Updated doctor details
     * @return Updated doctor DTO
     */
    @Transactional
    public DoctorDTO updateDoctor(Long id, DoctorDTO doctorDTO) {
        Doctor doctor = doctorRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Doctor not found"));

        doctor.setFirstName(doctorDTO.getFirstName());
        doctor.setLastName(doctorDTO.getLastName());
        doctor.setSpecialization(doctorDTO.getSpecialization());
        doctor.setPhoneNumber(doctorDTO.getPhoneNumber());
        doctor.setAbout(doctorDTO.getAbout());
        doctor.setConsultationFee(doctorDTO.getConsultationFee());
        doctor.setAvailable(doctorDTO.isAvailable());

        if (doctorDTO.getQualifications() != null) {
            doctor.setQualifications(doctorDTO.getQualifications());
        }

        if (doctorDTO.getPassword() != null && !doctorDTO.getPassword().isEmpty()) {
            doctor.setPassword(passwordEncoder.encode(doctorDTO.getPassword()));
        }

        Doctor updatedDoctor = doctorRepository.save(doctor);
        return DoctorDTO.fromEntity(updatedDoctor);
    }

    /**
     * Find doctors by specialization
     * @param specialization Medical specialization
     * @return List of doctor DTOs
     */
    public List<DoctorDTO> findBySpecialization(String specialization) {
        return doctorRepository.findBySpecializationAndIsAvailableTrue(specialization)
            .stream()
            .map(DoctorDTO::fromEntity)
            .collect(Collectors.toList());
    }

    /**
     * Find available doctors in time range
     * @param startTime Start of time range
     * @param endTime End of time range
     * @return List of doctor DTOs
     */
    public List<DoctorDTO> findAvailableDoctors(LocalDateTime startTime, LocalDateTime endTime) {
        return doctorRepository.findAvailableDoctorsInTimeRange(startTime, endTime)
            .stream()
            .map(DoctorDTO::fromEntity)
            .collect(Collectors.toList());
    }

    /**
     * Get top rated doctors
     * @param limit Number of doctors to return
     * @return List of doctor DTOs
     */
    public List<DoctorDTO> getTopRatedDoctors(int limit) {
        return doctorRepository.findTopRatedDoctors(limit)
            .stream()
            .map(DoctorDTO::fromEntity)
            .collect(Collectors.toList());
    }

    /**
     * Delete doctor by ID
     * @param id Doctor ID
     */
    @Transactional
    public void deleteDoctor(Long id) {
        if (!doctorRepository.existsById(id)) {
            throw new EntityNotFoundException("Doctor not found");
        }
        doctorRepository.deleteById(id);
    }
}
