package com.healthcare.service;

import com.healthcare.dto.AvailabilityDTO;
import com.healthcare.model.Availability;
import com.healthcare.model.Doctor;
import com.healthcare.repository.AvailabilityRepository;
import com.healthcare.repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AvailabilityService {

    @Autowired
    private AvailabilityRepository availabilityRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Transactional
    public AvailabilityDTO createAvailability(AvailabilityDTO dto) {
        Doctor doctor = doctorRepository.findById(dto.getDoctorId())
            .orElseThrow(() -> new RuntimeException("Doctor not found"));

        Availability availability = new Availability();
        availability.setDoctor(doctor);
        availability.setDayOfWeek(dto.getDayOfWeek());
        availability.setStartTime(dto.getStartTime());
        availability.setEndTime(dto.getEndTime());
        availability.setSlotDurationMinutes(dto.getSlotDurationMinutes());
        availability.setBreakStartTime(dto.getBreakStartTime());
        availability.setBreakEndTime(dto.getBreakEndTime());
        availability.setIsActive(true);

        availability = availabilityRepository.save(availability);
        return convertToDTO(availability);
    }

    @Transactional
    public List<AvailabilityDTO> createBulkSchedule(AvailabilityDTO.BulkScheduleRequest request) {
        List<AvailabilityDTO> created = new ArrayList<>();
        
        for (DayOfWeek day : request.getDays()) {
            AvailabilityDTO dto = new AvailabilityDTO();
            dto.setDoctorId(request.getDoctorId());
            dto.setDayOfWeek(day);
            dto.setStartTime(request.getStartTime());
            dto.setEndTime(request.getEndTime());
            dto.setSlotDurationMinutes(request.getSlotDurationMinutes());
            dto.setBreakStartTime(request.getBreakStartTime());
            dto.setBreakEndTime(request.getBreakEndTime());
            
            created.add(createAvailability(dto));
        }
        
        return created;
    }

    public List<AvailabilityDTO.TimeSlotResponse> getAvailableTimeSlots(
            Long doctorId, LocalDate date) {
        List<Availability> availabilities = availabilityRepository
            .findByDoctorIdAndDayOfWeekAndIsActiveTrue(doctorId, date.getDayOfWeek());

        List<AvailabilityDTO.TimeSlotResponse> slots = new ArrayList<>();
        
        for (Availability availability : availabilities) {
            LocalTime currentTime = availability.getStartTime();
            
            while (currentTime.plusMinutes(availability.getSlotDurationMinutes())
                    .isBefore(availability.getEndTime()) || 
                   currentTime.plusMinutes(availability.getSlotDurationMinutes())
                    .equals(availability.getEndTime())) {
                
                boolean isAvailable = availability.isTimeSlotAvailable(currentTime);
                
                // Check if slot is during break time
                if (availability.getBreakStartTime() != null && 
                    availability.getBreakEndTime() != null) {
                    if (!currentTime.isBefore(availability.getBreakStartTime()) && 
                        !currentTime.isAfter(availability.getBreakEndTime())) {
                        isAvailable = false;
                    }
                }
                
                slots.add(new AvailabilityDTO.TimeSlotResponse(
                    availability.getDayOfWeek(),
                    currentTime,
                    isAvailable
                ));
                
                currentTime = currentTime.plusMinutes(availability.getSlotDurationMinutes());
            }
        }
        
        return slots;
    }

    @Transactional
    public AvailabilityDTO updateAvailability(Long id, AvailabilityDTO dto) {
        Availability availability = availabilityRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Availability not found"));
        
        availability.setStartTime(dto.getStartTime());
        availability.setEndTime(dto.getEndTime());
        availability.setSlotDurationMinutes(dto.getSlotDurationMinutes());
        availability.setBreakStartTime(dto.getBreakStartTime());
        availability.setBreakEndTime(dto.getBreakEndTime());
        availability.setIsActive(dto.getIsActive());
        
        availability = availabilityRepository.save(availability);
        return convertToDTO(availability);
    }

    @Transactional
    public void deleteAvailability(Long id) {
        Availability availability = availabilityRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Availability not found"));
        
        availability.setIsActive(false);
        availabilityRepository.save(availability);
    }

    public List<DayOfWeek> getAvailableDays(Long doctorId) {
        return availabilityRepository.findAvailableDays(doctorId);
    }

    public boolean isSlotAvailable(Long doctorId, LocalDateTime dateTime) {
        List<Availability> availabilities = availabilityRepository
            .findAvailableSlots(doctorId, dateTime.getDayOfWeek(), dateTime);
            
        return !availabilities.isEmpty() && 
               availabilities.stream()
                   .anyMatch(a -> a.isTimeSlotAvailable(dateTime.toLocalTime()));
    }

    private AvailabilityDTO convertToDTO(Availability availability) {
        AvailabilityDTO dto = new AvailabilityDTO();
        dto.setId(availability.getId());
        dto.setDoctorId(availability.getDoctor().getId());
        dto.setDayOfWeek(availability.getDayOfWeek());
        dto.setStartTime(availability.getStartTime());
        dto.setEndTime(availability.getEndTime());
        dto.setSlotDurationMinutes(availability.getSlotDurationMinutes());
        dto.setBreakStartTime(availability.getBreakStartTime());
        dto.setBreakEndTime(availability.getBreakEndTime());
        dto.setIsActive(availability.getIsActive());
        return dto;
    }
}
