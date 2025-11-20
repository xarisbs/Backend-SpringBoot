package com.example.demo.service.impl;

import com.example.demo.dto.AuthUserDto;
import com.example.demo.dto.LoginResponseDto;
import com.example.demo.entity.AuthUser;
import com.example.demo.dto.TokenDto;
import com.example.demo.entity.Role;
import com.example.demo.repository.AuthRepository;
import com.example.demo.repository.RoleRepository;
import com.example.demo.security.JwtProvider;
import com.example.demo.service.AuthUserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthUserServiceImpl implements AuthUserService {
    @Autowired
    AuthRepository authRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    JwtProvider jwtProvider;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public List<AuthUser> list() {
        return authRepository.findAll();
    }

    @Override
    public Optional<AuthUser> listarPorId(Integer id) {
        return authRepository.findById(id);
    }

    @Override
    public AuthUser save(AuthUserDto authUserDto) {
        Optional<AuthUser> user = authRepository.findByUserName(authUserDto.getUserName());
        // Buscar el rol por defecto
        Role defaultRole = roleRepository.findByName("ROLE_ESTUDIANTE")
                .orElseThrow(() -> new RuntimeException("Rol por defecto no encontrado"));
        if (user.isPresent())
            return null;
        String password = passwordEncoder.encode(authUserDto.getPassword());
        AuthUser authUser = AuthUser.builder()
                .userName(authUserDto.getUserName())
                .password(password)
                .nombres(authUserDto.getNombres())
                .apellidos(authUserDto.getApellidos())
                .codigoUniversitario(authUserDto.getCodigoUniversitario())
                .dni(authUserDto.getDni())
                .correo(authUserDto.getCorreo())
                .roles(Set.of(defaultRole))
                .build();

        return authRepository.save(authUser);
    }

    // 🔹 Nuevo método UPDATE
    @Override
    public AuthUser update(Integer id, AuthUserDto authUserDto) {
        AuthUser existingUser = authRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));

        // Actualizar campos básicos
        existingUser.setUserName(authUserDto.getUserName());
        existingUser.setNombres(authUserDto.getNombres());
        existingUser.setApellidos(authUserDto.getApellidos());
        existingUser.setCodigoUniversitario(authUserDto.getCodigoUniversitario());
        existingUser.setDni(authUserDto.getDni());
        existingUser.setCorreo(authUserDto.getCorreo());

        // Verificar si el password realmente cambió
        String newPassword = authUserDto.getPassword();

        if (newPassword != null && !newPassword.isEmpty()) {
            // Si la contraseña enviada NO coincide con la ya guardada (hash)
            if (!passwordEncoder.matches(newPassword, existingUser.getPassword())) {
                existingUser.setPassword(passwordEncoder.encode(newPassword));
            }
            // Si coincide, no se hace nada (ya está cifrada y correcta)
        }

        return authRepository.save(existingUser);
    }

    // 🔹 Nuevo método DELETE
    @Override
    public void delete(Integer id) {
        AuthUser user = authRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // 🔹 Limpiar relación con roles
        user.getRoles().clear();
        authRepository.save(user);

        // 🔹 Ahora eliminar usuario
        authRepository.delete(user);
    }

    @Override
    public LoginResponseDto login(AuthUserDto authUserDto) {
        Optional<AuthUser> userOpt = authRepository.findByUserName(authUserDto.getUserName());
        if (userOpt.isEmpty())
            throw new RuntimeException("Usuario no encontrado");

        AuthUser user = userOpt.get();

        if (!passwordEncoder.matches(authUserDto.getPassword(), user.getPassword()))
            throw new RuntimeException("Contraseña incorrecta");

        String token = jwtProvider.createToken(user);

        // Mapeamos los roles a texto
        Set<String> roles = user.getRoles().stream()
                .map(role -> role.getName())
                .collect(java.util.stream.Collectors.toSet());

        // Extraer permisos desde cada rol
        Set<String> permisos = user.getRoles().stream()
                .flatMap(role -> role.getPermissions().stream())
                .map(p -> p.getName())
                .collect(Collectors.toSet());

        return new LoginResponseDto(
                token,
                user.getId(),
                user.getUserName(),
                user.getNombres(),
                user.getApellidos(),
                user.getCorreo(),
                roles,
                permisos
        );
    }

    @Override
    public void logout(String token) {
        if (jwtProvider.validate(token)) {
            jwtProvider.addToInvalidTokens(token);
            System.out.println("Token invalidado y agregado a la lista de tokens inválidos: " + token);
        } else {
            System.out.println("Token inválido: " + token);
        }
    }

    @Override
    public TokenDto validate(String token) {
        if (!jwtProvider.validate(token))
            return null;
        String username = jwtProvider.getUserNameFromToken(token);
        if (!authRepository.findByUserName(username).isPresent())
            return null;

        return new TokenDto(token);
    }

    // 👇 Método nuevo
    @Override
    public void assignRolesToUser(Long userId, Set<Long> roleIds) {
        AuthUser user = authRepository.findById(Math.toIntExact(userId))
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Set<Role> roles = roleIds.stream()
                .map(roleId -> roleRepository.findById(Math.toIntExact(roleId))
                        .orElseThrow(() -> new RuntimeException("Rol no encontrado con id: " + roleId)))
                .collect(Collectors.toSet());

        user.getRoles().addAll(roles); // asigna los nuevos sin eliminar los anteriores
        authRepository.save(user);
    }

    @Override
    public void removeRolesFromUser(Long userId, Set<Long> roleIds) {
        AuthUser user = authRepository.findById(Math.toIntExact(userId))
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Set<Role> rolesToRemove = roleIds.stream()
                .map(roleId -> roleRepository.findById(Math.toIntExact(roleId))
                        .orElseThrow(() -> new RuntimeException("Rol no encontrado con id: " + roleId)))
                .collect(Collectors.toSet());

        user.getRoles().removeAll(rolesToRemove); // 👈 elimina los roles indicados
        authRepository.save(user);
    }

    @Override
    public List<AuthUser> findUsersByRoleName(String roleName) {
        return authRepository.findByRoles_Name(roleName);
    }

    @Override
    public List<AuthUser> findUsersByRoleIds(Set<Long> roleIds) {
        return authRepository.findDistinctByRoles_IdIn(roleIds);
    }

    @Override
    public List<AuthUser> findUsersWithoutEstudiante() {
        return authRepository.findUsersWithoutRole("ROLE_ESTUDIANTE");
    }

    @Override
    public List<AuthUser> findStudents() {
        return authRepository.findByRoles_Name("ROLE_ESTUDIANTE");
    }
}