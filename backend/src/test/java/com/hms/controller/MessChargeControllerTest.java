package com.hms.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hms.entity.MessCharge;
import com.hms.exception.GlobalExceptionHandler;
import com.hms.exception.ResourceNotFoundException;
import com.hms.service.MessChargeService;
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
class MessChargeControllerTest {

    @Mock private MessChargeService service;
    @InjectMocks private MessChargeController controller;
    private MockMvc mockMvc;
    private final ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler()).build();
    }

    private MessCharge make(Long id) { return new MessCharge(id, 1L, 1L, 3, 2026, 2500.0); }

    @Test @DisplayName("POST /api/mess-charges: creates")
    void create() throws Exception {
        when(service.create(any())).thenReturn(make(1L));
        mockMvc.perform(post("/api/mess-charges").contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(make(null))))
                .andExpect(status().isOk()).andExpect(jsonPath("$.amount").value(2500.0));
    }

    @Test @DisplayName("GET /api/mess-charges: returns all")
    void getAll() throws Exception {
        when(service.getAll()).thenReturn(List.of(make(1L)));
        mockMvc.perform(get("/api/mess-charges")).andExpect(status().isOk());
    }

    @Test @DisplayName("GET /api/mess-charges/{id}: found")
    void getById() throws Exception {
        when(service.getById(1L)).thenReturn(make(1L));
        mockMvc.perform(get("/api/mess-charges/1")).andExpect(status().isOk());
    }

    @Test @DisplayName("GET /api/mess-charges/{id}: not found")
    void getById_notFound() throws Exception {
        when(service.getById(99L)).thenThrow(new ResourceNotFoundException("Not found"));
        mockMvc.perform(get("/api/mess-charges/99")).andExpect(status().isNotFound());
    }

    @Test @DisplayName("PUT /api/mess-charges/{id}: updates")
    void update() throws Exception {
        when(service.update(eq(1L), any())).thenReturn(make(1L));
        mockMvc.perform(put("/api/mess-charges/1").contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(make(1L)))).andExpect(status().isOk());
    }

    @Test @DisplayName("DELETE /api/mess-charges/{id}: deletes")
    void deleteCharge() throws Exception {
        doNothing().when(service).delete(1L);
        mockMvc.perform(delete("/api/mess-charges/1")).andExpect(status().isOk());
    }

    @Test @DisplayName("GET /api/mess-charges/student/{studentId}: by student")
    void getByStudentId() throws Exception {
        when(service.getByStudentId(1L)).thenReturn(List.of(make(1L)));
        mockMvc.perform(get("/api/mess-charges/student/1")).andExpect(status().isOk());
    }

    @Test @DisplayName("GET /api/mess-charges/hall/{hallId}: by hall/month/year")
    void getByHallIdAndMonthAndYear() throws Exception {
        when(service.getByHallIdAndMonthAndYear(1L, 3, 2026)).thenReturn(List.of(make(1L)));
        mockMvc.perform(get("/api/mess-charges/hall/1").param("month", "3").param("year", "2026"))
                .andExpect(status().isOk());
    }
}
