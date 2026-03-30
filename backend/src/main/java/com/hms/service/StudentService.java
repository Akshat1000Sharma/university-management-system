package com.hms.service;

import com.hms.entity.Student;
import java.util.List;

public interface StudentService {
    Student create(Student student);
    List<Student> getAll();
    Student getById(Long id);
    Student update(Long id, Student student);
    void delete(Long id);
    List<Student> getByHallId(Long hallId);
}
