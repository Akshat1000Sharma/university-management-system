package com.hms.service;

import com.hms.entity.MessCharge;
import java.util.List;

public interface MessChargeService {
    MessCharge create(MessCharge messCharge);
    List<MessCharge> getAll();
    MessCharge getById(Long id);
    MessCharge update(Long id, MessCharge messCharge);
    void delete(Long id);
    List<MessCharge> getByStudentId(Long studentId);
    List<MessCharge> getByHallIdAndMonthAndYear(Long hallId, int month, int year);
}
