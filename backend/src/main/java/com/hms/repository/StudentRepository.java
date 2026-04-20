package com.hms.repository;

import com.hms.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {
    List<Student> findByHallId(Long hallId);

    long countByRoomId(Long roomId);
}
