package com.hms.service.impl;

import com.hms.entity.Expenditure;
import com.hms.enums.ExpenseCategory;
import com.hms.exception.ResourceNotFoundException;
import com.hms.repository.ExpenditureRepository;
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
class ExpenditureServiceImplTest {

    @Mock private ExpenditureRepository repo;
    @InjectMocks private ExpenditureServiceImpl service;

    private Expenditure makeExpenditure(Long id) {
        return new Expenditure(id, 1L, "Repair work", 5000.0, LocalDate.of(2026, 3, 1), ExpenseCategory.REPAIR);
    }

    @Test @DisplayName("create: saves and returns expenditure")
    void create() {
        when(repo.save(any())).thenReturn(makeExpenditure(1L));
        assertEquals(1L, service.create(makeExpenditure(null)).getId());
    }

    @Test @DisplayName("getAll: returns all expenditures")
    void getAll() {
        when(repo.findAll()).thenReturn(List.of(makeExpenditure(1L)));
        assertEquals(1, service.getAll().size());
    }

    @Test @DisplayName("getById: found")
    void getById_found() {
        when(repo.findById(1L)).thenReturn(Optional.of(makeExpenditure(1L)));
        assertEquals("Repair work", service.getById(1L).getDescription());
    }

    @Test @DisplayName("getById: not found throws")
    void getById_notFound() {
        when(repo.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.getById(99L));
    }

    @Test @DisplayName("update: updates all fields")
    void update() {
        Expenditure existing = makeExpenditure(1L);
        Expenditure updated = new Expenditure(1L, 2L, "Magazine", 1000.0, LocalDate.of(2026, 4, 1), ExpenseCategory.MAGAZINE);
        when(repo.findById(1L)).thenReturn(Optional.of(existing));
        when(repo.save(any())).thenReturn(existing);

        service.update(1L, updated);
        verify(repo).save(argThat(e -> "Magazine".equals(e.getDescription()) && e.getCategory() == ExpenseCategory.MAGAZINE));
    }

    @Test @DisplayName("delete: delegates to repository")
    void delete() { service.delete(1L); verify(repo).deleteById(1L); }

    @Test @DisplayName("getByHallId: filters by hall")
    void getByHallId() {
        when(repo.findByHallId(1L)).thenReturn(List.of(makeExpenditure(1L)));
        assertEquals(1, service.getByHallId(1L).size());
    }
}
