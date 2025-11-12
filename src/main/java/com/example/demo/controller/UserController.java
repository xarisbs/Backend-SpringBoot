package com.example.demo.controller;

import com.example.demo.dto.AuthUserDto;
import com.example.demo.entity.AuthUser;
import com.example.demo.service.AuthUserService;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final AuthUserService authUserService;

    public UserController(AuthUserService authUserService) {
        this.authUserService = authUserService;
    }

    @GetMapping()
    public ResponseEntity<List<AuthUser>> listar() {
        return ResponseEntity.ok().body(authUserService.list());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuthUser> buscarPorId(@PathVariable(required = true) Integer id) {
        return authUserService.listarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // CREAR USUARIO
    @PostMapping
    public ResponseEntity<AuthUser> crear(@RequestBody AuthUserDto user) {
        AuthUser creado = authUserService.save(user); // o crear(user) según tu servicio
        // location opcional
        return ResponseEntity.created(URI.create("/auth/" + creado.getId())).body(creado);
    }

    // ACTUALIZAR USUARIO (PUT)
    @PutMapping("/{id}")
    public ResponseEntity<AuthUser> actualizar(
            @PathVariable Integer id,
            @RequestBody AuthUserDto user
    ) {
        // suponer que authUserService.update lanza excepción si no existe
        AuthUser actualizado = authUserService.update(id, user);
        return ResponseEntity.ok(actualizado);
    }

    // ELIMINAR USUARIO
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@RequestHeader("Authorization") String token, @PathVariable Integer id) {
        String cleanToken = token.replace("Bearer ", "");
        authUserService.logout(cleanToken);
        authUserService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // 👇 Nuevo endpoint para asignar roles al usuario
    @PostMapping("/{id}/roles")
    public ResponseEntity<String> assignRoles(
            @PathVariable Long id,
            @RequestBody Map<String, Set<Long>> request
    ) {
        Set<Long> roleIds = request.get("roleIds");
        authUserService.assignRolesToUser(id, roleIds);
        return ResponseEntity.ok("Roles asignados correctamente al usuario con ID " + id);
    }

    // 👇 Endpoint para eliminar roles del usuario
    @DeleteMapping("/{id}/roles")
    public ResponseEntity<String> removeRoles(
            @PathVariable Long id,
            @RequestBody Map<String, Set<Long>> request
    ) {
        Set<Long> roleIds = request.get("roleIds");
        authUserService.removeRolesFromUser(id, roleIds);
        return ResponseEntity.ok("Roles eliminados correctamente del usuario con ID " + id);
    }
}