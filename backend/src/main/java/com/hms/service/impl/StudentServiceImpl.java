package com.hms.service.impl;

import com.hms.entity.Student;
import com.hms.exception.ResourceNotFoundException;
import com.hms.repository.StudentRepository;
import com.hms.service.StudentService;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {

    private final StudentRepository repo;

    public StudentServiceImpl(StudentRepository repo) {
        this.repo = repo;
    }

    public Student create(Student student) { return repo.save(student); }
    public List<Student> getAll() { return repo.findAll(); }
    public Student getById(Long id) {
        return repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Student not found"));
    }
    public Student update(Long id, Student student) {
        Student existing = getById(id);
        existing.setName(student.getName());
        existing.setAddress(student.getAddress());
        existing.setPhone(student.getPhone());
        existing.setPhoto(student.getPhoto());
        existing.setHallId(student.getHallId());
        existing.setRoomId(student.getRoomId());
        return repo.save(existing);
    }
    public void delete(Long id) { repo.deleteById(id); }
    public List<Student> getByHallId(Long hallId) { return repo.findByHallId(hallId); }
}
