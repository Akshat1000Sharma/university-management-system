package com.hms.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class AdmitStudentRequest {
    private String name;
    private String address;
    private String phone;
    private String photo;
    private Long hallId;
    private Long roomId;
}
