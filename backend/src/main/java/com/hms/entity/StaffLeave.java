package com.hms.entity;

import com.hms.enums.LeaveType;
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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 32)
    private LeaveType leaveType = LeaveType.CASUAL;

    @PrePersist
    @PreUpdate
    void ensureLeaveType() {
        if (leaveType == null) {
            leaveType = LeaveType.CASUAL;
        }
    }
}
