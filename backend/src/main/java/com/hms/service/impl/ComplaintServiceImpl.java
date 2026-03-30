package com.hms.service.impl;

import com.hms.entity.Complaint;
import com.hms.exception.ResourceNotFoundException;
import com.hms.repository.ComplaintRepository;
import com.hms.service.ComplaintService;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ComplaintServiceImpl implements ComplaintService {

    private final ComplaintRepository repo;

    public ComplaintServiceImpl(ComplaintRepository repo) {
        this.repo = repo;
    }

    public Complaint create(Complaint c) { return repo.save(c); }
    public List<Complaint> getAll() { return repo.findAll(); }
    public Complaint getById(Long id) {
        return repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Complaint not found"));
    }
    public Complaint update(Long id, Complaint c) {
        Complaint existing = getById(id);
        existing.setStudentId(c.getStudentId());
        existing.setHallId(c.getHallId());
        existing.setType(c.getType());
        existing.setDescription(c.getDescription());
        existing.setStatus(c.getStatus());
        existing.setAtr(c.getAtr());
        return repo.save(existing);
    }
    public void delete(Long id) { repo.deleteById(id); }
    public List<Complaint> getByHallId(Long hallId) { return repo.findByHallId(hallId); }
    public List<Complaint> getByStudentId(Long studentId) { return repo.findByStudentId(studentId); }
}
