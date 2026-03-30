package com.hms.controller;

import com.hms.entity.MessCharge;
import com.hms.service.MessChargeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/mess-charges")
public class MessChargeController {

    private final MessChargeService service;

    public MessChargeController(MessChargeService service) { this.service = service; }

    @PostMapping
    public ResponseEntity<MessCharge> create(@RequestBody MessCharge mc) {
        return ResponseEntity.ok(service.create(mc));
    }

    @GetMapping
    public ResponseEntity<List<MessCharge>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MessCharge> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MessCharge> update(@PathVariable Long id, @RequestBody MessCharge mc) {
        return ResponseEntity.ok(service.update(id, mc));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok("MessCharge deleted");
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<MessCharge>> getByStudentId(@PathVariable Long studentId) {
        return ResponseEntity.ok(service.getByStudentId(studentId));
    }

    @GetMapping("/hall/{hallId}")
    public ResponseEntity<List<MessCharge>> getByHallMonthYear(
            @PathVariable Long hallId,
            @RequestParam int month,
            @RequestParam int year) {
        return ResponseEntity.ok(service.getByHallIdAndMonthAndYear(hallId, month, year));
    }
}
