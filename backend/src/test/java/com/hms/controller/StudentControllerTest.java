package com.hms.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hms.entity.Student;
import com.hms.exception.GlobalExceptionHandler;
import com.hms.exception.ResourceNotFoundException;
import com.hms.service.StudentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class StudentControllerTest {

    @Mock private StudentService service;
    @InjectMocks private StudentController controller;

    private MockMvc mockMvc;
    private final ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    private Student makeStudent(Long id) {
        return new Student(id, "Alice", "Addr", "999", null, null, null, null, 1L, 1L);
    }

    @Test
    @DisplayName("POST /api/students: creates student")
    void create() throws Exception {
        when(service.create(any())).thenReturn(makeStudent(1L));

        mockMvc.perform(post("/api/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(makeStudent(null))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Alice"));
    }

    @Test
    @DisplayName("GET /api/students: returns all students")
    void getAll() throws Exception {
        when(service.getAll()).thenReturn(List.of(makeStudent(1L), makeStudent(2L)));

        mockMvc.perform(get("/api/students"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    @DisplayName("GET /api/students/{id}: returns student by id")
    void getById() throws Exception {
        when(service.getById(1L)).thenReturn(makeStudent(1L));

        mockMvc.perform(get("/api/students/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Alice"));
    }

    @Test
    @DisplayName("GET /api/students/{id}: not found returns 404")
    void getById_notFound() throws Exception {
        when(service.getById(99L)).thenThrow(new ResourceNotFoundException("Student not found"));

        mockMvc.perform(get("/api/students/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("PUT /api/students/{id}: updates student")
    void update() throws Exception {
        when(service.update(eq(1L), any())).thenReturn(makeStudent(1L));

        mockMvc.perform(put("/api/students/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(makeStudent(1L))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @DisplayName("DELETE /api/students/{id}: deletes student")
    void deleteStudent() throws Exception {
        doNothing().when(service).delete(1L);

        mockMvc.perform(delete("/api/students/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Student deleted"));
    }

    @Test
    @DisplayName("GET /api/students/hall/{hallId}: returns students by hall")
    void getByHallId() throws Exception {
        when(service.getByHallId(1L)).thenReturn(List.of(makeStudent(1L)));

        mockMvc.perform(get("/api/students/hall/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }
}
