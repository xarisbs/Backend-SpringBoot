package com.example.demo.seeder;

import com.example.demo.entity.AuthUser;
import com.example.demo.entity.Role;
import com.example.demo.repository.AuthRepository;
import com.example.demo.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Component
@Order(3)
public class UserSeeder implements CommandLineRunner {

    private final AuthRepository authRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserSeeder(AuthRepository authRepository,
                      RoleRepository roleRepository,
                      PasswordEncoder passwordEncoder) {
        this.authRepository = authRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {

        crearUsuarioSiNoExiste(
                "superadmin",
                "superadmin123",
                "Super",
                "Administrador",
                "202300001",
                "70000001",
                "superadmin@gmail.com",
                "ROLE_SUPER_ADMIN"
        );

        crearUsuarioSiNoExiste(
                "admin",
                "admin123",
                "Administrador",
                "Administrador",
                "202300002",
                "70000002",
                "admin@gmail.com",
                "ROLE_ADMIN"
        );

        crearUsuarioSiNoExiste(
                "secretario",
                "secretario123",
                "Secretario",
                "Secretario",
                "202300003",
                "70000003",
                "secretario@gmail.com",
                "ROLE_SECRETARIO"
        );

        crearUsuarioSiNoExiste(
                "docente",
                "docente123",
                "Docente",
                "Docente",
                "202300004",
                "70000004",
                "docente@gmail.com",
                "ROLE_DOCENTE"
        );

        crearUsuarioSiNoExiste(
                "X@ris123",
                "123xaris",
                "Xaris",
                "Quispe Mamani",
                "202300005",
                "70000005",
                "xaris@gmail.com",
                "ROLE_ESTUDIANTE"
        );
    }

    private void crearUsuarioSiNoExiste(String username, String password, String nombres, String apellidos,
                                        String codigoUniversitario, String dni, String correo, String roleName) {

        Optional<AuthUser> existingUser = authRepository.findByUserName(username);

        if (existingUser.isEmpty()) {
            Role role = roleRepository.findByName(roleName)
                    .orElseThrow(() -> new RuntimeException("⚠️ El rol " + roleName + " no existe. Ejecuta primero el RoleSeeder."));

            Set<Role> roles = new HashSet<>();
            roles.add(role);

            AuthUser user = AuthUser.builder()
                    .userName(username)
                    .password(passwordEncoder.encode(password))
                    .nombres(nombres)
                    .apellidos(apellidos)
                    .codigoUniversitario(codigoUniversitario)
                    .dni(dni)
                    .correo(correo)
                    .roles(roles)
                    .build();

            authRepository.save(user);

            System.out.println("✅ Usuario creado correctamente: " + username + " con rol " + roleName);
        } else {
            System.out.println("ℹ️ El usuario " + username + " ya existe, no se creó nuevamente.");
        }
    }
}
