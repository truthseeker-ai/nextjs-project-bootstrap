package com.healthcare.repository;

import com.healthcare.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {
    List<Department> findByHospitalId(Long hospitalId);
    List<Department> findByHospitalIdAndIsActiveTrue(Long hospitalId);
    Optional<Department> findByNameAndHospitalId(String name, Long hospitalId);
    List<Department> findBySpecializationsContainingIgnoreCase(String specialization);
    boolean existsByNameAndHospitalId(String name, Long hospitalId);
    List<Department> findByHeadDoctor(String headDoctor);
    Integer countByHospitalId(Long hospitalId);
}
