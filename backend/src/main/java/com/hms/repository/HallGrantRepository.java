package com.hms.repository;

import com.hms.entity.HallGrant;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface HallGrantRepository extends JpaRepository<HallGrant, Long> {
    List<HallGrant> findByGrantId(Long grantId);
    List<HallGrant> findByHallId(Long hallId);
}
