package com.hms.repository;

import com.hms.entity.Permission;
import com.hms.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PermissionRepository extends JpaRepository<Permission, Long> {
    List<Permission> findByRole(UserRole role);
}
