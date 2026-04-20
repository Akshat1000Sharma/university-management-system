package com.hms.service.impl;

import com.hms.entity.Student;
import com.hms.exception.ResourceNotFoundException;
import com.hms.repository.StudentRepository;
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
class StudentServiceImplTest {

    @Mock private StudentRepository repo;
    @InjectMocks private StudentServiceImpl service;

    private Student makeStudent(Long id, String name) {
        return new Student(id, name, "Addr", "9999", null, null, null, null, 1L, 1L);
    }

    @Test
    @DisplayName("create: saves and returns student")
    void create_success() {
        Student s = makeStudent(null, "Alice");
        Student saved = makeStudent(1L, "Alice");
        when(repo.save(s)).thenReturn(saved);

        Student result = service.create(s);
        assertEquals(1L, result.getId());
        assertEquals("Alice", result.getName());
    }

    @Test
    @DisplayName("getAll: returns all students")
    void getAll_returnsList() {
        when(repo.findAll()).thenReturn(List.of(makeStudent(1L, "A"), makeStudent(2L, "B")));
        List<Student> result = service.getAll();
        assertEquals(2, result.size());
    }

    @Test
    @DisplayName("getById: existing ID returns student")
    void getById_found() {
        when(repo.findById(1L)).thenReturn(Optional.of(makeStudent(1L, "Alice")));
        Student result = service.getById(1L);
        assertEquals("Alice", result.getName());
    }

    @Test
    @DisplayName("getById: missing ID throws ResourceNotFoundException")
    void getById_notFound() {
        when(repo.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.getById(99L));
    }

    @Test
    @DisplayName("update: updates all fields and saves")
    void update_success() {
        Student existing = makeStudent(1L, "Alice");
        Student updated = makeStudent(1L, "Alice Updated");
        updated.setPhone("1111");
        when(repo.findById(1L)).thenReturn(Optional.of(existing));
        when(repo.save(any())).thenReturn(updated);

        Student result = service.update(1L, updated);
        assertEquals("Alice Updated", result.getName());
        verify(repo).save(existing);
    }

    @Test
    @DisplayName("delete: calls deleteById on repository")
    void delete_success() {
        service.delete(1L);
        verify(repo).deleteById(1L);
    }

    @Test
    @DisplayName("getByHallId: returns students for given hall")
    void getByHallId_returnsList() {
        when(repo.findByHallId(1L)).thenReturn(List.of(makeStudent(1L, "A")));
        List<Student> result = service.getByHallId(1L);
        assertEquals(1, result.size());
    }
}
