package com.example.demo.service.impl;

import com.example.demo.entity.Permission;
import com.example.demo.entity.Role;
import com.example.demo.repository.PermissionRepository;
import com.example.demo.repository.RoleRepository;
import com.example.demo.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    @Override
    public List<Role> findAll() {
        return roleRepository.findAll();
    }

    @Override
    public Optional<Role> findById(Long id) {
        return roleRepository.findById(Math.toIntExact(id));
    }

    @Override
    public Optional<Role> findByName(String name) {
        return roleRepository.findByName(name);
    }

    @Override
    public Role save(Role role) {
        if (roleRepository.existsByName(role.getName())) {
            throw new IllegalArgumentException("Ya existe un rol con ese nombre");
        }
        return roleRepository.save(role);
    }

    @Override
    public Role update(Long id, Role roleDetails) {
        Role role = roleRepository.findById(Math.toIntExact(id))
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));
        role.setName(roleDetails.getName());
        role.setPermissions(roleDetails.getPermissions());
        return roleRepository.save(role);
    }

    @Override
    public void delete(Long id) {
        if (!roleRepository.existsById(Math.toIntExact(id))) {
            throw new RuntimeException("Rol no encontrado");
        }
        roleRepository.deleteById(Math.toIntExact(id));
    }

    @Override
    public void assignPermissionsToRole(Long roleId, Set<Long> permissionIds) {
        Role role = roleRepository.findById(Math.toIntExact(roleId))
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));
        Set<Permission> permissions = permissionIds.stream()
                .map(id -> permissionRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Permiso no encontrado con ID: " + id)))
                .collect(Collectors.toSet());
        role.getPermissions().addAll(permissions);
        roleRepository.save(role);
    }

    @Override
    public void removePermissionsFromRole(Long roleId, Set<Long> permissionIds) {
        Role role = roleRepository.findById(Math.toIntExact(roleId))
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));
        Set<Permission> permissionsToRemove = permissionIds.stream()
                .map(id -> permissionRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Permiso no encontrado con ID: " + id)))
                .collect(Collectors.toSet());
        role.getPermissions().removeAll(permissionsToRemove);
        roleRepository.save(role);
    }

    @Override
    public List<Role> listarRolesConPermisos() {
        return roleRepository.findAllWithPermissions();
    }
}
