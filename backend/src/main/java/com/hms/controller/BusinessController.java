package com.hms.controller;

import com.hms.dto.*;
import com.hms.service.BusinessService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/business")
public class BusinessController {

    private final BusinessService service;

    public BusinessController(BusinessService service) { this.service = service; }

    // Admit student + allot room
    @PostMapping("/admit")
    public ResponseEntity<StudentDueResponse> admitStudent(@RequestBody AdmitStudentRequest request) {
        return ResponseEntity.ok(service.admitStudent(request));
    }

    // Get student total dues
    @GetMapping("/student/{studentId}/dues")
    public ResponseEntity<StudentDueResponse> getStudentDues(
            @PathVariable Long studentId,
            @RequestParam int month,
            @RequestParam int year) {
        return ResponseEntity.ok(service.getStudentDues(studentId, month, year));
    }

    // Mess manager monthly payment
    @GetMapping("/mess-payment/hall/{hallId}")
    public ResponseEntity<MessManagerPaymentResponse> getMessPayment(
            @PathVariable Long hallId,
            @RequestParam int month,
            @RequestParam int year) {
        return ResponseEntity.ok(service.getMessManagerPayment(hallId, month, year));
    }

    // Staff salary sheet
    @GetMapping("/salary-sheet/hall/{hallId}")
    public ResponseEntity<List<StaffSalaryResponse>> getSalarySheet(
            @PathVariable Long hallId,
            @RequestParam int month,
            @RequestParam int year) {
        return ResponseEntity.ok(service.getSalarySheet(hallId, month, year));
    }

    // Hall occupancy
    @GetMapping("/occupancy/hall/{hallId}")
    public ResponseEntity<OccupancyResponse> getHallOccupancy(@PathVariable Long hallId) {
        return ResponseEntity.ok(service.getHallOccupancy(hallId));
    }

    // Overall occupancy
    @GetMapping("/occupancy")
    public ResponseEntity<List<OccupancyResponse>> getOverallOccupancy() {
        return ResponseEntity.ok(service.getOverallOccupancy());
    }

    // Post ATR
    @PutMapping("/complaints/{complaintId}/atr")
    public ResponseEntity<String> postATR(
            @PathVariable Long complaintId,
            @RequestParam String atr) {
        return ResponseEntity.ok(service.postATR(complaintId, atr));
    }

    // Annual statement
    @GetMapping("/annual-statement/hall/{hallId}")
    public ResponseEntity<AnnualStatementResponse> getAnnualStatement(
            @PathVariable Long hallId,
            @RequestParam int year) {
        return ResponseEntity.ok(service.getAnnualStatement(hallId, year));
    }
}
