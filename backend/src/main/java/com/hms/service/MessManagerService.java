package com.hms.service;

import com.hms.entity.MessManager;
import java.util.List;

public interface MessManagerService {
    MessManager create(MessManager messManager);
    List<MessManager> getAll();
    MessManager getById(Long id);
    MessManager update(Long id, MessManager messManager);
    void delete(Long id);
}
