package com.hms.service.impl;

import com.hms.entity.Warden;
import com.hms.exception.ResourceNotFoundException;
import com.hms.repository.WardenRepository;
import com.hms.service.WardenService;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class WardenServiceImpl implements WardenService {

    private final WardenRepository repo;

    public WardenServiceImpl(WardenRepository repo) {
        this.repo = repo;
    }

    public Warden create(Warden warden) { return repo.save(warden); }
    public List<Warden> getAll() { return repo.findAll(); }
    public Warden getById(Long id) {
        return repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Warden not found"));
    }
    public Warden update(Long id, Warden warden) {
        Warden existing = getById(id);
        existing.setName(warden.getName());
        existing.setContact(warden.getContact());
        existing.setHallId(warden.getHallId());
        existing.setControlling(warden.isControlling());
        return repo.save(existing);
    }
    public void delete(Long id) { repo.deleteById(id); }
}
