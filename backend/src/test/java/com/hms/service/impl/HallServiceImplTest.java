package com.hms.service.impl;

import com.hms.entity.Hall;
import com.hms.exception.ResourceNotFoundException;
import com.hms.repository.HallRepository;
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
class HallServiceImplTest {

    @Mock private HallRepository repo;
    @InjectMocks private HallServiceImpl service;

    private Hall makeHall(Long id) {
        return new Hall(id, "Tagore Hall", false, 500.0);
    }

    @Test
    @DisplayName("create: saves and returns hall")
    void create() {
        Hall h = makeHall(null);
        when(repo.save(h)).thenReturn(makeHall(1L));
        assertEquals(1L, service.create(h).getId());
    }

    @Test
    @DisplayName("getAll: returns all halls")
    void getAll() {
        when(repo.findAll()).thenReturn(List.of(makeHall(1L)));
        assertEquals(1, service.getAll().size());
    }

    @Test
    @DisplayName("getById: found returns hall")
    void getById_found() {
        when(repo.findById(1L)).thenReturn(Optional.of(makeHall(1L)));
        assertEquals("Tagore Hall", service.getById(1L).getName());
    }

    @Test
    @DisplayName("getById: missing throws ResourceNotFoundException")
    void getById_notFound() {
        when(repo.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.getById(99L));
    }

    @Test
    @DisplayName("update: updates name, isNew, and amenityCharge")
    void update() {
        Hall existing = makeHall(1L);
        Hall updated = new Hall(1L, "Raman Hall", true, 700.0);
        when(repo.findById(1L)).thenReturn(Optional.of(existing));
        when(repo.save(any())).thenReturn(existing);

        service.update(1L, updated);
        verify(repo).save(argThat(h ->
                "Raman Hall".equals(h.getName()) && h.isNew() && h.getAmenityCharge() == 700.0));
    }

    @Test
    @DisplayName("delete: delegates to repository")
    void delete() {
        service.delete(1L);
        verify(repo).deleteById(1L);
    }
}
