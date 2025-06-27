package com.healthcare.service.impl;

import com.healthcare.dto.DepartmentDTO;
import com.healthcare.model.Department;
import com.healthcare.model.Hospital;
import com.healthcare.repository.DepartmentRepository;
import com.healthcare.repository.HospitalRepository;
import com.healthcare.service.DepartmentService;
import com.healthcare.exception.BusinessException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class DepartmentServiceImpl implements DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private HospitalRepository hospitalRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public DepartmentDTO createDepartment(DepartmentDTO departmentDTO) {
        Hospital hospital = hospitalRepository.findById(departmentDTO.getHospitalId())
            .orElseThrow(() -> new BusinessException("Hospital not found"));

        if (departmentRepository.existsByNameAndHospitalId(departmentDTO.getName(), departmentDTO.getHospitalId())) {
            throw new BusinessException("Department with this name already exists in the hospital");
        }

        Department department = modelMapper.map(departmentDTO, Department.class);
        department.setHospital(hospital);
        department = departmentRepository.save(department);
        return modelMapper.map(department, DepartmentDTO.class);
    }

    @Override
    public DepartmentDTO updateDepartment(Long id, DepartmentDTO departmentDTO) {
        Department department = departmentRepository.findById(id)
            .orElseThrow(() -> new BusinessException("Department not found"));

        Hospital hospital = hospitalRepository.findById(departmentDTO.getHospitalId())
            .orElseThrow(() -> new BusinessException("Hospital not found"));

        modelMapper.map(departmentDTO, department);
        department.setId(id);
        department.setHospital(hospital);
        department = departmentRepository.save(department);
        return modelMapper.map(department, DepartmentDTO.class);
    }

    @Override
    public DepartmentDTO getDepartmentById(Long id) {
        Department department = departmentRepository.findById(id)
            .orElseThrow(() -> new BusinessException("Department not found"));
        return modelMapper.map(department, DepartmentDTO.class);
    }

    @Override
    public List<DepartmentDTO> getAllDepartments() {
        return departmentRepository.findAll().stream()
            .map(department -> modelMapper.map(department, DepartmentDTO.class))
            .collect(Collectors.toList());
    }

    @Override
    public List<DepartmentDTO> getDepartmentsByHospital(Long hospitalId) {
        return departmentRepository.findByHospitalId(hospitalId).stream()
            .map(department -> modelMapper.map(department, DepartmentDTO.class))
            .collect(Collectors.toList());
    }

    @Override
    public List<DepartmentDTO> getActiveDepartmentsByHospital(Long hospitalId) {
        return departmentRepository.findByHospitalIdAndIsActiveTrue(hospitalId).stream()
            .map(department -> modelMapper.map(department, DepartmentDTO.class))
            .collect(Collectors.toList());
    }

    @Override
    public void deleteDepartment(Long id) {
        Department department = departmentRepository.findById(id)
            .orElseThrow(() -> new BusinessException("Department not found"));
        department.setIsActive(false);
        departmentRepository.save(department);
    }

    @Override
    public DepartmentDTO getDepartmentByNameAndHospital(String name, Long hospitalId) {
        Department department = departmentRepository.findByNameAndHospitalId(name, hospitalId)
            .orElseThrow(() -> new BusinessException("Department not found"));
        return modelMapper.map(department, DepartmentDTO.class);
    }

    @Override
    public List<DepartmentDTO> getDepartmentsBySpecialization(String specialization) {
        return departmentRepository.findBySpecializationsContainingIgnoreCase(specialization).stream()
            .map(department -> modelMapper.map(department, DepartmentDTO.class))
            .collect(Collectors.toList());
    }

    @Override
    public boolean existsByNameAndHospital(String name, Long hospitalId) {
        return departmentRepository.existsByNameAndHospitalId(name, hospitalId);
    }

    @Override
    public Integer countDepartmentsByHospital(Long hospitalId) {
        return departmentRepository.countByHospitalId(hospitalId);
    }

    @Override
    public List<DepartmentDTO> getDepartmentsByHeadDoctor(String headDoctor) {
        return departmentRepository.findByHeadDoctor(headDoctor).stream()
            .map(department -> modelMapper.map(department, DepartmentDTO.class))
            .collect(Collectors.toList());
    }
}
