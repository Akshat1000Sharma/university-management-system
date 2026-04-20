package com.hms.entity;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.hms.enums.ExpenseCategory;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "expenditures")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Expenditure {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long hallId;
    private String description;
    private double amount;
    private LocalDate date;

    @JsonAlias("expenseCategory")
    @Enumerated(EnumType.STRING)
    private ExpenseCategory category;
}
