package com.healthcare.service;

import com.healthcare.dto.DepartmentDTO;
import java.util.List;

public interface DepartmentService {
    DepartmentDTO createDepartment(DepartmentDTO departmentDTO);
    DepartmentDTO updateDepartment(Long id, DepartmentDTO departmentDTO);
    DepartmentDTO getDepartmentById(Long id);
    List<DepartmentDTO> getAllDepartments();
    List<DepartmentDTO> getDepartmentsByHospital(Long hospitalId);
    List<DepartmentDTO> getActiveDepartmentsByHospital(Long hospitalId);
    void deleteDepartment(Long id);
    DepartmentDTO getDepartmentByNameAndHospital(String name, Long hospitalId);
    List<DepartmentDTO> getDepartmentsBySpecialization(String specialization);
    boolean existsByNameAndHospital(String name, Long hospitalId);
    Integer countDepartmentsByHospital(Long hospitalId);
    List<DepartmentDTO> getDepartmentsByHeadDoctor(String headDoctor);
}
