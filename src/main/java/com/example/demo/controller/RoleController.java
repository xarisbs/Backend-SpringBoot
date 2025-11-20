package com.example.demo.controller;

import com.example.demo.entity.Role;
import com.example.demo.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @GetMapping
    public ResponseEntity<List<Role>> getAllRoles() {
        return ResponseEntity.ok(roleService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Role> getRoleById(@PathVariable Long id) {
        return roleService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Role> createRole(@RequestBody Role role) {
        return ResponseEntity.ok(roleService.save(role));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Role> updateRole(@PathVariable Long id, @RequestBody Role role) {
        return ResponseEntity.ok(roleService.update(id, role));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRole(@PathVariable Long id) {
        roleService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // 👇 Asignar uno o más permisos a un rol
    @PostMapping("/{id}/permissions")
    public ResponseEntity<String> assignPermissions(
            @PathVariable Long id,
            @RequestBody Map<String, Set<Long>> request
    ) {
        Set<Long> permissionIds = request.get("permissionIds");
        roleService.assignPermissionsToRole(id, permissionIds);
        return ResponseEntity.ok("Permisos asignados correctamente al rol con ID " + id);
    }

    // 👇 Quitar uno o más permisos de un rol
    @DeleteMapping("/{id}/permissions")
    public ResponseEntity<String> removePermissions(
            @PathVariable Long id,
            @RequestBody Map<String, Set<Long>> request
    ) {
        Set<Long> permissionIds = request.get("permissionIds");
        roleService.removePermissionsFromRole(id, permissionIds);
        return ResponseEntity.ok("Permisos eliminados correctamente del rol con ID " + id);
    }

    @GetMapping("/listar-permisos")
    public List<Role> listarRolesConPermisos() {
        return roleService.listarRolesConPermisos();
    }
}
