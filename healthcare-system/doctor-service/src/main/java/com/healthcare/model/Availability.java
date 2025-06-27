package com.healthcare.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "doctor_availability")
public class Availability {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DayOfWeek dayOfWeek;

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    @Column(name = "slot_duration_minutes", nullable = false)
    private Integer slotDurationMinutes = 30;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Column(name = "break_start_time")
    private LocalTime breakStartTime;

    @Column(name = "break_end_time")
    private LocalTime breakEndTime;

    @PrePersist
    @PreUpdate
    private void validateTimeSlots() {
        if (startTime.isAfter(endTime)) {
            throw new IllegalStateException("Start time must be before end time");
        }
        
        if (breakStartTime != null && breakEndTime != null) {
            if (breakStartTime.isBefore(startTime) || breakEndTime.isAfter(endTime)) {
                throw new IllegalStateException("Break time must be within working hours");
            }
            if (breakStartTime.isAfter(breakEndTime)) {
                throw new IllegalStateException("Break start time must be before break end time");
            }
        }
    }

    public boolean isTimeSlotAvailable(LocalTime time) {
        if (!isActive) return false;
        
        if (time.isBefore(startTime) || time.isAfter(endTime)) {
            return false;
        }

        if (breakStartTime != null && breakEndTime != null) {
            if (!time.isBefore(breakStartTime) && !time.isAfter(breakEndTime)) {
                return false;
            }
        }

        return true;
    }
}
