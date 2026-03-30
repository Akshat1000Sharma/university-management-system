package com.hms.controller;

import com.hms.entity.MessManager;
import com.hms.service.MessManagerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/mess-managers")
public class MessManagerController {

    private final MessManagerService service;

    public MessManagerController(MessManagerService service) { this.service = service; }

    @PostMapping
    public ResponseEntity<MessManager> create(@RequestBody MessManager m) {
        return ResponseEntity.ok(service.create(m));
    }

    @GetMapping
    public ResponseEntity<List<MessManager>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MessManager> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MessManager> update(@PathVariable Long id, @RequestBody MessManager m) {
        return ResponseEntity.ok(service.update(id, m));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok("MessManager deleted");
    }
}
