package com.hms.service;

import com.hms.entity.Payment;
import java.util.List;

public interface PaymentService {
    Payment create(Payment payment);
    List<Payment> getAll();
    Payment getById(Long id);
    Payment update(Long id, Payment payment);
    void delete(Long id);
    List<Payment> getByStudentId(Long studentId);
}
