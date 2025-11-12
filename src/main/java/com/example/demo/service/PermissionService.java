package com.example.demo.service;

import com.example.demo.entity.Permission;

import java.util.List;
import java.util.Optional;

public interface PermissionService {
    List<Permission> findAll();
    Optional<Permission> findById(Long id);
    Optional<Permission> findByName(String name);
    Permission save(Permission permission);
    Permission update(Long id, Permission permission);
    void delete(Long id);
}
