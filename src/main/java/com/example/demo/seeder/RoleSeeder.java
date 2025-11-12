package com.example.demo.seeder;

import com.example.demo.entity.Role;
import com.example.demo.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(1)
public class RoleSeeder implements CommandLineRunner {

    private final RoleRepository roleRepository;

    public RoleSeeder(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) {
        if (roleRepository.findByName("ROLE_SUPER_ADMIN").isEmpty()) {
            roleRepository.save(new Role("ROLE_SUPER_ADMIN"));
        }

        if (roleRepository.findByName("ROLE_ADMIN").isEmpty()) {
            roleRepository.save(new Role("ROLE_ADMIN"));
        }

        if (roleRepository.findByName("ROLE_SECRETARIO").isEmpty()) {
            roleRepository.save(new Role("ROLE_SECRETARIO"));
        }

        if (roleRepository.findByName("ROLE_DOCENTE").isEmpty()) {
            roleRepository.save(new Role("ROLE_DOCENTE"));
        }

        if (roleRepository.findByName("ROLE_ESTUDIANTE").isEmpty()) {
            roleRepository.save(new Role("ROLE_ESTUDIANTE"));
        }

        System.out.println("✅ Roles creados/verificados correctamente al iniciar la aplicación");
    }
}
