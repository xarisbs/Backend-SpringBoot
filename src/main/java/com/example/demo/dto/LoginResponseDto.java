package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDto {
    private String token;
    private Integer id;
    private String userName;
    private String nombres;
    private String apellidos;
    private String correo;
    private Set<String> roles;
}
