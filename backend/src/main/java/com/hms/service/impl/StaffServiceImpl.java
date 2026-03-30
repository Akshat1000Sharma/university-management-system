package com.hms.service.impl;

import com.hms.entity.Staff;
import com.hms.exception.ResourceNotFoundException;
import com.hms.repository.StaffRepository;
import com.hms.service.StaffService;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class StaffServiceImpl implements StaffService {

    private final StaffRepository repo;

    public StaffServiceImpl(StaffRepository repo) {
        this.repo = repo;
    }

    public Staff create(Staff s) { return repo.save(s); }
    public List<Staff> getAll() { return repo.findAll(); }
    public Staff getById(Long id) {
        return repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Staff not found"));
    }
    public Staff update(Long id, Staff s) {
        Staff existing = getById(id);
        existing.setName(s.getName());
        existing.setStaffType(s.getStaffType());
        existing.setDailyPay(s.getDailyPay());
        existing.setHallId(s.getHallId());
        return repo.save(existing);
    }
    public void delete(Long id) { repo.deleteById(id); }
    public List<Staff> getByHallId(Long hallId) { return repo.findByHallId(hallId); }
}
