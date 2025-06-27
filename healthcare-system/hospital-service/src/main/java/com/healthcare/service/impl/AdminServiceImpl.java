package com.healthcare.service.impl;

import com.healthcare.dto.AdminDTO;
import com.healthcare.dto.AdminAuthRequest;
import com.healthcare.dto.AdminAuthResponse;
import com.healthcare.dto.AdminProfileUpdateRequest;
import com.healthcare.model.Admin;
import com.healthcare.model.Hospital;
import com.healthcare.repository.AdminRepository;
import com.healthcare.repository.HospitalRepository;
import com.healthcare.service.AdminService;
import com.healthcare.exception.BusinessException;
import com.healthcare.security.JwtTokenProvider;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private HospitalRepository hospitalRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Override
    public AdminAuthResponse authenticate(AdminAuthRequest request) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        Admin admin = adminRepository.findByUsername(request.getUsername())
            .orElseThrow(() -> new BusinessException("Admin not found"));

        String token = jwtTokenProvider.generateToken(authentication);
        updateLastLogin(admin.getUsername());

        AdminAuthResponse response = modelMapper.map(admin, AdminAuthResponse.class);
        response.setToken(token);
        return response;
    }

    @Override
    public AdminDTO createAdmin(AdminDTO adminDTO) {
        if (adminRepository.existsByUsername(adminDTO.getUsername())) {
            throw new BusinessException("Username already exists");
        }
        if (adminRepository.existsByEmail(adminDTO.getEmail())) {
            throw new BusinessException("Email already exists");
        }

        Admin admin = modelMapper.map(adminDTO, Admin.class);
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        
        if (adminDTO.getHospitalId() != null) {
            Hospital hospital = hospitalRepository.findById(adminDTO.getHospitalId())
                .orElseThrow(() -> new BusinessException("Hospital not found"));
            admin.setHospital(hospital);
        }

        admin = adminRepository.save(admin);
        return modelMapper.map(admin, AdminDTO.class);
    }

    @Override
    public AdminDTO updateAdmin(Long id, AdminProfileUpdateRequest request) {
        Admin admin = adminRepository.findById(id)
            .orElseThrow(() -> new BusinessException("Admin not found"));

        if (!admin.getEmail().equals(request.getEmail()) && adminRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException("Email already exists");
        }

        modelMapper.map(request, admin);
        
        if (request.getHospitalId() != null) {
            Hospital hospital = hospitalRepository.findById(request.getHospitalId())
                .orElseThrow(() -> new BusinessException("Hospital not found"));
            admin.setHospital(hospital);
        }

        admin = adminRepository.save(admin);
        return modelMapper.map(admin, AdminDTO.class);
    }

    @Override
    public AdminDTO getAdminById(Long id) {
        Admin admin = adminRepository.findById(id)
            .orElseThrow(() -> new BusinessException("Admin not found"));
        return modelMapper.map(admin, AdminDTO.class);
    }

    @Override
    public AdminDTO getAdminByUsername(String username) {
        Admin admin = adminRepository.findByUsername(username)
            .orElseThrow(() -> new BusinessException("Admin not found"));
        return modelMapper.map(admin, AdminDTO.class);
    }

    @Override
    public List<AdminDTO> getAllAdmins() {
        return adminRepository.findAll().stream()
            .map(admin -> modelMapper.map(admin, AdminDTO.class))
            .collect(Collectors.toList());
    }

    @Override
    public List<AdminDTO> getAdminsByHospital(Long hospitalId) {
        return adminRepository.findByHospitalId(hospitalId).stream()
            .map(admin -> modelMapper.map(admin, AdminDTO.class))
            .collect(Collectors.toList());
    }

    @Override
    public List<AdminDTO> getActiveAdmins() {
        return adminRepository.findByIsActiveTrue().stream()
            .map(admin -> modelMapper.map(admin, AdminDTO.class))
            .collect(Collectors.toList());
    }

    @Override
    public void deleteAdmin(Long id) {
        Admin admin = adminRepository.findById(id)
            .orElseThrow(() -> new BusinessException("Admin not found"));
        admin.setIsActive(false);
        adminRepository.save(admin);
    }

    @Override
    public void updatePassword(Long id, String oldPassword, String newPassword) {
        Admin admin = adminRepository.findById(id)
            .orElseThrow(() -> new BusinessException("Admin not found"));

        if (!passwordEncoder.matches(oldPassword, admin.getPassword())) {
            throw new BusinessException("Invalid old password");
        }

        admin.setPassword(passwordEncoder.encode(newPassword));
        adminRepository.save(admin);
    }

    @Override
    public boolean existsByUsername(String username) {
        return adminRepository.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return adminRepository.existsByEmail(email);
    }

    @Override
    public void updateLastLogin(String username) {
        Admin admin = adminRepository.findByUsername(username)
            .orElseThrow(() -> new BusinessException("Admin not found"));
        admin.setLastLogin(LocalDateTime.now());
        adminRepository.save(admin);
    }

    @Override
    public List<AdminDTO> getAdminsByRole(String role) {
        return adminRepository.findByRole(role).stream()
            .map(admin -> modelMapper.map(admin, AdminDTO.class))
            .collect(Collectors.toList());
    }

    @Override
    public List<AdminDTO> getAdminsByDepartment(String department) {
        return adminRepository.findByDepartment(department).stream()
            .map(admin -> modelMapper.map(admin, AdminDTO.class))
            .collect(Collectors.toList());
    }

    @Override
    public void revokeToken(String username) {
        // Implementation for token revocation can be added here
        // This might involve adding the token to a blacklist or updating the user's tokenVersion
    }
}
