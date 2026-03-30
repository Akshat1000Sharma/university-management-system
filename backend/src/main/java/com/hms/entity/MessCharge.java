package com.hms.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "mess_charges")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class MessCharge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long studentId;
    private Long hallId;
    private int month;
    private int year;
    private double amount;
}
