package com.hms.service;

import com.hms.entity.Hall;
import java.util.List;

public interface HallService {
    Hall create(Hall hall);
    List<Hall> getAll();
    Hall getById(Long id);
    Hall update(Long id, Hall hall);
    void delete(Long id);
}
