package com.hms.service.impl;

import com.hms.entity.Staff;
import com.hms.enums.StaffType;
import com.hms.exception.ResourceNotFoundException;
import com.hms.repository.StaffRepository;
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
class StaffServiceImplTest {

    @Mock private StaffRepository repo;
    @InjectMocks private StaffServiceImpl service;

    private Staff makeStaff(Long id) {
        return new Staff(id, "Ramesh", StaffType.ATTENDANT, 500.0, 1L);
    }

    @Test @DisplayName("create: saves and returns staff")
    void create() {
        when(repo.save(any())).thenReturn(makeStaff(1L));
        assertEquals(1L, service.create(makeStaff(null)).getId());
    }

    @Test @DisplayName("getAll: returns all staff")
    void getAll() {
        when(repo.findAll()).thenReturn(List.of(makeStaff(1L)));
        assertEquals(1, service.getAll().size());
    }

    @Test @DisplayName("getById: found")
    void getById_found() {
        when(repo.findById(1L)).thenReturn(Optional.of(makeStaff(1L)));
        assertEquals("Ramesh", service.getById(1L).getName());
    }

    @Test @DisplayName("getById: not found throws")
    void getById_notFound() {
        when(repo.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.getById(99L));
    }

    @Test @DisplayName("update: updates all fields")
    void update() {
        Staff existing = makeStaff(1L);
        Staff updated = new Staff(1L, "Suresh", StaffType.GARDENER, 600.0, 2L);
        when(repo.findById(1L)).thenReturn(Optional.of(existing));
        when(repo.save(any())).thenReturn(existing);

        service.update(1L, updated);
        verify(repo).save(argThat(s -> "Suresh".equals(s.getName()) && s.getStaffType() == StaffType.GARDENER));
    }

    @Test @DisplayName("delete: delegates to repository")
    void delete() { service.delete(1L); verify(repo).deleteById(1L); }

    @Test @DisplayName("getByHallId: filters by hall")
    void getByHallId() {
        when(repo.findByHallId(1L)).thenReturn(List.of(makeStaff(1L)));
        assertEquals(1, service.getByHallId(1L).size());
    }
}
