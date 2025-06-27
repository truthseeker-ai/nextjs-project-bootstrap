package com.healthcare.repository;

import com.healthcare.model.Availability;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AvailabilityRepository extends JpaRepository<Availability, Long> {
    
    List<Availability> findByDoctorIdAndIsActiveTrue(Long doctorId);
    
    List<Availability> findByDoctorIdAndDayOfWeekAndIsActiveTrue(Long doctorId, DayOfWeek dayOfWeek);
    
    @Query("SELECT a FROM Availability a WHERE a.doctor.id = :doctorId " +
           "AND a.isActive = true " +
           "AND a.dayOfWeek = :dayOfWeek " +
           "AND :time BETWEEN a.startTime AND a.endTime")
    List<Availability> findAvailableSlots(Long doctorId, DayOfWeek dayOfWeek, LocalDateTime time);
    
    @Query("SELECT DISTINCT a.dayOfWeek FROM Availability a " +
           "WHERE a.doctor.id = :doctorId AND a.isActive = true")
    List<DayOfWeek> findAvailableDays(Long doctorId);
}
