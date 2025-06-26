package com.healthcare.repository;

import com.healthcare.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Doctor entity
 * Provides CRUD operations and custom queries for Doctor data
 */
@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    
    /**
     * Find a doctor by email
     * @param email Doctor's email
     * @return Optional containing the doctor if found
     */
    Optional<Doctor> findByEmail(String email);

    /**
     * Check if a doctor exists with the given email
     * @param email Doctor's email
     * @return true if a doctor exists with this email
     */
    boolean existsByEmail(String email);

    /**
     * Find doctors by specialization
     * @param specialization Medical specialization
     * @return List of doctors with the given specialization
     */
    List<Doctor> findBySpecialization(String specialization);

    /**
     * Find available doctors by specialization
     * @param specialization Medical specialization
     * @return List of available doctors with the given specialization
     */
    List<Doctor> findBySpecializationAndIsAvailableTrue(String specialization);

    /**
     * Find doctors by rating greater than or equal to the given value
     * @param rating Minimum rating value
     * @return List of doctors with rating >= given value
     */
    List<Doctor> findByRatingGreaterThanEqual(Double rating);

    /**
     * Find doctors who have available slots in the given time range
     * @param startTime Start of time range
     * @param endTime End of time range
     * @return List of doctors with available slots in the range
     */
    @Query("SELECT DISTINCT d FROM Doctor d JOIN d.availableSlots s " +
           "WHERE s BETWEEN :startTime AND :endTime AND d.isAvailable = true")
    List<Doctor> findAvailableDoctorsInTimeRange(
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime);

    /**
     * Find doctors by specialization and experience
     * @param specialization Medical specialization
     * @param minExperience Minimum years of experience
     * @return List of doctors matching criteria
     */
    @Query("SELECT d FROM Doctor d WHERE d.specialization = :specialization " +
           "AND d.yearsOfExperience >= :minExperience AND d.isAvailable = true")
    List<Doctor> findBySpecializationAndMinExperience(
            @Param("specialization") String specialization,
            @Param("minExperience") Integer minExperience);

    /**
     * Find top rated doctors
     * @param limit Number of doctors to return
     * @return List of top rated doctors
     */
    @Query("SELECT d FROM Doctor d WHERE d.isAvailable = true " +
           "ORDER BY d.rating DESC, d.totalPatients DESC")
    List<Doctor> findTopRatedDoctors(@Param("limit") int limit);
}
