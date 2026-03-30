package com.hms.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class MessManagerPaymentResponse {
    private Long hallId;
    private String hallName;
    private Long messManagerId;
    private String messManagerName;
    private int month;
    private int year;
    private double totalAmount;
}
