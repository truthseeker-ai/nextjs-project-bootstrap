package com.healthcare.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for managing doctor's time slots
 * Used for adding and removing availability slots
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TimeSlotDTO {
    
    @NotNull(message = "Start time is required")
    @Future(message = "Start time must be in the future")
    private LocalDateTime startTime;
    
    @NotNull(message = "End time is required")
    @Future(message = "End time must be in the future")
    private LocalDateTime endTime;
    
    @NotNull(message = "Duration in minutes is required")
    private Integer durationInMinutes;
    
    /**
     * Validate if the time slot is valid
     * @return true if end time is after start time and duration is positive
     */
    public boolean isValid() {
        return endTime.isAfter(startTime) && durationInMinutes > 0;
    }

    /**
     * Calculate the number of available slots based on duration
     * @return number of slots
     */
    public int getNumberOfSlots() {
        long totalMinutes = java.time.Duration.between(startTime, endTime).toMinutes();
        return (int) (totalMinutes / durationInMinutes);
    }

    /**
     * Generate all time slots between start and end time based on duration
     * @return array of time slots
     */
    public LocalDateTime[] generateTimeSlots() {
        int slots = getNumberOfSlots();
        LocalDateTime[] timeSlots = new LocalDateTime[slots];
        
        for (int i = 0; i < slots; i++) {
            timeSlots[i] = startTime.plusMinutes((long) i * durationInMinutes);
        }
        
        return timeSlots;
    }
}
