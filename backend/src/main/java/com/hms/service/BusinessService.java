package com.hms.service;

import com.hms.dto.*;
import java.util.List;

public interface BusinessService {

    // Admit student and allot room
    StudentDueResponse admitStudent(AdmitStudentRequest request);

    // Compute total dues for a student
    StudentDueResponse getStudentDues(Long studentId, int month, int year);

    // Mess manager monthly payment sheet
    MessManagerPaymentResponse getMessManagerPayment(Long hallId, int month, int year);

    // Staff salary sheet for a hall
    List<StaffSalaryResponse> getSalarySheet(Long hallId, int month, int year);

    // Room occupancy per hall
    OccupancyResponse getHallOccupancy(Long hallId);

    // Overall room occupancy
    List<OccupancyResponse> getOverallOccupancy();

    // Post ATR on complaint
    String postATR(Long complaintId, String atr);

    // Annual statement of accounts
    AnnualStatementResponse getAnnualStatement(Long hallId, int year);
}
