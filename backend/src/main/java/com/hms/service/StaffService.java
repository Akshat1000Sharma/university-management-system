package com.hms.service;

import com.hms.entity.Staff;
import java.util.List;

public interface StaffService {
    Staff create(Staff staff);
    List<Staff> getAll();
    Staff getById(Long id);
    Staff update(Long id, Staff staff);
    void delete(Long id);
    List<Staff> getByHallId(Long hallId);
}
