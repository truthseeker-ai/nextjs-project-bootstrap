package com.healthcare.service;

import com.healthcare.dto.HospitalDTO;
import java.util.List;

public interface HospitalService {
    HospitalDTO createHospital(HospitalDTO hospitalDTO);
    HospitalDTO updateHospital(Long id, HospitalDTO hospitalDTO);
    HospitalDTO getHospitalById(Long id);
    List<HospitalDTO> getAllHospitals();
    List<HospitalDTO> getActiveHospitals();
    void deleteHospital(Long id);
    HospitalDTO getHospitalByName(String name);
    List<HospitalDTO> getHospitalsBySpecialization(String specialization);
    boolean existsByLicenseNumber(String licenseNumber);
    void updateHospitalStats(Long hospitalId);
}
