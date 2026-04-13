package com.hms.service.impl;

import com.hms.entity.MessManager;
import com.hms.exception.ResourceNotFoundException;
import com.hms.repository.MessManagerRepository;
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
class MessManagerServiceImplTest {

    @Mock private MessManagerRepository repo;
    @InjectMocks private MessManagerServiceImpl service;

    private MessManager make(Long id) {
        return new MessManager(id, "Manager One", 1L);
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
        assertEquals("Manager One", service.getById(1L).getName());
    }

    @Test @DisplayName("getById: not found throws")
    void getById_notFound() {
        when(repo.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.getById(99L));
    }

    @Test @DisplayName("update: updates name and hallId")
    void update() {
        MessManager existing = make(1L);
        MessManager updated = new MessManager(1L, "Manager Two", 2L);
        when(repo.findById(1L)).thenReturn(Optional.of(existing));
        when(repo.save(any())).thenReturn(existing);

        service.update(1L, updated);
        verify(repo).save(argThat(m -> "Manager Two".equals(m.getName()) && m.getHallId().equals(2L)));
    }

    @Test @DisplayName("delete: delegates to repository")
    void delete() { service.delete(1L); verify(repo).deleteById(1L); }
}
