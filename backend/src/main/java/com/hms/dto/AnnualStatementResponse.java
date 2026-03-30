package com.hms.dto;

import lombok.*;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class AnnualStatementResponse {
    private Long hallId;
    private String hallName;
    private int year;
    private double grantAllocated;
    private double totalSalariesPaid;
    private double totalExpenditures;
    private double totalBalance;
    private List<StaffSalaryResponse> salaryDetails;
}
