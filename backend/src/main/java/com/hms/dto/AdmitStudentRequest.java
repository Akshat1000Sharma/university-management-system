package com.hms.dto;

import lombok.*;

import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class AdmitStudentRequest {
    private String name;
    private String address;
    private String phone;
    private String photo;
    private String email;
    private String registrationNumber;
    private LocalDate admissionDate;
    private Long hallId;
    /** When null, the first vacant room in {@link #hallId} is assigned. */
    private Long roomId;
}
