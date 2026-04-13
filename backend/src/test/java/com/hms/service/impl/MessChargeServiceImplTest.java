package com.hms.service.impl;

import com.hms.entity.MessCharge;
import com.hms.exception.ResourceNotFoundException;
import com.hms.repository.MessChargeRepository;
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
class MessChargeServiceImplTest {

    @Mock private MessChargeRepository repo;
    @InjectMocks private MessChargeServiceImpl service;

    private MessCharge make(Long id) {
        return new MessCharge(id, 1L, 1L, 3, 2026, 2500.0);
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
        assertEquals(2500.0, service.getById(1L).getAmount());
    }

    @Test @DisplayName("getById: not found throws")
    void getById_notFound() {
        when(repo.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.getById(99L));
    }

    @Test @DisplayName("update: updates all fields")
    void update() {
        MessCharge existing = make(1L);
        MessCharge updated = new MessCharge(1L, 2L, 2L, 4, 2026, 3000.0);
        when(repo.findById(1L)).thenReturn(Optional.of(existing));
        when(repo.save(any())).thenReturn(existing);

        service.update(1L, updated);
        verify(repo).save(argThat(mc -> mc.getAmount() == 3000.0 && mc.getMonth() == 4));
    }

    @Test @DisplayName("delete: delegates to repository")
    void delete() { service.delete(1L); verify(repo).deleteById(1L); }

    @Test @DisplayName("getByStudentId: filters by student")
    void getByStudentId() {
        when(repo.findByStudentId(1L)).thenReturn(List.of(make(1L)));
        assertEquals(1, service.getByStudentId(1L).size());
    }

    @Test @DisplayName("getByHallIdAndMonthAndYear: filters correctly")
    void getByHallIdAndMonthAndYear() {
        when(repo.findByHallIdAndMonthAndYear(1L, 3, 2026)).thenReturn(List.of(make(1L)));
        assertEquals(1, service.getByHallIdAndMonthAndYear(1L, 3, 2026).size());
    }
}
