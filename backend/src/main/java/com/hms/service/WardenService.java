package com.hms.service;

import com.hms.entity.Warden;
import java.util.List;

public interface WardenService {
    Warden create(Warden warden);
    List<Warden> getAll();
    Warden getById(Long id);
    Warden update(Long id, Warden warden);
    void delete(Long id);
}
