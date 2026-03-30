package com.hms.controller;

import com.hms.entity.HallGrant;
import com.hms.service.HallGrantService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/hall-grants")
public class HallGrantController {

    private final HallGrantService service;

    public HallGrantController(HallGrantService service) { this.service = service; }

    @PostMapping
    public ResponseEntity<HallGrant> create(@RequestBody HallGrant hg) {
        return ResponseEntity.ok(service.create(hg));
    }

    @GetMapping
    public ResponseEntity<List<HallGrant>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<HallGrant> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<HallGrant> update(@PathVariable Long id, @RequestBody HallGrant hg) {
        return ResponseEntity.ok(service.update(id, hg));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok("HallGrant deleted");
    }

    @GetMapping("/grant/{grantId}")
    public ResponseEntity<List<HallGrant>> getByGrantId(@PathVariable Long grantId) {
        return ResponseEntity.ok(service.getByGrantId(grantId));
    }

    @GetMapping("/hall/{hallId}")
    public ResponseEntity<List<HallGrant>> getByHallId(@PathVariable Long hallId) {
        return ResponseEntity.ok(service.getByHallId(hallId));
    }
}
