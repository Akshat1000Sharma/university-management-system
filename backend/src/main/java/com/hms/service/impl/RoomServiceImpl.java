package com.hms.service.impl;

import com.hms.entity.Room;
import com.hms.exception.ResourceNotFoundException;
import com.hms.repository.RoomRepository;
import com.hms.service.RoomOccupancyService;
import com.hms.service.RoomService;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class RoomServiceImpl implements RoomService {

    private final RoomRepository repo;
    private final RoomOccupancyService occupancyService;

    public RoomServiceImpl(RoomRepository repo, RoomOccupancyService occupancyService) {
        this.repo = repo;
        this.occupancyService = occupancyService;
    }

    public Room create(Room room) {
        Room saved = repo.save(room);
        return afterWrite(saved);
    }
    public List<Room> getAll() {
        List<Room> list = repo.findAll();
        list.forEach(occupancyService::enrich);
        return list;
    }
    public Room getById(Long id) {
        Room r = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Room not found"));
        occupancyService.enrich(r);
        return r;
    }
    public Room update(Long id, Room room) {
        Room existing = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Room not found"));
        existing.setRoomNumber(room.getRoomNumber());
        existing.setRoomType(room.getRoomType());
        existing.setRent(room.getRent());
        existing.setHallId(room.getHallId());
        Room saved = repo.save(existing);
        occupancyService.syncOccupiedFlag(saved.getId());
        return afterWrite(saved);
    }
    public void delete(Long id) { repo.deleteById(id); }
    public List<Room> getByHallId(Long hallId) {
        List<Room> list = repo.findByHallId(hallId);
        list.forEach(occupancyService::enrich);
        return list;
    }

    private Room afterWrite(Room saved) {
        if (saved.getId() != null) {
            occupancyService.syncOccupiedFlag(saved.getId());
            occupancyService.enrich(saved);
        }
        return saved;
    }
}
