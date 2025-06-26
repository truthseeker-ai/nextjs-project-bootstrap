package com.healthcare.service;

import com.healthcare.dto.AuthRequest;
import com.healthcare.dto.AuthResponse;
import com.healthcare.dto.PatientDTO;
import com.healthcare.model.Patient;
import com.healthcare.repository.PatientRepository;
import com.healthcare.security.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for PatientService
 */
@ExtendWith(MockitoExtension.class)
public class PatientServiceTest {

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @InjectMocks
    private PatientService patientService;

    private Patient testPatient;
    private PatientDTO testPatientDTO;
    private AuthRequest testAuthRequest;

    @BeforeEach
    void setUp() {
        testPatient = new Patient();
        testPatient.setId(1L);
        testPatient.setFirstName("John");
        testPatient.setLastName("Doe");
        testPatient.setEmail("john.doe@example.com");
        testPatient.setPassword("encoded_password");
        testPatient.setPhoneNumber("1234567890");
        testPatient.setDateOfBirth(LocalDateTime.now().minusYears(30));

        testPatientDTO = new PatientDTO();
        testPatientDTO.setFirstName("John");
        testPatientDTO.setLastName("Doe");
        testPatientDTO.setEmail("john.doe@example.com");
        testPatientDTO.setPassword("password123");
        testPatientDTO.setPhoneNumber("1234567890");
        testPatientDTO.setDateOfBirth(LocalDateTime.now().minusYears(30));

        testAuthRequest = new AuthRequest();
        testAuthRequest.setEmail("john.doe@example.com");
        testAuthRequest.setPassword("password123");
    }

    @Test
    void authenticate_ValidCredentials_ReturnsAuthResponse() {
        // Arrange
        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
            .thenReturn(authentication);
        when(jwtTokenProvider.generateToken(authentication)).thenReturn("test_token");
        when(patientRepository.findByEmail(testAuthRequest.getEmail()))
            .thenReturn(Optional.of(testPatient));

        // Act
        AuthResponse response = patientService.authenticate(testAuthRequest);

        // Assert
        assertNotNull(response);
        assertEquals("test_token", response.getToken());
        assertEquals(testPatient.getEmail(), response.getEmail());
        assertEquals(testPatient.getFirstName(), response.getFirstName());
    }

    @Test
    void register_NewPatient_ReturnsPatientDTO() {
        // Arrange
        when(patientRepository.existsByEmail(testPatientDTO.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(testPatientDTO.getPassword())).thenReturn("encoded_password");
        when(patientRepository.save(any(Patient.class))).thenReturn(testPatient);

        // Act
        PatientDTO result = patientService.register(testPatientDTO);

        // Assert
        assertNotNull(result);
        assertEquals(testPatientDTO.getEmail(), result.getEmail());
        assertEquals(testPatientDTO.getFirstName(), result.getFirstName());
        verify(patientRepository).save(any(Patient.class));
    }

    @Test
    void getPatientById_ExistingId_ReturnsPatientDTO() {
        // Arrange
        when(patientRepository.findById(1L)).thenReturn(Optional.of(testPatient));

        // Act
        PatientDTO result = patientService.getPatientById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(testPatient.getEmail(), result.getEmail());
    }

    @Test
    void getPatientById_NonExistingId_ThrowsException() {
        // Arrange
        when(patientRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> patientService.getPatientById(999L));
    }

    @Test
    void updatePatient_ExistingPatient_ReturnsUpdatedPatientDTO() {
        // Arrange
        when(patientRepository.findById(1L)).thenReturn(Optional.of(testPatient));
        when(patientRepository.save(any(Patient.class))).thenReturn(testPatient);

        testPatientDTO.setFirstName("Updated");
        testPatientDTO.setLastName("Name");

        // Act
        PatientDTO result = patientService.updatePatient(1L, testPatientDTO);

        // Assert
        assertNotNull(result);
        assertEquals("Updated", result.getFirstName());
        assertEquals("Name", result.getLastName());
    }

    @Test
    void getAllPatients_ReturnsListOfPatientDTOs() {
        // Arrange
        Patient patient2 = new Patient();
        patient2.setId(2L);
        patient2.setFirstName("Jane");
        patient2.setLastName("Doe");
        patient2.setEmail("jane.doe@example.com");

        when(patientRepository.findAll()).thenReturn(Arrays.asList(testPatient, patient2));

        // Act
        List<PatientDTO> results = patientService.getAllPatients();

        // Assert
        assertNotNull(results);
        assertEquals(2, results.size());
        assertEquals("John", results.get(0).getFirstName());
        assertEquals("Jane", results.get(1).getFirstName());
    }

    @Test
    void deletePatient_ExistingId_DeletesPatient() {
        // Arrange
        when(patientRepository.existsById(1L)).thenReturn(true);

        // Act
        patientService.deletePatient(1L);

        // Assert
        verify(patientRepository).deleteById(1L);
    }

    @Test
    void deletePatient_NonExistingId_ThrowsException() {
        // Arrange
        when(patientRepository.existsById(999L)).thenReturn(false);

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> patientService.deletePatient(999L));
    }
}
