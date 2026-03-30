package com.hms.service;

import com.hms.entity.Room;
import java.util.List;

public interface RoomService {
    Room create(Room room);
    List<Room> getAll();
    Room getById(Long id);
    Room update(Long id, Room room);
    void delete(Long id);
    List<Room> getByHallId(Long hallId);
}
