package com.hms.service.impl;

import com.hms.entity.Hall;
import com.hms.exception.ResourceNotFoundException;
import com.hms.repository.HallRepository;
import com.hms.service.HallService;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class HallServiceImpl implements HallService {

    private final HallRepository repo;

    public HallServiceImpl(HallRepository repo) {
        this.repo = repo;
    }

    public Hall create(Hall hall) { return repo.save(hall); }
    public List<Hall> getAll() { return repo.findAll(); }
    public Hall getById(Long id) {
        return repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Hall not found"));
    }
    public Hall update(Long id, Hall hall) {
        Hall existing = getById(id);
        existing.setName(hall.getName());
        existing.setNew(hall.isNew());
        existing.setAmenityCharge(hall.getAmenityCharge());
        return repo.save(existing);
    }
    public void delete(Long id) { repo.deleteById(id); }
}
