package com.hms.service;

import com.hms.entity.StaffLeave;
import java.util.List;

public interface StaffLeaveService {
    StaffLeave create(StaffLeave staffLeave);
    List<StaffLeave> getAll();
    StaffLeave getById(Long id);
    StaffLeave update(Long id, StaffLeave staffLeave);
    void delete(Long id);
    List<StaffLeave> getByStaffId(Long staffId);
}
