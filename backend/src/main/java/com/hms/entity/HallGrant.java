package com.hms.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "hall_grants")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class HallGrant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long grantId;
    private Long hallId;
    private double allocatedAmount;
    private double spentAmount;
}
