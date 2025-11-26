package com.example.demo.controller;

import com.example.demo.dto.AuthUserDto;
import com.example.demo.dto.LoginResponseDto;
import com.example.demo.entity.AuthUser;
import com.example.demo.dto.TokenDto;
import com.example.demo.service.AuthUserService;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthUserController {
    private final AuthUserService authUserService;

    public AuthUserController(AuthUserService authUserService) {
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

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody AuthUserDto authUserDto) {
        try {
            LoginResponseDto loginResponse = authUserService.login(authUserDto);
            return ResponseEntity.ok(loginResponse);
        } catch (RuntimeException e) {
            // Si hay error en usuario o contraseña
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(null);
        } catch (Exception e) {
            // Por si ocurre un error inesperado
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    @PostMapping("/student/login")
    public ResponseEntity<LoginResponseDto> studentLogin(@RequestBody AuthUserDto authUserDto) {
        try {
            LoginResponseDto loginResponse = authUserService.studentLogin(authUserDto);
            return ResponseEntity.ok(loginResponse);
        } catch (RuntimeException e) {
            // Si hay error en usuario, contraseña o rol
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(null);
        } catch (Exception e) {
            // Por si ocurre un error inesperado
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    @DeleteMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader("Authorization") String token) {
        authUserService.logout(token);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/validate")
    public ResponseEntity<TokenDto> validate(@RequestParam String token) {
        TokenDto tokenDto = authUserService.validate(token);
        if (tokenDto == null)
            return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(tokenDto);
    }

    @PostMapping("/create")
    public ResponseEntity<AuthUser> create(@RequestBody AuthUserDto authUserDto) {
        AuthUser authUser = authUserService.save(authUserDto);
        return (authUser != null)
                ? ResponseEntity.status(HttpStatus.CREATED).body(authUser)
                : ResponseEntity.badRequest().build();
    }

    // 👇 Nuevo endpoint para asignar roles
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