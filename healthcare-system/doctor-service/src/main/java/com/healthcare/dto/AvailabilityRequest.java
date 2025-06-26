package com.healthcare.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO for managing multiple time slots in a single request
 * Used for bulk updates to doctor's availability
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AvailabilityRequest {
    
    @NotEmpty(message = "Time slots cannot be empty")
    private List<@Valid TimeSlotDTO> timeSlots;
    
    private boolean clearExisting;
}
