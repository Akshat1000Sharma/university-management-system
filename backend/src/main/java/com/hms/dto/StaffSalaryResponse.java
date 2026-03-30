package com.hms.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class StaffSalaryResponse {
    private Long staffId;
    private String staffName;
    private String staffType;
    private double dailyPay;
    private int totalDays;
    private int leaveDays;
    private int workingDays;
    private double salary;
}
