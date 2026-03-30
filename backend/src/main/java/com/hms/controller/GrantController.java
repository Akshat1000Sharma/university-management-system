package com.hms.controller;

import com.hms.entity.Grant;
import com.hms.service.GrantService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/grants")
public class GrantController {

    private final GrantService service;

    public GrantController(GrantService service) { this.service = service; }

    @PostMapping
    public ResponseEntity<Grant> create(@RequestBody Grant grant) {
        return ResponseEntity.ok(service.create(grant));
    }

    @GetMapping
    public ResponseEntity<List<Grant>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Grant> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Grant> update(@PathVariable Long id, @RequestBody Grant grant) {
        return ResponseEntity.ok(service.update(id, grant));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok("Grant deleted");
    }
}
