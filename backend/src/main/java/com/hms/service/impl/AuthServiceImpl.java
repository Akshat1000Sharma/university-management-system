package com.hms.service.impl;

import com.hms.config.JwtUtil;
import com.hms.dto.LoginRequest;
import com.hms.dto.LoginResponse;
import com.hms.entity.User;
import com.hms.exception.ResourceNotFoundException;
import com.hms.repository.UserRepository;
import com.hms.service.AuthService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail().toLowerCase())
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name());

        return LoginResponse.builder()
                .token(token)
                .role(user.getRole().name())
                .name(user.getName())
                .email(user.getEmail())
                .hallId(user.getHallId())
                .hallName(user.getHallName())
                .studentId(user.getStudentId())
                .registrationNumber(user.getRegistrationNumber())
                .build();
    }

    @Override
    public LoginResponse getCurrentUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return LoginResponse.builder()
                .role(user.getRole().name())
                .name(user.getName())
                .email(user.getEmail())
                .hallId(user.getHallId())
                .hallName(user.getHallName())
                .studentId(user.getStudentId())
                .registrationNumber(user.getRegistrationNumber())
                .build();
    }
}
