package com.hms.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hms.entity.Room;
import com.hms.enums.RoomType;
import com.hms.exception.GlobalExceptionHandler;
import com.hms.exception.ResourceNotFoundException;
import com.hms.service.RoomService;
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
class RoomControllerTest {

    @Mock private RoomService service;
    @InjectMocks private RoomController controller;
    private MockMvc mockMvc;
    private final ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler()).build();
    }

    private Room make(Long id) { return new Room(id, "101", RoomType.SINGLE, 3000.0, 1L, false); }

    @Test @DisplayName("POST /api/rooms: creates room")
    void create() throws Exception {
        when(service.create(any())).thenReturn(make(1L));
        String json = """
                {"roomNumber":"101","roomType":"SINGLE","rent":3000.0,"hallId":1,"occupied":false,"isOccupied":false}""";
        mockMvc.perform(post("/api/rooms").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk()).andExpect(jsonPath("$.roomNumber").value("101"));
    }

    @Test @DisplayName("GET /api/rooms: returns all")
    void getAll() throws Exception {
        when(service.getAll()).thenReturn(List.of(make(1L)));
        mockMvc.perform(get("/api/rooms")).andExpect(status().isOk()).andExpect(jsonPath("$.length()").value(1));
    }

    @Test @DisplayName("GET /api/rooms/{id}: found")
    void getById() throws Exception {
        when(service.getById(1L)).thenReturn(make(1L));
        mockMvc.perform(get("/api/rooms/1")).andExpect(status().isOk());
    }

    @Test @DisplayName("GET /api/rooms/{id}: not found")
    void getById_notFound() throws Exception {
        when(service.getById(99L)).thenThrow(new ResourceNotFoundException("Room not found"));
        mockMvc.perform(get("/api/rooms/99")).andExpect(status().isNotFound());
    }

    @Test @DisplayName("PUT /api/rooms/{id}: updates")
    void update() throws Exception {
        when(service.update(eq(1L), any())).thenReturn(make(1L));
        String json = """
                {"id":1,"roomNumber":"101","roomType":"SINGLE","rent":3000.0,"hallId":1,"occupied":false,"isOccupied":false}""";
        mockMvc.perform(put("/api/rooms/1").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk());
    }

    @Test @DisplayName("DELETE /api/rooms/{id}: deletes")
    void deleteRoom() throws Exception {
        doNothing().when(service).delete(1L);
        mockMvc.perform(delete("/api/rooms/1")).andExpect(status().isOk());
    }

    @Test @DisplayName("GET /api/rooms/hall/{hallId}: by hall")
    void getByHallId() throws Exception {
        when(service.getByHallId(1L)).thenReturn(List.of(make(1L)));
        mockMvc.perform(get("/api/rooms/hall/1")).andExpect(status().isOk()).andExpect(jsonPath("$.length()").value(1));
    }
}
