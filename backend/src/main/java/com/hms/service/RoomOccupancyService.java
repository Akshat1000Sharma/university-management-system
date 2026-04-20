package com.hms.service;

import com.hms.entity.Room;
import com.hms.enums.RoomType;
import com.hms.repository.RoomRepository;
import com.hms.repository.StudentRepository;
import org.springframework.stereotype.Service;

/**
 * Single rooms hold one student; twin-sharing rooms hold up to two. Persisted {@link Room#isOccupied}
 * means "at full capacity" (no vacant beds).
 */
@Service
public class RoomOccupancyService {

    private final StudentRepository studentRepository;
    private final RoomRepository roomRepository;

    public RoomOccupancyService(StudentRepository studentRepository, RoomRepository roomRepository) {
        this.studentRepository = studentRepository;
        this.roomRepository = roomRepository;
    }

    public long countOccupants(Long roomId) {
        if (roomId == null) {
            return 0;
        }
        return studentRepository.countByRoomId(roomId);
    }

    public int bedCapacity(Room room) {
        return room.getRoomType() == RoomType.TWIN_SHARING ? 2 : 1;
    }

    public boolean hasVacancy(Room room) {
        return countOccupants(room.getId()) < bedCapacity(room);
    }

    /** Sync DB flag: full when occupant count &gt;= bed capacity. */
    public void syncOccupiedFlag(Long roomId) {
        if (roomId == null) {
            return;
        }
        roomRepository.findById(roomId).ifPresent(room -> {
            long c = countOccupants(roomId);
            room.setOccupied(c >= bedCapacity(room));
            roomRepository.save(room);
        });
    }

    /** For API payloads: current occupant count (not persisted on {@link Room}). */
    public void enrich(Room room) {
        if (room.getId() == null) {
            return;
        }
        room.setOccupantCount((int) countOccupants(room.getId()));
    }
}
