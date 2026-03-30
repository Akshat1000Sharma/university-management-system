package com.hms.service;

import com.hms.entity.Complaint;
import java.util.List;

public interface ComplaintService {
    Complaint create(Complaint complaint);
    List<Complaint> getAll();
    Complaint getById(Long id);
    Complaint update(Long id, Complaint complaint);
    void delete(Long id);
    List<Complaint> getByHallId(Long hallId);
    List<Complaint> getByStudentId(Long studentId);
}
