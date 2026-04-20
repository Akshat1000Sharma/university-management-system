package com.hms.controller;

import com.hms.dto.SelectRoomRequest;
import com.hms.entity.Student;
import com.hms.entity.User;
import com.hms.service.StudentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService service;

    public StudentController(StudentService service) { this.service = service; }

    @PostMapping
    public ResponseEntity<Student> create(@RequestBody Student student) {
        return ResponseEntity.ok(service.create(student));
    }

    @GetMapping
    public ResponseEntity<List<Student>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Student> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Student> update(@PathVariable Long id, @RequestBody Student student) {
        return ResponseEntity.ok(service.update(id, student));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok("Student deleted");
    }

    @GetMapping("/hall/{hallId}")
    public ResponseEntity<List<Student>> getByHallId(@PathVariable Long hallId) {
        return ResponseEntity.ok(service.getByHallId(hallId));
    }

    /**
     * Student (self), warden (same hall), or HMC chairman may assign a vacant room in the student's hall.
     * Twin sharing vs single is informational in this model; each room still holds at most one student.
     */
    @PostMapping("/{id}/select-room")
    public ResponseEntity<Student> selectRoom(
            @PathVariable Long id,
            @RequestBody SelectRoomRequest body,
            Authentication authentication) {
        if (authentication == null || !(authentication.getPrincipal() instanceof User user)) {
            return ResponseEntity.status(401).build();
        }
        return ResponseEntity.ok(service.selectRoom(id, body.getRoomId(), user));
    }
}
