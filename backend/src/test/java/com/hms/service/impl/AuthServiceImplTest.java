package com.hms.service.impl;

import com.hms.config.JwtUtil;
import com.hms.dto.LoginRequest;
import com.hms.dto.LoginResponse;
import com.hms.entity.User;
import com.hms.enums.UserRole;
import com.hms.exception.ResourceNotFoundException;
import com.hms.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock private UserRepository userRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private JwtUtil jwtUtil;
    @InjectMocks private AuthServiceImpl authService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setEmail("student@hms.edu");
        testUser.setPassword("encoded-password");
        testUser.setName("Test Student");
        testUser.setRole(UserRole.STUDENT);
        testUser.setHallId(1L);
        testUser.setHallName("Tagore Hall");
        testUser.setStudentId(10L);
        testUser.setRegistrationNumber("REG001");
    }

    @Test
    @DisplayName("login: valid credentials should return LoginResponse with token")
    void login_validCredentials_returnsLoginResponse() {
        LoginRequest request = new LoginRequest("student@hms.edu", "student123");
        when(userRepository.findByEmail("student@hms.edu")).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches("student123", "encoded-password")).thenReturn(true);
        when(jwtUtil.generateToken("student@hms.edu", "STUDENT")).thenReturn("jwt-token-123");

        LoginResponse response = authService.login(request);

        assertNotNull(response);
        assertEquals("jwt-token-123", response.getToken());
        assertEquals("STUDENT", response.getRole());
        assertEquals("Test Student", response.getName());
        assertEquals("student@hms.edu", response.getEmail());
        assertEquals(1L, response.getHallId());
        assertEquals("Tagore Hall", response.getHallName());
        assertEquals(10L, response.getStudentId());
        assertEquals("REG001", response.getRegistrationNumber());
    }

    @Test
    @DisplayName("login: non-existent email should throw RuntimeException")
    void login_invalidEmail_throwsException() {
        LoginRequest request = new LoginRequest("unknown@hms.edu", "pass");
        when(userRepository.findByEmail("unknown@hms.edu")).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> authService.login(request));
        assertEquals("Invalid email or password", ex.getMessage());
    }

    @Test
    @DisplayName("login: wrong password should throw RuntimeException")
    void login_wrongPassword_throwsException() {
        LoginRequest request = new LoginRequest("student@hms.edu", "wrongpass");
        when(userRepository.findByEmail("student@hms.edu")).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches("wrongpass", "encoded-password")).thenReturn(false);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> authService.login(request));
        assertEquals("Invalid email or password", ex.getMessage());
    }

    @Test
    @DisplayName("login: email should be lowercased before lookup")
    void login_emailIsLowercased() {
        LoginRequest request = new LoginRequest("Student@HMS.EDU", "student123");
        when(userRepository.findByEmail("student@hms.edu")).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches("student123", "encoded-password")).thenReturn(true);
        when(jwtUtil.generateToken("student@hms.edu", "STUDENT")).thenReturn("token");

        LoginResponse response = authService.login(request);
        assertNotNull(response);
        verify(userRepository).findByEmail("student@hms.edu");
    }

    @Test
    @DisplayName("getCurrentUser: valid email returns user profile without token")
    void getCurrentUser_validEmail_returnsProfile() {
        when(userRepository.findByEmail("student@hms.edu")).thenReturn(Optional.of(testUser));

        LoginResponse response = authService.getCurrentUser("student@hms.edu");

        assertNotNull(response);
        assertNull(response.getToken());
        assertEquals("STUDENT", response.getRole());
        assertEquals("Test Student", response.getName());
        assertEquals("student@hms.edu", response.getEmail());
    }

    @Test
    @DisplayName("getCurrentUser: unknown email throws ResourceNotFoundException")
    void getCurrentUser_unknownEmail_throwsException() {
        when(userRepository.findByEmail("unknown@hms.edu")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> authService.getCurrentUser("unknown@hms.edu"));
    }
}
