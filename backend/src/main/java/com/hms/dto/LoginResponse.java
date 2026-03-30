package com.hms.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class LoginResponse {
    private String token;
    private String role;
    private String name;
    private String email;
    private Long hallId;
    private String hallName;
    private Long studentId;
    private String registrationNumber;
}
