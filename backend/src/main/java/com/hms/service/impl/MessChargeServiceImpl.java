package com.hms.service.impl;

import com.hms.entity.MessCharge;
import com.hms.exception.ResourceNotFoundException;
import com.hms.repository.MessChargeRepository;
import com.hms.service.MessChargeService;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class MessChargeServiceImpl implements MessChargeService {

    private final MessChargeRepository repo;

    public MessChargeServiceImpl(MessChargeRepository repo) {
        this.repo = repo;
    }

    public MessCharge create(MessCharge mc) { return repo.save(mc); }
    public List<MessCharge> getAll() { return repo.findAll(); }
    public MessCharge getById(Long id) {
        return repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("MessCharge not found"));
    }
    public MessCharge update(Long id, MessCharge mc) {
        MessCharge existing = getById(id);
        existing.setStudentId(mc.getStudentId());
        existing.setHallId(mc.getHallId());
        existing.setMonth(mc.getMonth());
        existing.setYear(mc.getYear());
        existing.setAmount(mc.getAmount());
        return repo.save(existing);
    }
    public void delete(Long id) { repo.deleteById(id); }
    public List<MessCharge> getByStudentId(Long studentId) { return repo.findByStudentId(studentId); }
    public List<MessCharge> getByHallIdAndMonthAndYear(Long hallId, int month, int year) {
        return repo.findByHallIdAndMonthAndYear(hallId, month, year);
    }
}
