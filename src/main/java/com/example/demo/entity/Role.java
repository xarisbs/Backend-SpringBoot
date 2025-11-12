package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name; // ej: "ADMIN", "USER"

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "role_permissions", // tabla intermedia
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id")
    )
    private Set<Permission> permissions = new HashSet<>();

    // 👇 Constructor auxiliar
    public Role(String name) {
        this.name = name;
    }
}
