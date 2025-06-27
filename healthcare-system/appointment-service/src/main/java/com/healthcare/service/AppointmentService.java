package com.healthcare.service;

import com.healthcare.model.Appointment;
import com.healthcare.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Transactional
    public Appointment createAppointment(Appointment appointment) {
        // Validate time slot availability
        if (!isTimeSlotAvailable(appointment.getDoctorId(), appointment.getAppointmentDate())) {
            throw new RuntimeException("Selected time slot is not available");
        }
        
        appointment.setStatus(Appointment.AppointmentStatus.PENDING);
        return appointmentRepository.save(appointment);
    }

    @Transactional
    public Appointment updateAppointmentStatus(Long appointmentId, Appointment.AppointmentStatus status) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
            .orElseThrow(() -> new RuntimeException("Appointment not found"));
            
        // Validate status transition
        if (appointment.getStatus() == Appointment.AppointmentStatus.CANCELLED) {
            throw new RuntimeException("Cannot update cancelled appointment");
        }
        
        appointment.setStatus(status);
        return appointmentRepository.save(appointment);
    }

    public List<Appointment> getDoctorAppointments(Long doctorId) {
        return appointmentRepository.findByDoctorId(doctorId);
    }

    public List<Appointment> getPatientAppointments(Long patientId) {
        return appointmentRepository.findByPatientId(patientId);
    }

    public List<Appointment> getDoctorAppointmentsForDateRange(Long doctorId, LocalDateTime start, LocalDateTime end) {
        return appointmentRepository.findByDoctorIdAndAppointmentDateBetween(doctorId, start, end);
    }

    public boolean isTimeSlotAvailable(Long doctorId, LocalDateTime proposedTime) {
        // Check 30 minutes before and after the proposed time
        LocalDateTime rangeStart = proposedTime.minusMinutes(30);
        LocalDateTime rangeEnd = proposedTime.plusMinutes(30);
        
        List<Appointment> existingAppointments = getDoctorAppointmentsForDateRange(doctorId, rangeStart, rangeEnd);
        
        // Filter out cancelled appointments
        return existingAppointments.stream()
            .noneMatch(apt -> apt.getStatus() != Appointment.AppointmentStatus.CANCELLED);
    }

    public List<LocalDateTime> getAvailableTimeSlots(Long doctorId, LocalDateTime date) {
        // Get all appointments for the day
        LocalDateTime startOfDay = date.toLocalDate().atStartOfDay();
        LocalDateTime endOfDay = startOfDay.plusDays(1);
        
        List<Appointment> dayAppointments = getDoctorAppointmentsForDateRange(doctorId, startOfDay, endOfDay);
        
        // Generate available time slots (assuming 30-minute intervals during working hours 9 AM to 5 PM)
        List<LocalDateTime> availableSlots = new java.util.ArrayList<>();
        LocalDateTime timeSlot = startOfDay.plusHours(9); // Start at 9 AM
        
        while (timeSlot.isBefore(startOfDay.plusHours(17))) { // Until 5 PM
            if (isTimeSlotAvailable(doctorId, timeSlot)) {
                availableSlots.add(timeSlot);
            }
            timeSlot = timeSlot.plusMinutes(30);
        }
        
        return availableSlots;
    }
}
