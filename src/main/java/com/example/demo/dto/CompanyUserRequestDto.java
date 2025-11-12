package com.example.demo.dto;

import lombok.Data;

@Data
public class CompanyUserRequestDto {
    private Long authUserId;
    private Long companyId;
    private Long supervisorId; // opcional
}
