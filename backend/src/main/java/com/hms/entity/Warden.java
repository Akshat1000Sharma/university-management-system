package com.hms.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "wardens")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Warden {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String contact;
    private Long hallId;
    private boolean isControlling;
}
