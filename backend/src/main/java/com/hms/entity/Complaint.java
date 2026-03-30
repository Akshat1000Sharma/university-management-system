package com.hms.entity;

import com.hms.enums.ComplaintStatus;
import com.hms.enums.ComplaintType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "complaints")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Complaint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long studentId;
    private Long hallId;

    @Enumerated(EnumType.STRING)
    private ComplaintType type;

    private String description;

    @Enumerated(EnumType.STRING)
    private ComplaintStatus status;

    private String atr;
}
