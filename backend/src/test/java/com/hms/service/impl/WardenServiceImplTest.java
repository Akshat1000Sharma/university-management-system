package com.hms.service.impl;

import com.hms.entity.Warden;
import com.hms.exception.ResourceNotFoundException;
import com.hms.repository.WardenRepository;
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
class WardenServiceImplTest {

    @Mock private WardenRepository repo;
    @InjectMocks private WardenServiceImpl service;

    private Warden makeWarden(Long id) {
        return new Warden(id, "Dr. Smith", "9876543210", 1L, false);
    }

    @Test @DisplayName("create: saves and returns warden")
    void create() {
        when(repo.save(any())).thenReturn(makeWarden(1L));
        assertEquals(1L, service.create(makeWarden(null)).getId());
    }

    @Test @DisplayName("getAll: returns all wardens")
    void getAll() {
        when(repo.findAll()).thenReturn(List.of(makeWarden(1L)));
        assertEquals(1, service.getAll().size());
    }

    @Test @DisplayName("getById: found")
    void getById_found() {
        when(repo.findById(1L)).thenReturn(Optional.of(makeWarden(1L)));
        assertEquals("Dr. Smith", service.getById(1L).getName());
    }

    @Test @DisplayName("getById: not found throws")
    void getById_notFound() {
        when(repo.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.getById(99L));
    }

    @Test @DisplayName("update: updates all fields")
    void update() {
        Warden existing = makeWarden(1L);
        Warden updated = new Warden(1L, "Dr. Jones", "1111111111", 2L, true);
        when(repo.findById(1L)).thenReturn(Optional.of(existing));
        when(repo.save(any())).thenReturn(existing);

        service.update(1L, updated);
        verify(repo).save(argThat(w -> "Dr. Jones".equals(w.getName()) && w.isControlling()));
    }

    @Test @DisplayName("delete: delegates to repository")
    void delete() { service.delete(1L); verify(repo).deleteById(1L); }
}
