package com.hms.controller;

import com.hms.entity.Hall;
import com.hms.service.HallService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/halls")
public class HallController {

    private final HallService service;

    public HallController(HallService service) { this.service = service; }

    @PostMapping
    public ResponseEntity<Hall> create(@RequestBody Hall hall) {
        return ResponseEntity.ok(service.create(hall));
    }

    @GetMapping
    public ResponseEntity<List<Hall>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Hall> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Hall> update(@PathVariable Long id, @RequestBody Hall hall) {
        return ResponseEntity.ok(service.update(id, hall));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok("Hall deleted");
    }
}
