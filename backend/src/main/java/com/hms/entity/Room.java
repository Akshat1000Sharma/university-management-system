package com.hms.entity;

import com.hms.enums.RoomType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "rooms")
@Getter @Setter @NoArgsConstructor
@AllArgsConstructor
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String roomNumber;

    @Enumerated(EnumType.STRING)
    private RoomType roomType;

    private double rent;
    private Long hallId;
    /** True when occupant count &gt;= bed capacity (single: 1, twin: 2). */
    private boolean isOccupied;

    /** Populated for API responses only; not stored in DB. */
    @Transient
    private Integer occupantCount;

    public int getBedCapacity() {
        return roomType == RoomType.TWIN_SHARING ? 2 : 1;
    }
}
