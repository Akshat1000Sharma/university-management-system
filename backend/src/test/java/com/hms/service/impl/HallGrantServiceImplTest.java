package com.hms.service.impl;

import com.hms.entity.HallGrant;
import com.hms.exception.ResourceNotFoundException;
import com.hms.repository.HallGrantRepository;
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
class HallGrantServiceImplTest {

    @Mock private HallGrantRepository repo;
    @InjectMocks private HallGrantServiceImpl service;

    private HallGrant make(Long id) {
        return new HallGrant(id, 1L, 1L, 50000.0, 10000.0);
    }

    @Test @DisplayName("create: saves and returns")
    void create() {
        when(repo.save(any())).thenReturn(make(1L));
        assertEquals(1L, service.create(make(null)).getId());
    }

    @Test @DisplayName("getAll: returns all")
    void getAll() {
        when(repo.findAll()).thenReturn(List.of(make(1L)));
        assertEquals(1, service.getAll().size());
    }

    @Test @DisplayName("getById: found")
    void getById_found() {
        when(repo.findById(1L)).thenReturn(Optional.of(make(1L)));
        assertEquals(50000.0, service.getById(1L).getAllocatedAmount());
    }

    @Test @DisplayName("getById: not found throws")
    void getById_notFound() {
        when(repo.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.getById(99L));
    }

    @Test @DisplayName("update: updates all fields")
    void update() {
        HallGrant existing = make(1L);
        HallGrant updated = new HallGrant(1L, 2L, 2L, 80000.0, 20000.0);
        when(repo.findById(1L)).thenReturn(Optional.of(existing));
        when(repo.save(any())).thenReturn(existing);

        service.update(1L, updated);
        verify(repo).save(argThat(hg -> hg.getAllocatedAmount() == 80000.0));
    }

    @Test @DisplayName("delete: delegates to repository")
    void delete() { service.delete(1L); verify(repo).deleteById(1L); }

    @Test @DisplayName("getByGrantId: filters by grant")
    void getByGrantId() {
        when(repo.findByGrantId(1L)).thenReturn(List.of(make(1L)));
        assertEquals(1, service.getByGrantId(1L).size());
    }

    @Test @DisplayName("getByHallId: filters by hall")
    void getByHallId() {
        when(repo.findByHallId(1L)).thenReturn(List.of(make(1L)));
        assertEquals(1, service.getByHallId(1L).size());
    }
}
