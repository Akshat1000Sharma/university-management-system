package com.hms.service.impl;

import com.hms.entity.Payment;
import com.hms.exception.ResourceNotFoundException;
import com.hms.repository.PaymentRepository;
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
class PaymentServiceImplTest {

    @Mock private PaymentRepository repo;
    @InjectMocks private PaymentServiceImpl service;

    private Payment makePayment(Long id) {
        return new Payment(id, 1L, 5000.0, LocalDate.of(2026, 3, 15));
    }

    @Test @DisplayName("create: saves and returns payment")
    void create() {
        when(repo.save(any())).thenReturn(makePayment(1L));
        assertEquals(1L, service.create(makePayment(null)).getId());
    }

    @Test @DisplayName("getAll: returns all payments")
    void getAll() {
        when(repo.findAll()).thenReturn(List.of(makePayment(1L)));
        assertEquals(1, service.getAll().size());
    }

    @Test @DisplayName("getById: found")
    void getById_found() {
        when(repo.findById(1L)).thenReturn(Optional.of(makePayment(1L)));
        assertEquals(5000.0, service.getById(1L).getAmount());
    }

    @Test @DisplayName("getById: not found throws")
    void getById_notFound() {
        when(repo.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.getById(99L));
    }

    @Test @DisplayName("update: updates fields")
    void update() {
        Payment existing = makePayment(1L);
        Payment updated = new Payment(1L, 2L, 7000.0, LocalDate.of(2026, 4, 1));
        when(repo.findById(1L)).thenReturn(Optional.of(existing));
        when(repo.save(any())).thenReturn(existing);

        service.update(1L, updated);
        verify(repo).save(argThat(p -> p.getAmount() == 7000.0 && p.getStudentId().equals(2L)));
    }

    @Test @DisplayName("delete: delegates to repository")
    void delete() { service.delete(1L); verify(repo).deleteById(1L); }

    @Test @DisplayName("getByStudentId: filters by student")
    void getByStudentId() {
        when(repo.findByStudentId(1L)).thenReturn(List.of(makePayment(1L)));
        assertEquals(1, service.getByStudentId(1L).size());
    }
}
