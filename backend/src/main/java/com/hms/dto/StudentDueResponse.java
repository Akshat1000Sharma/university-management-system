package com.hms.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class StudentDueResponse {
    private Long studentId;
    private String studentName;
    private double messCharges;
    private double roomRent;
    private double amenityCharge;
    private double totalDue;
}
