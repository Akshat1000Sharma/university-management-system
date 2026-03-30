package com.hms.service;

import com.hms.entity.HallGrant;
import java.util.List;

public interface HallGrantService {
    HallGrant create(HallGrant hallGrant);
    List<HallGrant> getAll();
    HallGrant getById(Long id);
    HallGrant update(Long id, HallGrant hallGrant);
    void delete(Long id);
    List<HallGrant> getByGrantId(Long grantId);
    List<HallGrant> getByHallId(Long hallId);
}
