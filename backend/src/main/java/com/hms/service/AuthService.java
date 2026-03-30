package com.hms.service;

import com.hms.dto.LoginRequest;
import com.hms.dto.LoginResponse;

public interface AuthService {
    LoginResponse login(LoginRequest request);
    LoginResponse getCurrentUser(String email);
}
