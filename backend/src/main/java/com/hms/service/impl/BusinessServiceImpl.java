package com.hms.service.impl;

import com.hms.dto.*;
import com.hms.entity.*;
import com.hms.enums.ComplaintStatus;
import com.hms.exception.ResourceNotFoundException;
import com.hms.enums.RoomType;
import com.hms.repository.*;
import com.hms.service.BusinessService;
import com.hms.service.RoomOccupancyService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BusinessServiceImpl implements BusinessService {

    private final StudentRepository studentRepo;
    private final RoomRepository roomRepo;
    private final HallRepository hallRepo;
    private final MessChargeRepository messChargeRepo;
    private final MessManagerRepository messManagerRepo;
    private final ComplaintRepository complaintRepo;
    private final StaffRepository staffRepo;
    private final StaffLeaveRepository staffLeaveRepo;
    private final HallGrantRepository hallGrantRepo;
    private final ExpenditureRepository expenditureRepo;
    private final RoomOccupancyService roomOccupancyService;

    public BusinessServiceImpl(StudentRepository studentRepo,
                               RoomRepository roomRepo,
                               HallRepository hallRepo,
                               MessChargeRepository messChargeRepo,
                               MessManagerRepository messManagerRepo,
                               ComplaintRepository complaintRepo,
                               StaffRepository staffRepo,
                               StaffLeaveRepository staffLeaveRepo,
                               HallGrantRepository hallGrantRepo,
                               ExpenditureRepository expenditureRepo,
                               RoomOccupancyService roomOccupancyService) {
        this.studentRepo = studentRepo;
        this.roomRepo = roomRepo;
        this.hallRepo = hallRepo;
        this.messChargeRepo = messChargeRepo;
        this.messManagerRepo = messManagerRepo;
        this.complaintRepo = complaintRepo;
        this.staffRepo = staffRepo;
        this.staffLeaveRepo = staffLeaveRepo;
        this.hallGrantRepo = hallGrantRepo;
        this.expenditureRepo = expenditureRepo;
        this.roomOccupancyService = roomOccupancyService;
    }

    @Override
    public StudentDueResponse admitStudent(AdmitStudentRequest request) {
        Room room = resolveRoomForAdmission(request);

        if (!roomOccupancyService.hasVacancy(room)) {
            throw new RuntimeException("Room has no vacant beds");
        }

        Long hallId = request.getHallId() != null ? request.getHallId() : room.getHallId();
        if (hallId == null) {
            throw new IllegalArgumentException("hallId is required");
        }

        // Create student
        Student student = new Student();
        student.setName(request.getName());
        student.setAddress(request.getAddress());
        student.setPhone(request.getPhone());
        student.setPhoto(request.getPhoto());
        student.setEmail(request.getEmail());
        student.setRegistrationNumber(request.getRegistrationNumber());
        student.setAdmissionDate(request.getAdmissionDate());
        student.setHallId(hallId);
        student.setRoomId(room.getId());
        student = studentRepo.save(student);

        roomOccupancyService.syncOccupiedFlag(room.getId());

        Hall hall = hallRepo.findById(hallId)
                .orElseThrow(() -> new ResourceNotFoundException("Hall not found"));

        StudentDueResponse response = new StudentDueResponse();
        response.setStudentId(student.getId());
        response.setStudentName(student.getName());
        response.setMessCharges(0);
        response.setRoomRent(room.getRent());
        response.setAmenityCharge(hall.getAmenityCharge());
        response.setTotalDue(room.getRent() + hall.getAmenityCharge());
        return response;
    }

    /**
     * Resolves the room for admission: explicit {@link AdmitStudentRequest#getRoomId()}, or the first
     * vacant room in {@link AdmitStudentRequest#getHallId()} when room id is omitted.
     */
    private Room resolveRoomForAdmission(AdmitStudentRequest request) {
        Long roomId = request.getRoomId();
        if (roomId != null) {
            Room room = roomRepo.findById(roomId)
                    .orElseThrow(() -> new ResourceNotFoundException("Room not found"));
            if (request.getHallId() != null && !request.getHallId().equals(room.getHallId())) {
                throw new IllegalArgumentException("Room does not belong to the specified hall");
            }
            return room;
        }

        Long hallId = request.getHallId();
        if (hallId == null) {
            throw new IllegalArgumentException("hallId is required when roomId is not specified");
        }

        hallRepo.findById(hallId)
                .orElseThrow(() -> new ResourceNotFoundException("Hall not found"));

        return roomRepo.findByHallId(hallId).stream()
                .filter(roomOccupancyService::hasVacancy)
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("No vacant room available in this hall"));
    }

    @Override
    public StudentDueResponse getStudentDues(Long studentId, int month, int year) {
        Student student = studentRepo.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));

        Room room = roomRepo.findById(student.getRoomId())
                .orElseThrow(() -> new ResourceNotFoundException("Room not found"));

        Hall hall = hallRepo.findById(student.getHallId())
                .orElseThrow(() -> new ResourceNotFoundException("Hall not found"));

        List<MessCharge> messCharges = messChargeRepo.findByStudentId(studentId);
        double messTotal = messCharges.stream()
                .filter(mc -> mc.getMonth() == month && mc.getYear() == year)
                .mapToDouble(MessCharge::getAmount)
                .sum();

        double totalDue = messTotal + room.getRent() + hall.getAmenityCharge();

        StudentDueResponse response = new StudentDueResponse();
        response.setStudentId(studentId);
        response.setStudentName(student.getName());
        response.setMessCharges(messTotal);
        response.setRoomRent(room.getRent());
        response.setAmenityCharge(hall.getAmenityCharge());
        response.setTotalDue(totalDue);
        return response;
    }

    @Override
    public MessManagerPaymentResponse getMessManagerPayment(Long hallId, int month, int year) {
        Hall hall = hallRepo.findById(hallId)
                .orElseThrow(() -> new ResourceNotFoundException("Hall not found"));

        List<MessCharge> charges = messChargeRepo.findByHallIdAndMonthAndYear(hallId, month, year);
        double total = charges.stream().mapToDouble(MessCharge::getAmount).sum();

        List<MessManager> managers = messManagerRepo.findAll().stream()
                .filter(m -> m.getHallId().equals(hallId))
                .collect(Collectors.toList());

        String managerName = managers.isEmpty() ? "N/A" : managers.get(0).getName();
        Long managerId = managers.isEmpty() ? null : managers.get(0).getId();

        MessManagerPaymentResponse response = new MessManagerPaymentResponse();
        response.setHallId(hallId);
        response.setHallName(hall.getName());
        response.setMessManagerId(managerId);
        response.setMessManagerName(managerName);
        response.setMonth(month);
        response.setYear(year);
        response.setTotalAmount(total);
        return response;
    }

    @Override
    public List<StaffSalaryResponse> getSalarySheet(Long hallId, int month, int year) {
        List<Staff> staffList = staffRepo.findByHallId(hallId);
        YearMonth ym = YearMonth.of(year, month);
        int totalDays = ym.lengthOfMonth();
        LocalDate start = ym.atDay(1);
        LocalDate end = ym.atEndOfMonth();

        List<StaffSalaryResponse> result = new ArrayList<>();

        for (Staff staff : staffList) {
            List<StaffLeave> leaves = staffLeaveRepo
                    .findByStaffIdAndLeaveDateBetween(staff.getId(), start, end);
            int leaveDays = leaves.size();
            int workingDays = totalDays - leaveDays;
            double salary = workingDays * staff.getDailyPay();

            StaffSalaryResponse sr = new StaffSalaryResponse();
            sr.setStaffId(staff.getId());
            sr.setStaffName(staff.getName());
            sr.setStaffType(staff.getStaffType().name());
            sr.setDailyPay(staff.getDailyPay());
            sr.setTotalDays(totalDays);
            sr.setLeaveDays(leaveDays);
            sr.setWorkingDays(workingDays);
            sr.setSalary(salary);
            result.add(sr);
        }
        return result;
    }

    @Override
    public OccupancyResponse getHallOccupancy(Long hallId) {
        Hall hall = hallRepo.findById(hallId)
                .orElseThrow(() -> new ResourceNotFoundException("Hall not found"));

        List<Room> rooms = roomRepo.findByHallId(hallId);
        int total = rooms.size();
        int totalBeds = rooms.stream().mapToInt(r -> r.getRoomType() == RoomType.TWIN_SHARING ? 2 : 1).sum();
        long totalAssigned = rooms.stream().mapToLong(r -> roomOccupancyService.countOccupants(r.getId())).sum();
        int roomsWithResidents = (int) rooms.stream()
                .filter(r -> roomOccupancyService.countOccupants(r.getId()) > 0)
                .count();
        int roomsWithVacancy = (int) rooms.stream()
                .filter(roomOccupancyService::hasVacancy)
                .count();
        double percent = totalBeds == 0 ? 0 : Math.round(totalAssigned * 10000.0 / totalBeds) / 100.0;

        OccupancyResponse response = new OccupancyResponse();
        response.setHallId(hallId);
        response.setHallName(hall.getName());
        response.setTotalRooms(total);
        response.setOccupiedRooms(roomsWithResidents);
        response.setVacantRooms(roomsWithVacancy);
        response.setOccupancyPercent(percent);
        return response;
    }

    @Override
    public List<OccupancyResponse> getOverallOccupancy() {
        List<Hall> halls = hallRepo.findAll();
        List<OccupancyResponse> result = new ArrayList<>();
        for (Hall hall : halls) {
            result.add(getHallOccupancy(hall.getId()));
        }
        return result;
    }

    @Override
    public String postATR(Long complaintId, String atr) {
        Complaint complaint = complaintRepo.findById(complaintId)
                .orElseThrow(() -> new ResourceNotFoundException("Complaint not found"));
        complaint.setAtr(atr);
        complaint.setStatus(ComplaintStatus.RESOLVED);
        complaintRepo.save(complaint);
        return "ATR posted successfully for complaint ID: " + complaintId;
    }

    @Override
    public AnnualStatementResponse getAnnualStatement(Long hallId, int year) {
        Hall hall = hallRepo.findById(hallId)
                .orElseThrow(() -> new ResourceNotFoundException("Hall not found"));

        // Grant allocated
        List<HallGrant> grants = hallGrantRepo.findByHallId(hallId);
        double grantAllocated = grants.stream()
                .mapToDouble(HallGrant::getAllocatedAmount)
                .sum();

        // Total salaries for the year (12 months)
        List<StaffSalaryResponse> allSalaries = new ArrayList<>();
        double totalSalaries = 0;
        for (int m = 1; m <= 12; m++) {
            List<StaffSalaryResponse> monthly = getSalarySheet(hallId, m, year);
            allSalaries.addAll(monthly);
            totalSalaries += monthly.stream().mapToDouble(StaffSalaryResponse::getSalary).sum();
        }

        // Total expenditures
        List<Expenditure> expenditures = expenditureRepo.findByHallId(hallId);
        double totalExpenditure = expenditures.stream()
                .filter(e -> e.getDate() != null && e.getDate().getYear() == year)
                .mapToDouble(Expenditure::getAmount)
                .sum();

        double balance = grantAllocated - totalSalaries - totalExpenditure;

        AnnualStatementResponse response = new AnnualStatementResponse();
        response.setHallId(hallId);
        response.setHallName(hall.getName());
        response.setYear(year);
        response.setGrantAllocated(grantAllocated);
        response.setTotalSalariesPaid(totalSalaries);
        response.setTotalExpenditures(totalExpenditure);
        response.setTotalBalance(balance);
        response.setSalaryDetails(allSalaries);
        return response;
    }
}
