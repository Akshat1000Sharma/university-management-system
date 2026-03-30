package com.hms.controller;

import com.hms.entity.Warden;
import com.hms.service.WardenService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/wardens")
public class WardenController {

    private final WardenService service;

    public WardenController(WardenService service) { this.service = service; }

    @PostMapping
    public ResponseEntity<Warden> create(@RequestBody Warden warden) {
        return ResponseEntity.ok(service.create(warden));
    }

    @GetMapping
    public ResponseEntity<List<Warden>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Warden> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Warden> update(@PathVariable Long id, @RequestBody Warden warden) {
        return ResponseEntity.ok(service.update(id, warden));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok("Warden deleted");
    }
}
