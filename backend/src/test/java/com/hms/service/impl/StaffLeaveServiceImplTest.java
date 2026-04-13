package com.hms.service.impl;

import com.hms.entity.StaffLeave;
import com.hms.exception.ResourceNotFoundException;
import com.hms.repository.StaffLeaveRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StaffLeaveServiceImplTest {

    @Mock private StaffLeaveRepository repo;
    @InjectMocks private StaffLeaveServiceImpl service;

    private StaffLeave make(Long id) {
        return new StaffLeave(id, 1L, LocalDate.of(2026, 3, 15));
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
        assertEquals(1L, service.getById(1L).getStaffId());
    }

    @Test @DisplayName("getById: not found throws")
    void getById_notFound() {
        when(repo.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.getById(99L));
    }

    @Test @DisplayName("update: updates fields")
    void update() {
        StaffLeave existing = make(1L);
        StaffLeave updated = new StaffLeave(1L, 2L, LocalDate.of(2026, 4, 1));
        when(repo.findById(1L)).thenReturn(Optional.of(existing));
        when(repo.save(any())).thenReturn(existing);

        service.update(1L, updated);
        verify(repo).save(argThat(sl -> sl.getStaffId().equals(2L)));
    }

    @Test @DisplayName("delete: delegates to repository")
    void delete() { service.delete(1L); verify(repo).deleteById(1L); }

    @Test @DisplayName("getByStaffId: filters by staff")
    void getByStaffId() {
        when(repo.findByStaffId(1L)).thenReturn(List.of(make(1L)));
        assertEquals(1, service.getByStaffId(1L).size());
    }
}
