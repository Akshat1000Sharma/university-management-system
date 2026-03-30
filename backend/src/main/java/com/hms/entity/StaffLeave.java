package com.hms.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "staff_leaves")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class StaffLeave {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long staffId;
    private LocalDate leaveDate;
}
