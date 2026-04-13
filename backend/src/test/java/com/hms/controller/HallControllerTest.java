package com.hms.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hms.entity.Hall;
import com.hms.exception.GlobalExceptionHandler;
import com.hms.exception.ResourceNotFoundException;
import com.hms.service.HallService;
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
class HallControllerTest {

    @Mock private HallService service;
    @InjectMocks private HallController controller;

    private MockMvc mockMvc;
    private final ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    private Hall make(Long id) { return new Hall(id, "Tagore Hall", false, 500.0); }

    @Test @DisplayName("POST /api/halls: creates hall")
    void create() throws Exception {
        when(service.create(any())).thenReturn(make(1L));
        String json = """
                {"name":"Tagore Hall","new":false,"isNew":false,"amenityCharge":500.0}""";
        mockMvc.perform(post("/api/halls").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Tagore Hall"));
    }

    @Test @DisplayName("GET /api/halls: returns all")
    void getAll() throws Exception {
        when(service.getAll()).thenReturn(List.of(make(1L)));
        mockMvc.perform(get("/api/halls")).andExpect(status().isOk()).andExpect(jsonPath("$.length()").value(1));
    }

    @Test @DisplayName("GET /api/halls/{id}: found")
    void getById() throws Exception {
        when(service.getById(1L)).thenReturn(make(1L));
        mockMvc.perform(get("/api/halls/1")).andExpect(status().isOk()).andExpect(jsonPath("$.name").value("Tagore Hall"));
    }

    @Test @DisplayName("GET /api/halls/{id}: not found")
    void getById_notFound() throws Exception {
        when(service.getById(99L)).thenThrow(new ResourceNotFoundException("Hall not found"));
        mockMvc.perform(get("/api/halls/99")).andExpect(status().isNotFound());
    }

    @Test @DisplayName("PUT /api/halls/{id}: updates hall")
    void update() throws Exception {
        when(service.update(eq(1L), any())).thenReturn(make(1L));
        String json = """
                {"id":1,"name":"Tagore Hall","new":false,"isNew":false,"amenityCharge":500.0}""";
        mockMvc.perform(put("/api/halls/1").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk());
    }

    @Test @DisplayName("DELETE /api/halls/{id}: deletes")
    void deleteHall() throws Exception {
        doNothing().when(service).delete(1L);
        mockMvc.perform(delete("/api/halls/1")).andExpect(status().isOk());
    }
}
