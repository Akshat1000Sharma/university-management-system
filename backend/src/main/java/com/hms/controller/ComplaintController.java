package com.hms.controller;

import com.hms.entity.Complaint;
import com.hms.service.ComplaintService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/complaints")
public class ComplaintController {

    private final ComplaintService service;

    public ComplaintController(ComplaintService service) { this.service = service; }

    @PostMapping
    public ResponseEntity<Complaint> create(@RequestBody Complaint c) {
        return ResponseEntity.ok(service.create(c));
    }

    @GetMapping
    public ResponseEntity<List<Complaint>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Complaint> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Complaint> update(@PathVariable Long id, @RequestBody Complaint c) {
        return ResponseEntity.ok(service.update(id, c));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok("Complaint deleted");
    }

    @GetMapping("/hall/{hallId}")
    public ResponseEntity<List<Complaint>> getByHallId(@PathVariable Long hallId) {
        return ResponseEntity.ok(service.getByHallId(hallId));
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<Complaint>> getByStudentId(@PathVariable Long studentId) {
        return ResponseEntity.ok(service.getByStudentId(studentId));
    }
}
