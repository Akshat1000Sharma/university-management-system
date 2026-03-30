package com.hms.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "grants")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Grant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int year;
    private double totalAmount;
}
