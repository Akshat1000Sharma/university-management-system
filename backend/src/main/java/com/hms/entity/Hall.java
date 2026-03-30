package com.hms.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "halls")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Hall {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private boolean isNew;
    private double amenityCharge;
}
