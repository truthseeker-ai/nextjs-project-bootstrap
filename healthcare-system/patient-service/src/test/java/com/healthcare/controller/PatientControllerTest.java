package com.healthcare.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.healthcare.dto.AuthRequest;
import com.healthcare.dto.AuthResponse;
import com.healthcare.dto.PatientDTO;
import com.healthcare.service.PatientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Unit tests for PatientController
 */
@ExtendWith(MockitoExtension.class)
public class PatientControllerTest {

    private MockMvc mockMvc;

    @Mock
    private PatientService patientService;

    @InjectMocks
    private PatientController patientController;

    private ObjectMapper objectMapper;
    private PatientDTO testPatientDTO;
    private AuthRequest testAuthRequest;
    private AuthResponse testAuthResponse;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(patientController)
                .build();
        objectMapper = new ObjectMapper();

        // Initialize test data
        testPatientDTO = new PatientDTO();
        testPatientDTO.setId(1L);
        testPatientDTO.setFirstName("John");
        testPatientDTO.setLastName("Doe");
        testPatientDTO.setEmail("john.doe@example.com");
        testPatientDTO.setPassword("password123");
        testPatientDTO.setPhoneNumber("1234567890");
        testPatientDTO.setDateOfBirth(LocalDateTime.now().minusYears(30));

        testAuthRequest = new AuthRequest();
        testAuthRequest.setEmail("john.doe@example.com");
        testAuthRequest.setPassword("password123");

        testAuthResponse = AuthResponse.builder()
                .token("test_token")
                .email("john.doe@example.com")
                .firstName("John")
                .lastName("Doe")
                .message("Successfully authenticated")
                .build();
    }

    @Test
    void login_ValidCredentials_ReturnsAuthResponse() throws Exception {
        // Arrange
        when(patientService.authenticate(any(AuthRequest.class))).thenReturn(testAuthResponse);

        // Act & Assert
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testAuthRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("test_token"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"));
    }

    @Test
    void register_ValidPatient_ReturnsCreatedPatient() throws Exception {
        // Arrange
        when(patientService.register(any(PatientDTO.class))).thenReturn(testPatientDTO);

        // Act & Assert
        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testPatientDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("john.doe@example.com"))
                .andExpect(jsonPath("$.firstName").value("John"));
    }

    @Test
    void getPatient_ExistingId_ReturnsPatient() throws Exception {
        // Arrange
        when(patientService.getPatientById(1L)).thenReturn(testPatientDTO);

        // Act & Assert
        mockMvc.perform(get("/api/patients/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"));
    }

    @Test
    void updatePatient_ValidUpdate_ReturnsUpdatedPatient() throws Exception {
        // Arrange
        PatientDTO updatedDTO = testPatientDTO;
        updatedDTO.setFirstName("Updated");
        when(patientService.updatePatient(eq(1L), any(PatientDTO.class))).thenReturn(updatedDTO);

        // Act & Assert
        mockMvc.perform(put("/api/patients/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Updated"));
    }

    @Test
    void getAllPatients_ReturnsListOfPatients() throws Exception {
        // Arrange
        PatientDTO patient2 = new PatientDTO();
        patient2.setId(2L);
        patient2.setFirstName("Jane");
        patient2.setEmail("jane.doe@example.com");

        List<PatientDTO> patients = Arrays.asList(testPatientDTO, patient2);
        when(patientService.getAllPatients()).thenReturn(patients);

        // Act & Assert
        mockMvc.perform(get("/api/patients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("John"))
                .andExpect(jsonPath("$[1].firstName").value("Jane"))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void deletePatient_ExistingId_ReturnsNoContent() throws Exception {
        // Act & Assert
        mockMvc.perform(delete("/api/patients/1"))
                .andExpect(status().isNoContent());
    }
}
