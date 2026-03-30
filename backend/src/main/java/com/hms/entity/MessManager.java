package com.hms.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "mess_managers")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class MessManager {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private Long hallId;
}
