package com.hms.service.impl;

import com.hms.entity.Expenditure;
import com.hms.exception.ResourceNotFoundException;
import com.hms.repository.ExpenditureRepository;
import com.hms.service.ExpenditureService;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ExpenditureServiceImpl implements ExpenditureService {

    private final ExpenditureRepository repo;

    public ExpenditureServiceImpl(ExpenditureRepository repo) {
        this.repo = repo;
    }

    public Expenditure create(Expenditure e) { return repo.save(e); }
    public List<Expenditure> getAll() { return repo.findAll(); }
    public Expenditure getById(Long id) {
        return repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Expenditure not found"));
    }
    public Expenditure update(Long id, Expenditure e) {
        Expenditure existing = getById(id);
        existing.setHallId(e.getHallId());
        existing.setDescription(e.getDescription());
        existing.setAmount(e.getAmount());
        existing.setDate(e.getDate());
        existing.setCategory(e.getCategory());
        return repo.save(existing);
    }
    public void delete(Long id) { repo.deleteById(id); }
    public List<Expenditure> getByHallId(Long hallId) { return repo.findByHallId(hallId); }
}
