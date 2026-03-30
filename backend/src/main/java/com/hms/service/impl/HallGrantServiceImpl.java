package com.hms.service.impl;

import com.hms.entity.HallGrant;
import com.hms.exception.ResourceNotFoundException;
import com.hms.repository.HallGrantRepository;
import com.hms.service.HallGrantService;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class HallGrantServiceImpl implements HallGrantService {

    private final HallGrantRepository repo;

    public HallGrantServiceImpl(HallGrantRepository repo) {
        this.repo = repo;
    }

    public HallGrant create(HallGrant hg) { return repo.save(hg); }
    public List<HallGrant> getAll() { return repo.findAll(); }
    public HallGrant getById(Long id) {
        return repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("HallGrant not found"));
    }
    public HallGrant update(Long id, HallGrant hg) {
        HallGrant existing = getById(id);
        existing.setGrantId(hg.getGrantId());
        existing.setHallId(hg.getHallId());
        existing.setAllocatedAmount(hg.getAllocatedAmount());
        existing.setSpentAmount(hg.getSpentAmount());
        return repo.save(existing);
    }
    public void delete(Long id) { repo.deleteById(id); }
    public List<HallGrant> getByGrantId(Long grantId) { return repo.findByGrantId(grantId); }
    public List<HallGrant> getByHallId(Long hallId) { return repo.findByHallId(hallId); }
}
