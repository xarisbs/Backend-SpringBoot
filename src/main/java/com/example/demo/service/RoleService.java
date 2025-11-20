package com.example.demo.service;

import com.example.demo.entity.Role;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface RoleService {
    List<Role> findAll();
    Optional<Role> findById(Long id);
    Optional<Role> findByName(String name);
    Role save(Role role);
    Role update(Long id, Role role);
    void delete(Long id);

    void assignPermissionsToRole(Long roleId, Set<Long> permissionIds);
    void removePermissionsFromRole(Long roleId, Set<Long> permissionIds);

    List<Role> listarRolesConPermisos();
}
