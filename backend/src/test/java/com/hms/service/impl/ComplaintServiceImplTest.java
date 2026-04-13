package com.hms.service.impl;

import com.hms.entity.Complaint;
import com.hms.enums.ComplaintStatus;
import com.hms.enums.ComplaintType;
import com.hms.exception.ResourceNotFoundException;
import com.hms.repository.ComplaintRepository;
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
class ComplaintServiceImplTest {

    @Mock private ComplaintRepository repo;
    @InjectMocks private ComplaintServiceImpl service;

    private Complaint makeComplaint(Long id) {
        return new Complaint(id, 1L, 1L, ComplaintType.FUSED_LIGHT, "Light issue", ComplaintStatus.PENDING, null);
    }

    @Test
    @DisplayName("create: saves and returns complaint")
    void create_success() {
        Complaint c = makeComplaint(null);
        Complaint saved = makeComplaint(1L);
        when(repo.save(c)).thenReturn(saved);

        Complaint result = service.create(c);
        assertEquals(1L, result.getId());
    }

    @Test
    @DisplayName("getAll: returns all complaints")
    void getAll() {
        when(repo.findAll()).thenReturn(List.of(makeComplaint(1L), makeComplaint(2L)));
        assertEquals(2, service.getAll().size());
    }

    @Test
    @DisplayName("getById: existing returns complaint")
    void getById_found() {
        when(repo.findById(1L)).thenReturn(Optional.of(makeComplaint(1L)));
        assertNotNull(service.getById(1L));
    }

    @Test
    @DisplayName("getById: missing throws ResourceNotFoundException")
    void getById_notFound() {
        when(repo.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.getById(99L));
    }

    @Test
    @DisplayName("update: updates all fields correctly")
    void update_success() {
        Complaint existing = makeComplaint(1L);
        Complaint updated = new Complaint(1L, 2L, 2L, ComplaintType.WATER_TAP, "Tap broken", ComplaintStatus.IN_PROGRESS, "Checking");
        when(repo.findById(1L)).thenReturn(Optional.of(existing));
        when(repo.save(any())).thenReturn(existing);

        service.update(1L, updated);
        verify(repo).save(argThat(c ->
                c.getStudentId().equals(2L) &&
                c.getType() == ComplaintType.WATER_TAP &&
                c.getStatus() == ComplaintStatus.IN_PROGRESS));
    }

    @Test
    @DisplayName("delete: calls deleteById")
    void delete_success() {
        service.delete(1L);
        verify(repo).deleteById(1L);
    }

    @Test
    @DisplayName("getByHallId: filters by hall")
    void getByHallId() {
        when(repo.findByHallId(1L)).thenReturn(List.of(makeComplaint(1L)));
        assertEquals(1, service.getByHallId(1L).size());
    }

    @Test
    @DisplayName("getByStudentId: filters by student")
    void getByStudentId() {
        when(repo.findByStudentId(1L)).thenReturn(List.of(makeComplaint(1L)));
        assertEquals(1, service.getByStudentId(1L).size());
    }
}
