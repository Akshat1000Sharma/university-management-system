package com.hms.service.impl;

import com.hms.entity.Room;
import com.hms.exception.ResourceNotFoundException;
import com.hms.repository.RoomRepository;
import com.hms.service.RoomService;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class RoomServiceImpl implements RoomService {

    private final RoomRepository repo;

    public RoomServiceImpl(RoomRepository repo) {
        this.repo = repo;
    }

    public Room create(Room room) { return repo.save(room); }
    public List<Room> getAll() { return repo.findAll(); }
    public Room getById(Long id) {
        return repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Room not found"));
    }
    public Room update(Long id, Room room) {
        Room existing = getById(id);
        existing.setRoomNumber(room.getRoomNumber());
        existing.setRoomType(room.getRoomType());
        existing.setRent(room.getRent());
        existing.setHallId(room.getHallId());
        existing.setOccupied(room.isOccupied());
        return repo.save(existing);
    }
    public void delete(Long id) { repo.deleteById(id); }
    public List<Room> getByHallId(Long hallId) { return repo.findByHallId(hallId); }
}
