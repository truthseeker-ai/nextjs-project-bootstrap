package com.healthcare.service.impl;

import com.healthcare.dto.HospitalDTO;
import com.healthcare.model.Hospital;
import com.healthcare.repository.HospitalRepository;
import com.healthcare.service.HospitalService;
import com.healthcare.exception.BusinessException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class HospitalServiceImpl implements HospitalService {

    @Autowired
    private HospitalRepository hospitalRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public HospitalDTO createHospital(HospitalDTO hospitalDTO) {
        if (hospitalRepository.existsByLicenseNumber(hospitalDTO.getLicenseNumber())) {
            throw new BusinessException("Hospital with this license number already exists");
        }
        Hospital hospital = modelMapper.map(hospitalDTO, Hospital.class);
        hospital = hospitalRepository.save(hospital);
        return modelMapper.map(hospital, HospitalDTO.class);
    }

    @Override
    public HospitalDTO updateHospital(Long id, HospitalDTO hospitalDTO) {
        Hospital hospital = hospitalRepository.findById(id)
            .orElseThrow(() -> new BusinessException("Hospital not found"));
        
        modelMapper.map(hospitalDTO, hospital);
        hospital.setId(id); // Ensure ID remains unchanged
        hospital = hospitalRepository.save(hospital);
        return modelMapper.map(hospital, HospitalDTO.class);
    }

    @Override
    public HospitalDTO getHospitalById(Long id) {
        Hospital hospital = hospitalRepository.findById(id)
            .orElseThrow(() -> new BusinessException("Hospital not found"));
        return modelMapper.map(hospital, HospitalDTO.class);
    }

    @Override
    public List<HospitalDTO> getAllHospitals() {
        return hospitalRepository.findAll().stream()
            .map(hospital -> modelMapper.map(hospital, HospitalDTO.class))
            .collect(Collectors.toList());
    }

    @Override
    public List<HospitalDTO> getActiveHospitals() {
        return hospitalRepository.findByIsActiveTrue().stream()
            .map(hospital -> modelMapper.map(hospital, HospitalDTO.class))
            .collect(Collectors.toList());
    }

    @Override
    public void deleteHospital(Long id) {
        Hospital hospital = hospitalRepository.findById(id)
            .orElseThrow(() -> new BusinessException("Hospital not found"));
        hospital.setIsActive(false);
        hospitalRepository.save(hospital);
    }

    @Override
    public HospitalDTO getHospitalByName(String name) {
        Hospital hospital = hospitalRepository.findByName(name)
            .orElseThrow(() -> new BusinessException("Hospital not found"));
        return modelMapper.map(hospital, HospitalDTO.class);
    }

    @Override
    public List<HospitalDTO> getHospitalsBySpecialization(String specialization) {
        return hospitalRepository.findBySpecializationsContainingIgnoreCase(specialization).stream()
            .map(hospital -> modelMapper.map(hospital, HospitalDTO.class))
            .collect(Collectors.toList());
    }

    @Override
    public boolean existsByLicenseNumber(String licenseNumber) {
        return hospitalRepository.existsByLicenseNumber(licenseNumber);
    }

    @Override
    public void updateHospitalStats(Long hospitalId) {
        Hospital hospital = hospitalRepository.findById(hospitalId)
            .orElseThrow(() -> new BusinessException("Hospital not found"));
        // Update statistics (can be implemented based on requirements)
        hospitalRepository.save(hospital);
    }
}
