package com.hms.service.impl;

import com.hms.entity.Grant;
import com.hms.exception.ResourceNotFoundException;
import com.hms.repository.GrantRepository;
import com.hms.service.GrantService;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class GrantServiceImpl implements GrantService {

    private final GrantRepository repo;

    public GrantServiceImpl(GrantRepository repo) {
        this.repo = repo;
    }

    public Grant create(Grant g) { return repo.save(g); }
    public List<Grant> getAll() { return repo.findAll(); }
    public Grant getById(Long id) {
        return repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Grant not found"));
    }
    public Grant update(Long id, Grant g) {
        Grant existing = getById(id);
        existing.setYear(g.getYear());
        existing.setTotalAmount(g.getTotalAmount());
        return repo.save(existing);
    }
    public void delete(Long id) { repo.deleteById(id); }
}
