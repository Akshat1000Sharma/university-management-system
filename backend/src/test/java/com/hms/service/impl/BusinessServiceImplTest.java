package com.hms.service.impl;

import com.hms.dto.*;
import com.hms.entity.*;
import com.hms.enums.ComplaintStatus;
import com.hms.enums.ComplaintType;
import com.hms.enums.RoomType;
import com.hms.enums.StaffType;
import com.hms.exception.ResourceNotFoundException;
import com.hms.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BusinessServiceImplTest {

    @Mock private StudentRepository studentRepo;
    @Mock private RoomRepository roomRepo;
    @Mock private HallRepository hallRepo;
    @Mock private MessChargeRepository messChargeRepo;
    @Mock private MessManagerRepository messManagerRepo;
    @Mock private ComplaintRepository complaintRepo;
    @Mock private StaffRepository staffRepo;
    @Mock private StaffLeaveRepository staffLeaveRepo;
    @Mock private HallGrantRepository hallGrantRepo;
    @Mock private ExpenditureRepository expenditureRepo;
    @InjectMocks private BusinessServiceImpl service;

    private Room testRoom;
    private Hall testHall;
    private Student testStudent;

    @BeforeEach
    void setUp() {
        testHall = new Hall(1L, "Tagore Hall", false, 500.0);
        testRoom = new Room(1L, "101", RoomType.SINGLE, 3000.0, 1L, false);
        testStudent = new Student(1L, "John Doe", "Address", "1234567890", null, null, null, null, 1L, 1L);
    }

    // --- admitStudent ---

    @Test
    @DisplayName("admitStudent: should create student and mark room occupied")
    void admitStudent_success() {
        AdmitStudentRequest request = new AdmitStudentRequest("John", "Addr", "123", null, null, null, null, 1L, 1L);
        when(roomRepo.findById(1L)).thenReturn(Optional.of(testRoom));
        when(studentRepo.save(any(Student.class))).thenAnswer(inv -> {
            Student s = inv.getArgument(0);
            s.setId(1L);
            return s;
        });
        when(roomRepo.save(any(Room.class))).thenReturn(testRoom);
        when(hallRepo.findById(1L)).thenReturn(Optional.of(testHall));

        StudentDueResponse response = service.admitStudent(request);

        assertNotNull(response);
        assertEquals(1L, response.getStudentId());
        assertEquals("John", response.getStudentName());
        assertEquals(0, response.getMessCharges());
        assertEquals(3000.0, response.getRoomRent());
        assertEquals(500.0, response.getAmenityCharge());
        assertEquals(3500.0, response.getTotalDue());
        verify(roomRepo).save(argThat(Room::isOccupied));
    }

    @Test
    @DisplayName("admitStudent: non-existent room should throw ResourceNotFoundException")
    void admitStudent_roomNotFound() {
        AdmitStudentRequest request = new AdmitStudentRequest("John", "Addr", "123", null, null, null, null, 1L, 99L);
        when(roomRepo.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.admitStudent(request));
    }

    @Test
    @DisplayName("admitStudent: occupied room should throw RuntimeException")
    void admitStudent_roomOccupied() {
        testRoom.setOccupied(true);
        AdmitStudentRequest request = new AdmitStudentRequest("John", "Addr", "123", null, null, null, null, 1L, 1L);
        when(roomRepo.findById(1L)).thenReturn(Optional.of(testRoom));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.admitStudent(request));
        assertEquals("Room is already occupied", ex.getMessage());
    }

    @Test
    @DisplayName("admitStudent: should pick first vacant room when roomId is null")
    void admitStudent_autoAssignsVacantRoom() {
        AdmitStudentRequest request = new AdmitStudentRequest("John", "Addr", "123", null, null, null, null, 1L, null);
        when(hallRepo.findById(1L)).thenReturn(Optional.of(testHall));
        when(roomRepo.findByHallId(1L)).thenReturn(List.of(testRoom));
        when(studentRepo.save(any(Student.class))).thenAnswer(inv -> {
            Student s = inv.getArgument(0);
            s.setId(1L);
            return s;
        });
        when(roomRepo.save(any(Room.class))).thenReturn(testRoom);

        StudentDueResponse response = service.admitStudent(request);

        assertNotNull(response);
        assertEquals(1L, response.getStudentId());
        verify(roomRepo, never()).findById(any());
        verify(roomRepo).findByHallId(1L);
        verify(roomRepo).save(argThat(Room::isOccupied));
    }

    // --- getStudentDues ---

    @Test
    @DisplayName("getStudentDues: should calculate total dues with mess charges")
    void getStudentDues_withMessCharges() {
        when(studentRepo.findById(1L)).thenReturn(Optional.of(testStudent));
        when(roomRepo.findById(1L)).thenReturn(Optional.of(testRoom));
        when(hallRepo.findById(1L)).thenReturn(Optional.of(testHall));

        MessCharge mc1 = new MessCharge(1L, 1L, 1L, 3, 2026, 2000.0);
        MessCharge mc2 = new MessCharge(2L, 1L, 1L, 3, 2026, 1500.0);
        MessCharge mcOtherMonth = new MessCharge(3L, 1L, 1L, 4, 2026, 1000.0);
        when(messChargeRepo.findByStudentId(1L)).thenReturn(List.of(mc1, mc2, mcOtherMonth));

        StudentDueResponse response = service.getStudentDues(1L, 3, 2026);

        assertEquals(1L, response.getStudentId());
        assertEquals(3500.0, response.getMessCharges());
        assertEquals(3000.0, response.getRoomRent());
        assertEquals(500.0, response.getAmenityCharge());
        assertEquals(7000.0, response.getTotalDue());
    }

    @Test
    @DisplayName("getStudentDues: student not found should throw")
    void getStudentDues_studentNotFound() {
        when(studentRepo.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.getStudentDues(99L, 3, 2026));
    }

    @Test
    @DisplayName("getStudentDues: no mess charges should only include rent and amenity")
    void getStudentDues_noMessCharges() {
        when(studentRepo.findById(1L)).thenReturn(Optional.of(testStudent));
        when(roomRepo.findById(1L)).thenReturn(Optional.of(testRoom));
        when(hallRepo.findById(1L)).thenReturn(Optional.of(testHall));
        when(messChargeRepo.findByStudentId(1L)).thenReturn(Collections.emptyList());

        StudentDueResponse response = service.getStudentDues(1L, 3, 2026);
        assertEquals(0, response.getMessCharges());
        assertEquals(3500.0, response.getTotalDue());
    }

    // --- getMessManagerPayment ---

    @Test
    @DisplayName("getMessManagerPayment: should aggregate charges and find manager")
    void getMessManagerPayment_success() {
        when(hallRepo.findById(1L)).thenReturn(Optional.of(testHall));
        MessCharge mc = new MessCharge(1L, 1L, 1L, 3, 2026, 2000.0);
        when(messChargeRepo.findByHallIdAndMonthAndYear(1L, 3, 2026)).thenReturn(List.of(mc));
        MessManager manager = new MessManager(1L, "Manager One", 1L);
        when(messManagerRepo.findAll()).thenReturn(List.of(manager));

        MessManagerPaymentResponse response = service.getMessManagerPayment(1L, 3, 2026);

        assertEquals(1L, response.getHallId());
        assertEquals("Tagore Hall", response.getHallName());
        assertEquals("Manager One", response.getMessManagerName());
        assertEquals(2000.0, response.getTotalAmount());
    }

    @Test
    @DisplayName("getMessManagerPayment: no manager returns N/A")
    void getMessManagerPayment_noManager() {
        when(hallRepo.findById(1L)).thenReturn(Optional.of(testHall));
        when(messChargeRepo.findByHallIdAndMonthAndYear(1L, 3, 2026)).thenReturn(Collections.emptyList());
        when(messManagerRepo.findAll()).thenReturn(Collections.emptyList());

        MessManagerPaymentResponse response = service.getMessManagerPayment(1L, 3, 2026);
        assertEquals("N/A", response.getMessManagerName());
        assertNull(response.getMessManagerId());
    }

    // --- getSalarySheet ---

    @Test
    @DisplayName("getSalarySheet: calculates salary with leave deductions")
    void getSalarySheet_withLeaves() {
        Staff staff = new Staff(1L, "Ramesh", StaffType.ATTENDANT, 500.0, 1L);
        when(staffRepo.findByHallId(1L)).thenReturn(List.of(staff));

        StaffLeave leave1 = new StaffLeave(1L, 1L, LocalDate.of(2026, 3, 5));
        StaffLeave leave2 = new StaffLeave(2L, 1L, LocalDate.of(2026, 3, 10));
        when(staffLeaveRepo.findByStaffIdAndLeaveDateBetween(eq(1L), any(), any()))
                .thenReturn(List.of(leave1, leave2));

        List<StaffSalaryResponse> result = service.getSalarySheet(1L, 3, 2026);

        assertEquals(1, result.size());
        StaffSalaryResponse sr = result.get(0);
        assertEquals(31, sr.getTotalDays());
        assertEquals(2, sr.getLeaveDays());
        assertEquals(29, sr.getWorkingDays());
        assertEquals(14500.0, sr.getSalary());
    }

    @Test
    @DisplayName("getSalarySheet: no staff returns empty list")
    void getSalarySheet_noStaff() {
        when(staffRepo.findByHallId(1L)).thenReturn(Collections.emptyList());
        List<StaffSalaryResponse> result = service.getSalarySheet(1L, 1, 2026);
        assertTrue(result.isEmpty());
    }

    // --- getHallOccupancy ---

    @Test
    @DisplayName("getHallOccupancy: calculates correct occupancy percentages")
    void getHallOccupancy_mixed() {
        when(hallRepo.findById(1L)).thenReturn(Optional.of(testHall));
        Room r1 = new Room(1L, "101", RoomType.SINGLE, 3000, 1L, true);
        Room r2 = new Room(2L, "102", RoomType.SINGLE, 3000, 1L, false);
        Room r3 = new Room(3L, "103", RoomType.TWIN_SHARING, 2000, 1L, true);
        when(roomRepo.findByHallId(1L)).thenReturn(List.of(r1, r2, r3));

        OccupancyResponse response = service.getHallOccupancy(1L);

        assertEquals(3, response.getTotalRooms());
        assertEquals(2, response.getOccupiedRooms());
        assertEquals(1, response.getVacantRooms());
        assertEquals(66.67, response.getOccupancyPercent());
    }

    @Test
    @DisplayName("getHallOccupancy: zero rooms gives 0% occupancy")
    void getHallOccupancy_noRooms() {
        when(hallRepo.findById(1L)).thenReturn(Optional.of(testHall));
        when(roomRepo.findByHallId(1L)).thenReturn(Collections.emptyList());

        OccupancyResponse response = service.getHallOccupancy(1L);
        assertEquals(0, response.getTotalRooms());
        assertEquals(0.0, response.getOccupancyPercent());
    }

    // --- getOverallOccupancy ---

    @Test
    @DisplayName("getOverallOccupancy: returns occupancy for all halls")
    void getOverallOccupancy_multipleHalls() {
        Hall hall2 = new Hall(2L, "Raman Hall", true, 600.0);
        when(hallRepo.findAll()).thenReturn(List.of(testHall, hall2));
        when(hallRepo.findById(1L)).thenReturn(Optional.of(testHall));
        when(hallRepo.findById(2L)).thenReturn(Optional.of(hall2));
        when(roomRepo.findByHallId(1L)).thenReturn(Collections.emptyList());
        when(roomRepo.findByHallId(2L)).thenReturn(Collections.emptyList());

        List<OccupancyResponse> result = service.getOverallOccupancy();
        assertEquals(2, result.size());
    }

    // --- postATR ---

    @Test
    @DisplayName("postATR: should set ATR and mark complaint as RESOLVED")
    void postATR_success() {
        Complaint complaint = new Complaint(1L, 1L, 1L, ComplaintType.FUSED_LIGHT, "Light fused", ComplaintStatus.PENDING, null);
        when(complaintRepo.findById(1L)).thenReturn(Optional.of(complaint));
        when(complaintRepo.save(any())).thenReturn(complaint);

        String result = service.postATR(1L, "Fixed the light");

        assertTrue(result.contains("1"));
        verify(complaintRepo).save(argThat(c ->
                "Fixed the light".equals(c.getAtr()) && c.getStatus() == ComplaintStatus.RESOLVED));
    }

    @Test
    @DisplayName("postATR: complaint not found should throw")
    void postATR_notFound() {
        when(complaintRepo.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.postATR(99L, "ATR"));
    }

    // --- getAnnualStatement ---

    @Test
    @DisplayName("getAnnualStatement: aggregates grants, salaries, and expenditures")
    void getAnnualStatement_success() {
        when(hallRepo.findById(1L)).thenReturn(Optional.of(testHall));
        HallGrant hg = new HallGrant(1L, 1L, 1L, 100000.0, 0);
        when(hallGrantRepo.findByHallId(1L)).thenReturn(List.of(hg));
        when(staffRepo.findByHallId(1L)).thenReturn(Collections.emptyList());
        Expenditure exp = new Expenditure(1L, 1L, "Repair", 5000.0, LocalDate.of(2026, 6, 1), null);
        when(expenditureRepo.findByHallId(1L)).thenReturn(List.of(exp));

        AnnualStatementResponse response = service.getAnnualStatement(1L, 2026);

        assertEquals(100000.0, response.getGrantAllocated());
        assertEquals(0.0, response.getTotalSalariesPaid());
        assertEquals(5000.0, response.getTotalExpenditures());
        assertEquals(95000.0, response.getTotalBalance());
    }

    @Test
    @DisplayName("getAnnualStatement: expenditures from other years excluded")
    void getAnnualStatement_filtersExpendituresByYear() {
        when(hallRepo.findById(1L)).thenReturn(Optional.of(testHall));
        when(hallGrantRepo.findByHallId(1L)).thenReturn(Collections.emptyList());
        when(staffRepo.findByHallId(1L)).thenReturn(Collections.emptyList());
        Expenditure exp2025 = new Expenditure(1L, 1L, "Old repair", 5000.0, LocalDate.of(2025, 6, 1), null);
        Expenditure exp2026 = new Expenditure(2L, 1L, "New repair", 3000.0, LocalDate.of(2026, 3, 1), null);
        when(expenditureRepo.findByHallId(1L)).thenReturn(List.of(exp2025, exp2026));

        AnnualStatementResponse response = service.getAnnualStatement(1L, 2026);
        assertEquals(3000.0, response.getTotalExpenditures());
    }
}
