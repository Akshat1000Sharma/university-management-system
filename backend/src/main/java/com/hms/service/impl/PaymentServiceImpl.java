package com.hms.service.impl;

import com.hms.entity.Payment;
import com.hms.exception.ResourceNotFoundException;
import com.hms.repository.PaymentRepository;
import com.hms.service.PaymentService;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository repo;

    public PaymentServiceImpl(PaymentRepository repo) {
        this.repo = repo;
    }

    public Payment create(Payment p) { return repo.save(p); }
    public List<Payment> getAll() { return repo.findAll(); }
    public Payment getById(Long id) {
        return repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Payment not found"));
    }
    public Payment update(Long id, Payment p) {
        Payment existing = getById(id);
        existing.setStudentId(p.getStudentId());
        existing.setAmount(p.getAmount());
        existing.setDate(p.getDate());
        return repo.save(existing);
    }
    public void delete(Long id) { repo.deleteById(id); }
    public List<Payment> getByStudentId(Long studentId) { return repo.findByStudentId(studentId); }
}
