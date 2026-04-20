package com.hms.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.hms.entity.Expenditure;
import com.hms.enums.ExpenseCategory;
import com.hms.exception.GlobalExceptionHandler;
import com.hms.exception.ResourceNotFoundException;
import com.hms.service.ExpenditureService;
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
class ExpenditureControllerTest {

    @Mock private ExpenditureService service;
    @InjectMocks private ExpenditureController controller;
    private MockMvc mockMvc;
    private final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler()).build();
    }

    private Expenditure make(Long id) {
        return new Expenditure(id, 1L, "Repair", 5000.0, LocalDate.of(2026, 3, 1), ExpenseCategory.REPAIR);
    }

    @Test @DisplayName("POST /api/expenditures: creates")
    void create() throws Exception {
        when(service.create(any())).thenReturn(make(1L));
        mockMvc.perform(post("/api/expenditures").contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(make(null))))
                .andExpect(status().isOk()).andExpect(jsonPath("$.description").value("Repair"));
    }

    @Test @DisplayName("POST /api/expenditures: maps expenseCategory JSON to category")
    void create_mapsExpenseCategoryAlias() throws Exception {
        when(service.create(any())).thenAnswer(inv -> {
            Expenditure e = inv.getArgument(0);
            return new Expenditure(1L, e.getHallId(), e.getDescription(), e.getAmount(), e.getDate(), e.getCategory());
        });
        mockMvc.perform(post("/api/expenditures").contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"hallId":1,"description":"x","amount":100,"date":"2026-04-20","expenseCategory":"FOOD"}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.category").value("FOOD"));
    }

    @Test @DisplayName("GET /api/expenditures: returns all")
    void getAll() throws Exception {
        when(service.getAll()).thenReturn(List.of(make(1L)));
        mockMvc.perform(get("/api/expenditures")).andExpect(status().isOk());
    }

    @Test @DisplayName("GET /api/expenditures/{id}: found")
    void getById() throws Exception {
        when(service.getById(1L)).thenReturn(make(1L));
        mockMvc.perform(get("/api/expenditures/1")).andExpect(status().isOk());
    }

    @Test @DisplayName("GET /api/expenditures/{id}: not found")
    void getById_notFound() throws Exception {
        when(service.getById(99L)).thenThrow(new ResourceNotFoundException("Not found"));
        mockMvc.perform(get("/api/expenditures/99")).andExpect(status().isNotFound());
    }

    @Test @DisplayName("PUT /api/expenditures/{id}: updates")
    void update() throws Exception {
        when(service.update(eq(1L), any())).thenReturn(make(1L));
        mockMvc.perform(put("/api/expenditures/1").contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(make(1L)))).andExpect(status().isOk());
    }

    @Test @DisplayName("DELETE /api/expenditures/{id}: deletes")
    void deleteExp() throws Exception {
        doNothing().when(service).delete(1L);
        mockMvc.perform(delete("/api/expenditures/1")).andExpect(status().isOk());
    }

    @Test @DisplayName("GET /api/expenditures/hall/{hallId}: by hall")
    void getByHallId() throws Exception {
        when(service.getByHallId(1L)).thenReturn(List.of(make(1L)));
        mockMvc.perform(get("/api/expenditures/hall/1")).andExpect(status().isOk());
    }
}
