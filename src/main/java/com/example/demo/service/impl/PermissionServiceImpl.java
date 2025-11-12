package com.example.demo.service.impl;

import com.example.demo.entity.Permission;
import com.example.demo.repository.PermissionRepository;
import com.example.demo.service.PermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PermissionServiceImpl implements PermissionService {

    private final PermissionRepository permissionRepository;

    @Override
    public List<Permission> findAll() {
        return permissionRepository.findAll();
    }

    @Override
    public Optional<Permission> findById(Long id) {
        return permissionRepository.findById(id);
    }

    @Override
    public Optional<Permission> findByName(String name) {
        return permissionRepository.findByName(name);
    }

    @Override
    public Permission save(Permission permission) {
        if (permissionRepository.existsByName(permission.getName())) {
            throw new IllegalArgumentException("⚠️ Ya existe un permiso con ese nombre");
        }

        // Aseguramos que section y description no sean nulos
        if (permission.getSection() == null || permission.getSection().isBlank()) {
            throw new IllegalArgumentException("La sección del permiso es obligatoria");
        }

        if (permission.getDescription() == null || permission.getDescription().isBlank()) {
            throw new IllegalArgumentException("La descripción del permiso es obligatoria");
        }

        return permissionRepository.save(permission);
    }

    @Override
    public Permission update(Long id, Permission permissionDetails) {
        Permission permission = permissionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Permiso no encontrado"));

        // Actualizamos todos los campos
        permission.setName(permissionDetails.getName());
        permission.setSection(permissionDetails.getSection());
        permission.setDescription(permissionDetails.getDescription());

        return permissionRepository.save(permission);
    }

    @Override
    public void delete(Long id) {
        if (!permissionRepository.existsById(id)) {
            throw new RuntimeException("Permiso no encontrado");
        }
        permissionRepository.deleteById(id);
    }
}
