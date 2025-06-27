package com.healthcare.service;

import com.healthcare.dto.AdminDTO;
import com.healthcare.dto.AdminAuthRequest;
import com.healthcare.dto.AdminAuthResponse;
import com.healthcare.dto.AdminProfileUpdateRequest;
import java.util.List;

public interface AdminService {
    AdminAuthResponse authenticate(AdminAuthRequest request);
    AdminDTO createAdmin(AdminDTO adminDTO);
    AdminDTO updateAdmin(Long id, AdminProfileUpdateRequest request);
    AdminDTO getAdminById(Long id);
    AdminDTO getAdminByUsername(String username);
    List<AdminDTO> getAllAdmins();
    List<AdminDTO> getAdminsByHospital(Long hospitalId);
    List<AdminDTO> getActiveAdmins();
    void deleteAdmin(Long id);
    void updatePassword(Long id, String oldPassword, String newPassword);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    void updateLastLogin(String username);
    List<AdminDTO> getAdminsByRole(String role);
    List<AdminDTO> getAdminsByDepartment(String department);
    void revokeToken(String username);
}
