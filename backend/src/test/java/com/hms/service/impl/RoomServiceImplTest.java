package com.hms.service.impl;

import com.hms.entity.Room;
import com.hms.enums.RoomType;
import com.hms.exception.ResourceNotFoundException;
import com.hms.repository.RoomRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoomServiceImplTest {

    @Mock private RoomRepository repo;
    @InjectMocks private RoomServiceImpl service;

    private Room makeRoom(Long id) {
        return new Room(id, "101", RoomType.SINGLE, 3000.0, 1L, false);
    }

    @Test
    @DisplayName("create: saves and returns room")
    void create() {
        Room r = makeRoom(null);
        when(repo.save(r)).thenReturn(makeRoom(1L));
        assertEquals(1L, service.create(r).getId());
    }

    @Test
    @DisplayName("getAll: returns all rooms")
    void getAll() {
        when(repo.findAll()).thenReturn(List.of(makeRoom(1L), makeRoom(2L)));
        assertEquals(2, service.getAll().size());
    }

    @Test
    @DisplayName("getById: found returns room")
    void getById_found() {
        when(repo.findById(1L)).thenReturn(Optional.of(makeRoom(1L)));
        assertEquals("101", service.getById(1L).getRoomNumber());
    }

    @Test
    @DisplayName("getById: missing throws ResourceNotFoundException")
    void getById_notFound() {
        when(repo.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.getById(99L));
    }

    @Test
    @DisplayName("update: updates all room fields")
    void update() {
        Room existing = makeRoom(1L);
        Room updated = new Room(1L, "102", RoomType.TWIN_SHARING, 2000.0, 2L, true);
        when(repo.findById(1L)).thenReturn(Optional.of(existing));
        when(repo.save(any())).thenReturn(existing);

        service.update(1L, updated);
        verify(repo).save(argThat(r ->
                "102".equals(r.getRoomNumber()) && r.getRoomType() == RoomType.TWIN_SHARING && r.getRent() == 2000.0));
    }

    @Test
    @DisplayName("delete: delegates to repository")
    void delete() {
        service.delete(1L);
        verify(repo).deleteById(1L);
    }

    @Test
    @DisplayName("getByHallId: returns rooms for hall")
    void getByHallId() {
        when(repo.findByHallId(1L)).thenReturn(List.of(makeRoom(1L)));
        assertEquals(1, service.getByHallId(1L).size());
    }
}
