package com.hms.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hms.dto.LoginRequest;
import com.hms.dto.LoginResponse;
import com.hms.entity.User;
import com.hms.enums.UserRole;
import com.hms.exception.GlobalExceptionHandler;
import com.hms.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock private AuthService authService;
    @InjectMocks private AuthController controller;

    private MockMvc mockMvc;
    private final ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    @DisplayName("POST /api/auth/login: valid credentials returns 200 with token")
    void login_validCredentials() throws Exception {
        LoginResponse response = LoginResponse.builder()
                .token("jwt-token")
                .role("STUDENT")
                .name("Test Student")
                .email("student@hms.edu")
                .hallId(1L)
                .build();
        when(authService.login(any(LoginRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(new LoginRequest("student@hms.edu", "student123"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("jwt-token"))
                .andExpect(jsonPath("$.role").value("STUDENT"))
                .andExpect(jsonPath("$.name").value("Test Student"))
                .andExpect(jsonPath("$.email").value("student@hms.edu"));
    }

    @Test
    @DisplayName("POST /api/auth/login: invalid credentials returns 401")
    void login_invalidCredentials() throws Exception {
        when(authService.login(any())).thenThrow(new RuntimeException("Invalid email or password"));

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(new LoginRequest("bad@hms.edu", "wrong"))))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("GET /api/auth/me: authenticated user returns profile")
    void getCurrentUser_authenticated() throws Exception {
        User user = new User();
        user.setEmail("student@hms.edu");
        user.setRole(UserRole.STUDENT);

        LoginResponse response = LoginResponse.builder()
                .role("STUDENT").name("Test").email("student@hms.edu").build();
        when(authService.getCurrentUser("student@hms.edu")).thenReturn(response);

        mockMvc.perform(get("/api/auth/me")
                        .principal(new UsernamePasswordAuthenticationToken(user, null)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("student@hms.edu"));
    }

    @Test
    @DisplayName("GET /api/auth/me: no authentication returns 401")
    void getCurrentUser_noAuth() throws Exception {
        mockMvc.perform(get("/api/auth/me"))
                .andExpect(status().isUnauthorized());
    }
}
