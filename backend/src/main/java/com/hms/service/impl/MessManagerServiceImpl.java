package com.hms.service.impl;

import com.hms.entity.MessManager;
import com.hms.exception.ResourceNotFoundException;
import com.hms.repository.MessManagerRepository;
import com.hms.service.MessManagerService;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class MessManagerServiceImpl implements MessManagerService {

    private final MessManagerRepository repo;

    public MessManagerServiceImpl(MessManagerRepository repo) {
        this.repo = repo;
    }

    public MessManager create(MessManager m) { return repo.save(m); }
    public List<MessManager> getAll() { return repo.findAll(); }
    public MessManager getById(Long id) {
        return repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("MessManager not found"));
    }
    public MessManager update(Long id, MessManager m) {
        MessManager existing = getById(id);
        existing.setName(m.getName());
        existing.setHallId(m.getHallId());
        return repo.save(existing);
    }
    public void delete(Long id) { repo.deleteById(id); }
}
