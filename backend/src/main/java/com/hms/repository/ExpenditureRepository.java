package com.hms.repository;

import com.hms.entity.Expenditure;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ExpenditureRepository extends JpaRepository<Expenditure, Long> {
    List<Expenditure> findByHallId(Long hallId);
}
