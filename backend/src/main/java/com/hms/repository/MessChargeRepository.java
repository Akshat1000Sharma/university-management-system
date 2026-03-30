package com.hms.repository;

import com.hms.entity.MessCharge;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MessChargeRepository extends JpaRepository<MessCharge, Long> {
    List<MessCharge> findByStudentId(Long studentId);
    List<MessCharge> findByHallIdAndMonthAndYear(Long hallId, int month, int year);
}
