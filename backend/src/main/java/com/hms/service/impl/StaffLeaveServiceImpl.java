package com.hms.service.impl;

import com.hms.entity.StaffLeave;
import com.hms.exception.ResourceNotFoundException;
import com.hms.repository.StaffLeaveRepository;
import com.hms.service.StaffLeaveService;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class StaffLeaveServiceImpl implements StaffLeaveService {

    private final StaffLeaveRepository repo;

    public StaffLeaveServiceImpl(StaffLeaveRepository repo) {
        this.repo = repo;
    }

    public StaffLeave create(StaffLeave sl) { return repo.save(sl); }
    public List<StaffLeave> getAll() { return repo.findAll(); }
    public StaffLeave getById(Long id) {
        return repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("StaffLeave not found"));
    }
    public StaffLeave update(Long id, StaffLeave sl) {
        StaffLeave existing = getById(id);
        existing.setStaffId(sl.getStaffId());
        existing.setLeaveDate(sl.getLeaveDate());
        return repo.save(existing);
    }
    public void delete(Long id) { repo.deleteById(id); }
    public List<StaffLeave> getByStaffId(Long staffId) { return repo.findByStaffId(staffId); }
}
