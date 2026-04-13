package com.hms.service.impl;

import com.hms.entity.Grant;
import com.hms.exception.ResourceNotFoundException;
import com.hms.repository.GrantRepository;
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
class GrantServiceImplTest {

    @Mock private GrantRepository repo;
    @InjectMocks private GrantServiceImpl service;

    private Grant makeGrant(Long id) {
        return new Grant(id, 2026, 500000.0);
    }

    @Test @DisplayName("create: saves and returns grant")
    void create() {
        when(repo.save(any())).thenReturn(makeGrant(1L));
        assertEquals(1L, service.create(makeGrant(null)).getId());
    }

    @Test @DisplayName("getAll: returns all grants")
    void getAll() {
        when(repo.findAll()).thenReturn(List.of(makeGrant(1L)));
        assertEquals(1, service.getAll().size());
    }

    @Test @DisplayName("getById: found")
    void getById_found() {
        when(repo.findById(1L)).thenReturn(Optional.of(makeGrant(1L)));
        assertEquals(2026, service.getById(1L).getYear());
    }

    @Test @DisplayName("getById: not found throws")
    void getById_notFound() {
        when(repo.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.getById(99L));
    }

    @Test @DisplayName("update: updates year and amount")
    void update() {
        Grant existing = makeGrant(1L);
        Grant updated = new Grant(1L, 2027, 600000.0);
        when(repo.findById(1L)).thenReturn(Optional.of(existing));
        when(repo.save(any())).thenReturn(existing);

        service.update(1L, updated);
        verify(repo).save(argThat(g -> g.getYear() == 2027 && g.getTotalAmount() == 600000.0));
    }

    @Test @DisplayName("delete: delegates to repository")
    void delete() { service.delete(1L); verify(repo).deleteById(1L); }
}
