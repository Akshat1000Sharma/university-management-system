package com.hms.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hms.dto.*;
import com.hms.exception.GlobalExceptionHandler;
import com.hms.exception.ResourceNotFoundException;
import com.hms.service.BusinessService;
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

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class BusinessControllerTest {

    @Mock private BusinessService service;
    @InjectMocks private BusinessController controller;

    private MockMvc mockMvc;
    private final ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    @DisplayName("POST /api/business/admit: admits student and returns dues")
    void admitStudent() throws Exception {
        StudentDueResponse response = new StudentDueResponse(1L, "John", 0, 3000, 500, 3500);
        when(service.admitStudent(any())).thenReturn(response);

        AdmitStudentRequest request = new AdmitStudentRequest("John", "Addr", "123", null, null, null, null, 1L, 1L);
        mockMvc.perform(post("/api/business/admit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.studentName").value("John"))
                .andExpect(jsonPath("$.totalDue").value(3500));
    }

    @Test
    @DisplayName("GET /api/business/student/{id}/dues: returns student dues")
    void getStudentDues() throws Exception {
        StudentDueResponse response = new StudentDueResponse(1L, "John", 2000, 3000, 500, 5500);
        when(service.getStudentDues(1L, 3, 2026)).thenReturn(response);

        mockMvc.perform(get("/api/business/student/1/dues")
                        .param("month", "3").param("year", "2026"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalDue").value(5500));
    }

    @Test
    @DisplayName("GET /api/business/student/{id}/dues: non-existent student returns 404")
    void getStudentDues_notFound() throws Exception {
        when(service.getStudentDues(eq(99L), anyInt(), anyInt()))
                .thenThrow(new ResourceNotFoundException("Student not found"));

        mockMvc.perform(get("/api/business/student/99/dues")
                        .param("month", "3").param("year", "2026"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GET /api/business/mess-payment/hall/{id}: returns mess payment summary")
    void getMessPayment() throws Exception {
        MessManagerPaymentResponse response = new MessManagerPaymentResponse(1L, "Hall", 1L, "Mgr", 3, 2026, 50000);
        when(service.getMessManagerPayment(1L, 3, 2026)).thenReturn(response);

        mockMvc.perform(get("/api/business/mess-payment/hall/1")
                        .param("month", "3").param("year", "2026"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalAmount").value(50000));
    }

    @Test
    @DisplayName("GET /api/business/salary-sheet/hall/{id}: returns salary list")
    void getSalarySheet() throws Exception {
        StaffSalaryResponse sr = new StaffSalaryResponse(1L, "Staff", "ATTENDANT", 500, 31, 2, 29, 14500);
        when(service.getSalarySheet(1L, 3, 2026)).thenReturn(List.of(sr));

        mockMvc.perform(get("/api/business/salary-sheet/hall/1")
                        .param("month", "3").param("year", "2026"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].salary").value(14500));
    }

    @Test
    @DisplayName("GET /api/business/occupancy/hall/{id}: returns occupancy")
    void getHallOccupancy() throws Exception {
        OccupancyResponse response = new OccupancyResponse(1L, "Hall", 10, 7, 3, 70.0);
        when(service.getHallOccupancy(1L)).thenReturn(response);

        mockMvc.perform(get("/api/business/occupancy/hall/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.occupancyPercent").value(70.0));
    }

    @Test
    @DisplayName("GET /api/business/occupancy: returns overall occupancy")
    void getOverallOccupancy() throws Exception {
        when(service.getOverallOccupancy()).thenReturn(List.of(
                new OccupancyResponse(1L, "H1", 10, 5, 5, 50.0),
                new OccupancyResponse(2L, "H2", 8, 6, 2, 75.0)));

        mockMvc.perform(get("/api/business/occupancy"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    @DisplayName("PUT /api/business/complaints/{id}/atr: posts ATR")
    void postATR() throws Exception {
        when(service.postATR(1L, "Fixed")).thenReturn("ATR posted successfully for complaint ID: 1");

        mockMvc.perform(put("/api/business/complaints/1/atr")
                        .param("atr", "Fixed"))
                .andExpect(status().isOk())
                .andExpect(content().string("ATR posted successfully for complaint ID: 1"));
    }

    @Test
    @DisplayName("GET /api/business/annual-statement/hall/{id}: returns annual statement")
    void getAnnualStatement() throws Exception {
        AnnualStatementResponse response = new AnnualStatementResponse(
                1L, "Hall", 2026, 100000, 30000, 5000, 65000, List.of());
        when(service.getAnnualStatement(1L, 2026)).thenReturn(response);

        mockMvc.perform(get("/api/business/annual-statement/hall/1")
                        .param("year", "2026"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalBalance").value(65000));
    }
}
