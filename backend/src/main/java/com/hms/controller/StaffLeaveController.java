package com.hms.controller;

import com.hms.entity.StaffLeave;
import com.hms.service.StaffLeaveService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/staff-leaves")
public class StaffLeaveController {

    private final StaffLeaveService service;

    public StaffLeaveController(StaffLeaveService service) { this.service = service; }

    @PostMapping
    public ResponseEntity<StaffLeave> create(@RequestBody StaffLeave sl) {
        return ResponseEntity.ok(service.create(sl));
    }

    @GetMapping
    public ResponseEntity<List<StaffLeave>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<StaffLeave> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<StaffLeave> update(@PathVariable Long id, @RequestBody StaffLeave sl) {
        return ResponseEntity.ok(service.update(id, sl));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok("StaffLeave deleted");
    }

    @GetMapping("/staff/{staffId}")
    public ResponseEntity<List<StaffLeave>> getByStaffId(@PathVariable Long staffId) {
        return ResponseEntity.ok(service.getByStaffId(staffId));
    }
}
