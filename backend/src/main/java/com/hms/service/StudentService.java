package com.hms.service;

import com.hms.entity.Student;
import com.hms.entity.User;

import java.util.List;

public interface StudentService {
    Student create(Student student);
    List<Student> getAll();
    Student getById(Long id);
    Student update(Long id, Student student);
    void delete(Long id);
    List<Student> getByHallId(Long hallId);

    /** Assign or change a student's room; updates {@code Room#isOccupied} for old and new rooms. */
    Student selectRoom(Long studentId, Long roomId, User currentUser);
}
