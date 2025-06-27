package com.healthcare.repository;

import com.healthcare.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {
    Optional<Admin> findByUsername(String username);
    Optional<Admin> findByEmail(String email);
    List<Admin> findByHospitalId(Long hospitalId);
    List<Admin> findByIsActiveTrue();
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    List<Admin> findByRole(String role);
    List<Admin> findByDepartment(String department);
    Optional<Admin> findByUsernameAndIsActiveTrue(String username);
    List<Admin> findByHospitalIdAndIsActiveTrue(Long hospitalId);
}
