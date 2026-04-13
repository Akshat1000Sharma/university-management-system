package com.hms.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hms.entity.Warden;
import com.hms.exception.GlobalExceptionHandler;
import com.hms.exception.ResourceNotFoundException;
import com.hms.service.WardenService;
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
class WardenControllerTest {

    @Mock private WardenService service;
    @InjectMocks private WardenController controller;
    private MockMvc mockMvc;
    private final ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler()).build();
    }

    private Warden make(Long id) { return new Warden(id, "Dr. Smith", "9876543210", 1L, false); }

    @Test @DisplayName("POST /api/wardens: creates")
    void create() throws Exception {
        when(service.create(any())).thenReturn(make(1L));
        String json = """
                {"name":"Dr. Smith","contact":"9876543210","hallId":1,"controlling":false,"isControlling":false}""";
        mockMvc.perform(post("/api/wardens").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk()).andExpect(jsonPath("$.name").value("Dr. Smith"));
    }

    @Test @DisplayName("GET /api/wardens: returns all")
    void getAll() throws Exception {
        when(service.getAll()).thenReturn(List.of(make(1L)));
        mockMvc.perform(get("/api/wardens")).andExpect(status().isOk());
    }

    @Test @DisplayName("GET /api/wardens/{id}: found")
    void getById() throws Exception {
        when(service.getById(1L)).thenReturn(make(1L));
        mockMvc.perform(get("/api/wardens/1")).andExpect(status().isOk());
    }

    @Test @DisplayName("GET /api/wardens/{id}: not found")
    void getById_notFound() throws Exception {
        when(service.getById(99L)).thenThrow(new ResourceNotFoundException("Warden not found"));
        mockMvc.perform(get("/api/wardens/99")).andExpect(status().isNotFound());
    }

    @Test @DisplayName("PUT /api/wardens/{id}: updates")
    void update() throws Exception {
        when(service.update(eq(1L), any())).thenReturn(make(1L));
        String json = """
                {"id":1,"name":"Dr. Smith","contact":"9876543210","hallId":1,"controlling":false,"isControlling":false}""";
        mockMvc.perform(put("/api/wardens/1").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk());
    }

    @Test @DisplayName("DELETE /api/wardens/{id}: deletes")
    void deleteWarden() throws Exception {
        doNothing().when(service).delete(1L);
        mockMvc.perform(delete("/api/wardens/1")).andExpect(status().isOk());
    }
}
