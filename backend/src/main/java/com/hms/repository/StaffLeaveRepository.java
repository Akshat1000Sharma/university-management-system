package com.hms.repository;

import com.hms.entity.StaffLeave;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface StaffLeaveRepository extends JpaRepository<StaffLeave, Long> {
    List<StaffLeave> findByStaffId(Long staffId);
    List<StaffLeave> findByStaffIdAndLeaveDateBetween(Long staffId, LocalDate start, LocalDate end);
}
