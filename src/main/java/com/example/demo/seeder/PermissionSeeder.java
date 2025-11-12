package com.example.demo.seeder;

import com.example.demo.entity.Permission;
import com.example.demo.entity.Role;
import com.example.demo.repository.PermissionRepository;
import com.example.demo.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
@Order(2)
@RequiredArgsConstructor
public class PermissionSeeder implements CommandLineRunner {

    private final PermissionRepository permissionRepository;
    private final RoleRepository roleRepository;

    @Override
    public void run(String... args) {
        if (permissionRepository.count() == 0) {
            List<Permission> permissions = Arrays.asList(
                    Permission.builder().name("admin.home").section("Estadística").description("Ver dashboard").build(),
                    Permission.builder().name("admin.manage.profile").section("Configuración").description("Administrar perfil personal").build(),
                    Permission.builder().name("admin.roles").section("Roles").description("Ver listado de roles").build(),
                    Permission.builder().name("admin.roles.create").section("Roles").description("Crear roles").build(),
                    Permission.builder().name("admin.roles.edit").section("Roles").description("Editar roles").build(),
                    Permission.builder().name("admin.roles.assign-permission").section("Roles").description("Asignar permisos al rol").build(),
                    Permission.builder().name("admin.users").section("Usuarios").description("Ver listado de usuarios").build(),
                    Permission.builder().name("admin.users.show").section("Usuarios").description("Ver detalle del usuario").build(),
                    Permission.builder().name("admin.users.create").section("Usuarios").description("Crear usuarios").build(),
                    Permission.builder().name("admin.users.edit").section("Usuarios").description("Editar usuarios").build(),
                    Permission.builder().name("admin.users.delete").section("Usuarios").description("Eliminar usuarios").build(),
                    Permission.builder().name("admin.users.assign-role").section("Usuarios").description("Asignar roles al usuario").build(),
                    Permission.builder().name("admin.monitor").section("Monitorear").description("Monitorear PPP").build(),
                    Permission.builder().name("admin.register-ppp").section("Registrar").description("Registrar PPP").build(),
                    Permission.builder().name("admin.supervisor-ppp").section("Supervisor").description("Supervisor PPP").build(),
                    Permission.builder().name("admin.evaluation.ppp").section("Evaluación").description("Evaluación PPP").build()
            );

            permissionRepository.saveAll(permissions);
            System.out.println("✅ Permisos insertados correctamente (" + permissions.size() + ").");
        } else {
            System.out.println("ℹ️ Los permisos ya existen, no se insertaron nuevamente.");
        }

        // 🔹 Obtener todos los permisos existentes
        List<Permission> allPermissions = permissionRepository.findAll();

        // 🔹 Buscar roles existentes
        Role superAdmin = roleRepository.findByName("ROLE_SUPER_ADMIN").orElseThrow();
        Role admin = roleRepository.findByName("ROLE_ADMIN").orElseThrow();
        Role secretario = roleRepository.findByName("ROLE_SECRETARIO").orElseThrow();
        Role docente = roleRepository.findByName("ROLE_DOCENTE").orElseThrow();
        Role estudiante = roleRepository.findByName("ROLE_ESTUDIANTE").orElseThrow();

        // 🔹 SUPER ADMIN → todos los permisos
        superAdmin.setPermissions(new HashSet<>(allPermissions));

        // 🔹 ADMIN → todos menos los de asignación de roles/permisos
        Set<Permission> adminPermissions = allPermissions.stream()
                .filter(p -> !p.getName().contains("assign"))
                .collect(Collectors.toSet());
        admin.setPermissions(adminPermissions);

        // 🔹 SECRETARIO → puede ver, crear y editar usuarios y monitorear PPP
        Set<Permission> secretarioPermissions = allPermissions.stream()
                .filter(p -> p.getName().contains("admin.users") ||
                        p.getName().equals("admin.monitor") ||
                        p.getName().equals("admin.register-ppp"))
                .collect(Collectors.toSet());
        secretario.setPermissions(secretarioPermissions);

        // 🔹 DOCENTE → permisos de supervisión y evaluación PPP
        Set<Permission> docentePermissions = allPermissions.stream()
                .filter(p -> p.getName().contains("supervisor-ppp") ||
                        p.getName().contains("evaluation.ppp"))
                .collect(Collectors.toSet());
        docente.setPermissions(docentePermissions);

        // 🔹 ESTUDIANTE → solo ver dashboard y gestionar su perfil
        Set<Permission> estudiantePermissions = allPermissions.stream()
                .filter(p -> p.getName().equals("admin.home") ||
                        p.getName().equals("admin.manage.profile"))
                .collect(Collectors.toSet());
        estudiante.setPermissions(estudiantePermissions);

        // 🔹 Guardar roles actualizados
        roleRepository.saveAll(Arrays.asList(superAdmin, admin, secretario, docente, estudiante));

        System.out.println("✅ Permisos asignados correctamente a cada rol.");
    }
}
