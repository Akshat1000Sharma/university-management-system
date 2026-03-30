package com.hms.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class OccupancyResponse {
    private Long hallId;
    private String hallName;
    private int totalRooms;
    private int occupiedRooms;
    private int vacantRooms;
    private double occupancyPercent;
}
