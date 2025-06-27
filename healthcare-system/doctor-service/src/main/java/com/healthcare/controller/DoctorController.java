package com.healthcare.controller;

import com.healthcare.dto.DoctorDTO;
import com.healthcare.dto.ProfileUpdateRequest;
import com.healthcare.service.DoctorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/doctors")
@Tag(name = "Doctor Management", description = "APIs for managing doctor information and profiles")
@SecurityRequirement(name = "Bearer Authentication")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    @GetMapping
    @Operation(
        summary = "Get all doctors",
        description = "Retrieve a list of all registered doctors",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "List of doctors retrieved successfully",
                content = @Content(schema = @Schema(implementation = DoctorDTO.class))
            )
        }
    )
    public ResponseEntity<List<DoctorDTO>> getAllDoctors() {
        return ResponseEntity.ok(doctorService.getAllDoctors());
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "Get doctor by ID",
        description = "Retrieve a specific doctor's information by their ID",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Doctor found",
                content = @Content(schema = @Schema(implementation = DoctorDTO.class))
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Doctor not found"
            )
        }
    )
    public ResponseEntity<DoctorDTO> getDoctorById(
        @Parameter(description = "ID of the doctor", required = true)
        @PathVariable Long id
    ) {
        return ResponseEntity.ok(doctorService.getDoctorById(id));
    }

    @GetMapping("/{id}/profile")
    @Operation(
        summary = "Get doctor's profile",
        description = "Retrieve detailed profile information for a specific doctor"
    )
    public ResponseEntity<DoctorDTO> getDoctorProfile(
        @Parameter(description = "ID of the doctor", required = true)
        @PathVariable Long id
    ) {
        return ResponseEntity.ok(doctorService.getDoctorProfile(id));
    }

    @PutMapping("/{id}/profile")
    @PreAuthorize("hasRole('DOCTOR') and @securityService.isCurrentUser(#id)")
    @Operation(
        summary = "Update doctor's profile",
        description = "Update profile information for the authenticated doctor"
    )
    public ResponseEntity<DoctorDTO> updateProfile(
        @Parameter(description = "ID of the doctor", required = true)
        @PathVariable Long id,
        @Valid @RequestBody ProfileUpdateRequest request
    ) {
        return ResponseEntity.ok(doctorService.updateProfile(id, request));
    }

    @PostMapping("/{id}/profile-image")
    @PreAuthorize("hasRole('DOCTOR') and @securityService.isCurrentUser(#id)")
    @Operation(
        summary = "Upload profile image",
        description = "Upload or update the doctor's profile image"
    )
    public ResponseEntity<DoctorDTO> uploadProfileImage(
        @Parameter(description = "ID of the doctor", required = true)
        @PathVariable Long id,
        @Parameter(description = "Profile image file", required = true)
        @RequestParam("file") MultipartFile file
    ) {
        return ResponseEntity.ok(doctorService.updateProfileImage(id, file));
    }

    @GetMapping("/{id}/stats")
    @PreAuthorize("hasRole('DOCTOR') and @securityService.isCurrentUser(#id)")
    @Operation(
        summary = "Get doctor's statistics",
        description = "Retrieve statistics about appointments and patients for the doctor"
    )
    public ResponseEntity<Map<String, Object>> getDoctorStats(
        @Parameter(description = "ID of the doctor", required = true)
        @PathVariable Long id
    ) {
        return ResponseEntity.ok(doctorService.getDoctorStats(id));
    }

    @GetMapping("/specializations")
    @Operation(
        summary = "Get all specializations",
        description = "Retrieve a list of all available doctor specializations"
    )
    public ResponseEntity<List<String>> getAllSpecializations() {
        return ResponseEntity.ok(doctorService.getAllSpecializations());
    }

    @GetMapping("/search")
    @Operation(
        summary = "Search doctors",
        description = "Search for doctors based on various criteria"
    )
    public ResponseEntity<List<DoctorDTO>> searchDoctors(
        @Parameter(description = "Specialization to filter by")
        @RequestParam(required = false) String specialization,
        @Parameter(description = "Name to search for")
        @RequestParam(required = false) String name,
        @Parameter(description = "Minimum rating to filter by")
        @RequestParam(required = false) Double rating
    ) {
        return ResponseEntity.ok(doctorService.searchDoctors(specialization, name, rating));
    }
}
