package com.hms.service;

import com.hms.entity.Grant;
import java.util.List;

public interface GrantService {
    Grant create(Grant grant);
    List<Grant> getAll();
    Grant getById(Long id);
    Grant update(Long id, Grant grant);
    void delete(Long id);
}
