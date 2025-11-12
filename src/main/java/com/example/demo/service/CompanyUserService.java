package com.example.demo.service;

import com.example.demo.dto.CompanyUserRequestDto;
import com.example.demo.dto.CompanyUserResponseDto;
import com.example.demo.entity.CompanyUser;
import java.util.List;
import java.util.Optional;

public interface CompanyUserService {
    List<CompanyUser> findAll();
    Optional<CompanyUser> findById(Long id);
    List<CompanyUser> findByCompany(Long companyId);
    CompanyUser save(CompanyUser companyUser);
    // Crear una relación a partir del DTO de request y devolver el DTO de respuesta
    CompanyUserResponseDto create(CompanyUserRequestDto requestDto);
    void delete(Long id);
}
