package com.hms.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hms.entity.HallGrant;
import com.hms.exception.GlobalExceptionHandler;
import com.hms.exception.ResourceNotFoundException;
import com.hms.service.HallGrantService;
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
class HallGrantControllerTest {

    @Mock private HallGrantService service;
    @InjectMocks private HallGrantController controller;
    private MockMvc mockMvc;
    private final ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler()).build();
    }

    private HallGrant make(Long id) { return new HallGrant(id, 1L, 1L, 50000.0, 10000.0); }

    @Test @DisplayName("POST /api/hall-grants: creates")
    void create() throws Exception {
        when(service.create(any())).thenReturn(make(1L));
        mockMvc.perform(post("/api/hall-grants").contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(make(null))))
                .andExpect(status().isOk()).andExpect(jsonPath("$.allocatedAmount").value(50000.0));
    }

    @Test @DisplayName("GET /api/hall-grants: returns all")
    void getAll() throws Exception {
        when(service.getAll()).thenReturn(List.of(make(1L)));
        mockMvc.perform(get("/api/hall-grants")).andExpect(status().isOk());
    }

    @Test @DisplayName("GET /api/hall-grants/{id}: found")
    void getById() throws Exception {
        when(service.getById(1L)).thenReturn(make(1L));
        mockMvc.perform(get("/api/hall-grants/1")).andExpect(status().isOk());
    }

    @Test @DisplayName("GET /api/hall-grants/{id}: not found")
    void getById_notFound() throws Exception {
        when(service.getById(99L)).thenThrow(new ResourceNotFoundException("Not found"));
        mockMvc.perform(get("/api/hall-grants/99")).andExpect(status().isNotFound());
    }

    @Test @DisplayName("PUT /api/hall-grants/{id}: updates")
    void update() throws Exception {
        when(service.update(eq(1L), any())).thenReturn(make(1L));
        mockMvc.perform(put("/api/hall-grants/1").contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(make(1L)))).andExpect(status().isOk());
    }

    @Test @DisplayName("DELETE /api/hall-grants/{id}: deletes")
    void deleteHallGrant() throws Exception {
        doNothing().when(service).delete(1L);
        mockMvc.perform(delete("/api/hall-grants/1")).andExpect(status().isOk());
    }

    @Test @DisplayName("GET /api/hall-grants/grant/{grantId}: by grant")
    void getByGrantId() throws Exception {
        when(service.getByGrantId(1L)).thenReturn(List.of(make(1L)));
        mockMvc.perform(get("/api/hall-grants/grant/1")).andExpect(status().isOk());
    }

    @Test @DisplayName("GET /api/hall-grants/hall/{hallId}: by hall")
    void getByHallId() throws Exception {
        when(service.getByHallId(1L)).thenReturn(List.of(make(1L)));
        mockMvc.perform(get("/api/hall-grants/hall/1")).andExpect(status().isOk());
    }
}
