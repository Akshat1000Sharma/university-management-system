package com.hms.service;

import com.hms.entity.Expenditure;
import java.util.List;

public interface ExpenditureService {
    Expenditure create(Expenditure expenditure);
    List<Expenditure> getAll();
    Expenditure getById(Long id);
    Expenditure update(Long id, Expenditure expenditure);
    void delete(Long id);
    List<Expenditure> getByHallId(Long hallId);
}
