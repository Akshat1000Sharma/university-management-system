package com.hms.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hms.entity.Staff;
import com.hms.enums.StaffType;
import com.hms.exception.GlobalExceptionHandler;
import com.hms.exception.ResourceNotFoundException;
import com.hms.service.StaffService;
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
class StaffControllerTest {

    @Mock private StaffService service;
    @InjectMocks private StaffController controller;
    private MockMvc mockMvc;
    private final ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler()).build();
    }

    private Staff make(Long id) { return new Staff(id, "Ramesh", StaffType.ATTENDANT, 500.0, 1L); }

    @Test @DisplayName("POST /api/staff: creates")
    void create() throws Exception {
        when(service.create(any())).thenReturn(make(1L));
        mockMvc.perform(post("/api/staff").contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(make(null))))
                .andExpect(status().isOk()).andExpect(jsonPath("$.name").value("Ramesh"));
    }

    @Test @DisplayName("GET /api/staff: returns all")
    void getAll() throws Exception {
        when(service.getAll()).thenReturn(List.of(make(1L)));
        mockMvc.perform(get("/api/staff")).andExpect(status().isOk());
    }

    @Test @DisplayName("GET /api/staff/{id}: found")
    void getById() throws Exception {
        when(service.getById(1L)).thenReturn(make(1L));
        mockMvc.perform(get("/api/staff/1")).andExpect(status().isOk());
    }

    @Test @DisplayName("GET /api/staff/{id}: not found")
    void getById_notFound() throws Exception {
        when(service.getById(99L)).thenThrow(new ResourceNotFoundException("Staff not found"));
        mockMvc.perform(get("/api/staff/99")).andExpect(status().isNotFound());
    }

    @Test @DisplayName("PUT /api/staff/{id}: updates")
    void update() throws Exception {
        when(service.update(eq(1L), any())).thenReturn(make(1L));
        mockMvc.perform(put("/api/staff/1").contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(make(1L)))).andExpect(status().isOk());
    }

    @Test @DisplayName("DELETE /api/staff/{id}: deletes")
    void deleteStaff() throws Exception {
        doNothing().when(service).delete(1L);
        mockMvc.perform(delete("/api/staff/1")).andExpect(status().isOk());
    }

    @Test @DisplayName("GET /api/staff/hall/{hallId}: by hall")
    void getByHallId() throws Exception {
        when(service.getByHallId(1L)).thenReturn(List.of(make(1L)));
        mockMvc.perform(get("/api/staff/hall/1")).andExpect(status().isOk());
    }
}
