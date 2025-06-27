package com.healthcare.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotNull;
import java.time.DayOfWeek;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AvailabilityDTO {
    
    private Long id;
    
    @NotNull(message = "Doctor ID is required")
    private Long doctorId;
    
    @NotNull(message = "Day of week is required")
    private DayOfWeek dayOfWeek;
    
    @NotNull(message = "Start time is required")
    @JsonFormat(pattern = "HH:mm")
    private LocalTime startTime;
    
    @NotNull(message = "End time is required")
    @JsonFormat(pattern = "HH:mm")
    private LocalTime endTime;
    
    private Integer slotDurationMinutes = 30;
    
    private Boolean isActive = true;
    
    @JsonFormat(pattern = "HH:mm")
    private LocalTime breakStartTime;
    
    @JsonFormat(pattern = "HH:mm")
    private LocalTime breakEndTime;

    // Request DTO for bulk schedule creation
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BulkScheduleRequest {
        @NotNull(message = "Doctor ID is required")
        private Long doctorId;
        
        @NotNull(message = "Days are required")
        private DayOfWeek[] days;
        
        @NotNull(message = "Start time is required")
        @JsonFormat(pattern = "HH:mm")
        private LocalTime startTime;
        
        @NotNull(message = "End time is required")
        @JsonFormat(pattern = "HH:mm")
        private LocalTime endTime;
        
        private Integer slotDurationMinutes = 30;
        
        @JsonFormat(pattern = "HH:mm")
        private LocalTime breakStartTime;
        
        @JsonFormat(pattern = "HH:mm")
        private LocalTime breakEndTime;
    }

    // Response DTO for available time slots
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TimeSlotResponse {
        private DayOfWeek dayOfWeek;
        @JsonFormat(pattern = "HH:mm")
        private LocalTime time;
        private boolean available;
    }
}
