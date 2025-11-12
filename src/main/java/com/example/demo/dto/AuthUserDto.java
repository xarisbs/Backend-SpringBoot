package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthUserDto {
    private String userName;
    private String password;
    private String nombres;
    private String apellidos;
    private String codigoUniversitario;
    private String dni;
    private String correo;
}