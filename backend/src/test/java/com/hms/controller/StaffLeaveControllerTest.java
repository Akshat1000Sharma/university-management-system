package com.hms.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.hms.entity.StaffLeave;
import com.hms.exception.GlobalExceptionHandler;
import com.hms.exception.ResourceNotFoundException;
import com.hms.service.StaffLeaveService;
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

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class StaffLeaveControllerTest {

    @Mock private StaffLeaveService service;
    @InjectMocks private StaffLeaveController controller;
    private MockMvc mockMvc;
    private final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler()).build();
    }

    private StaffLeave make(Long id) { return new StaffLeave(id, 1L, LocalDate.of(2026, 3, 15)); }

    @Test @DisplayName("POST /api/staff-leaves: creates")
    void create() throws Exception {
        when(service.create(any())).thenReturn(make(1L));
        mockMvc.perform(post("/api/staff-leaves").contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(make(null))))
                .andExpect(status().isOk());
    }

    @Test @DisplayName("GET /api/staff-leaves: returns all")
    void getAll() throws Exception {
        when(service.getAll()).thenReturn(List.of(make(1L)));
        mockMvc.perform(get("/api/staff-leaves")).andExpect(status().isOk());
    }

    @Test @DisplayName("GET /api/staff-leaves/{id}: found")
    void getById() throws Exception {
        when(service.getById(1L)).thenReturn(make(1L));
        mockMvc.perform(get("/api/staff-leaves/1")).andExpect(status().isOk());
    }

    @Test @DisplayName("GET /api/staff-leaves/{id}: not found")
    void getById_notFound() throws Exception {
        when(service.getById(99L)).thenThrow(new ResourceNotFoundException("Not found"));
        mockMvc.perform(get("/api/staff-leaves/99")).andExpect(status().isNotFound());
    }

    @Test @DisplayName("PUT /api/staff-leaves/{id}: updates")
    void update() throws Exception {
        when(service.update(eq(1L), any())).thenReturn(make(1L));
        mockMvc.perform(put("/api/staff-leaves/1").contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(make(1L)))).andExpect(status().isOk());
    }

    @Test @DisplayName("DELETE /api/staff-leaves/{id}: deletes")
    void deleteLeave() throws Exception {
        doNothing().when(service).delete(1L);
        mockMvc.perform(delete("/api/staff-leaves/1")).andExpect(status().isOk());
    }

    @Test @DisplayName("GET /api/staff-leaves/staff/{staffId}: by staff")
    void getByStaffId() throws Exception {
        when(service.getByStaffId(1L)).thenReturn(List.of(make(1L)));
        mockMvc.perform(get("/api/staff-leaves/staff/1")).andExpect(status().isOk());
    }
}
