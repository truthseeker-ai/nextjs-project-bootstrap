package com.healthcare.controller;

import com.healthcare.dto.AvailabilityDTO;
import com.healthcare.service.AvailabilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/availability")
public class AvailabilityController {

    @Autowired
    private AvailabilityService availabilityService;

    @PostMapping
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<AvailabilityDTO> createAvailability(
            @Valid @RequestBody AvailabilityDTO availabilityDTO) {
        return ResponseEntity.ok(availabilityService.createAvailability(availabilityDTO));
    }

    @PostMapping("/bulk")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<List<AvailabilityDTO>> createBulkSchedule(
            @Valid @RequestBody AvailabilityDTO.BulkScheduleRequest request) {
        return ResponseEntity.ok(availabilityService.createBulkSchedule(request));
    }

    @GetMapping("/doctor/{doctorId}/slots")
    public ResponseEntity<List<AvailabilityDTO.TimeSlotResponse>> getAvailableTimeSlots(
            @PathVariable Long doctorId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(availabilityService.getAvailableTimeSlots(doctorId, date));
    }

    @GetMapping("/doctor/{doctorId}/days")
    public ResponseEntity<List<DayOfWeek>> getAvailableDays(@PathVariable Long doctorId) {
        return ResponseEntity.ok(availabilityService.getAvailableDays(doctorId));
    }

    @GetMapping("/check")
    public ResponseEntity<Boolean> checkAvailability(
            @RequestParam Long doctorId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateTime) {
        return ResponseEntity.ok(availabilityService.isSlotAvailable(doctorId, dateTime));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<AvailabilityDTO> updateAvailability(
            @PathVariable Long id,
            @Valid @RequestBody AvailabilityDTO availabilityDTO) {
        return ResponseEntity.ok(availabilityService.updateAvailability(id, availabilityDTO));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<Void> deleteAvailability(@PathVariable Long id) {
        availabilityService.deleteAvailability(id);
        return ResponseEntity.ok().build();
    }
}
