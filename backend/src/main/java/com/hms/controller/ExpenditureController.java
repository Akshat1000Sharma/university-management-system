package com.hms.controller;

import com.hms.entity.Expenditure;
import com.hms.service.ExpenditureService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/expenditures")
public class ExpenditureController {

    private final ExpenditureService service;

    public ExpenditureController(ExpenditureService service) { this.service = service; }

    @PostMapping
    public ResponseEntity<Expenditure> create(@RequestBody Expenditure e) {
        return ResponseEntity.ok(service.create(e));
    }

    @GetMapping
    public ResponseEntity<List<Expenditure>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Expenditure> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Expenditure> update(@PathVariable Long id, @RequestBody Expenditure e) {
        return ResponseEntity.ok(service.update(id, e));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok("Expenditure deleted");
    }

    @GetMapping("/hall/{hallId}")
    public ResponseEntity<List<Expenditure>> getByHallId(@PathVariable Long hallId) {
        return ResponseEntity.ok(service.getByHallId(hallId));
    }
}
