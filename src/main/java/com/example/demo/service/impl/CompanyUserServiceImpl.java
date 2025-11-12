package com.example.demo.service.impl;

import com.example.demo.dto.CompanyUserRequestDto;
import com.example.demo.dto.CompanyUserResponseDto;
import com.example.demo.dto.SimpleCompanyDto;
import com.example.demo.dto.SimpleUserDto;
import com.example.demo.entity.AuthUser;
import com.example.demo.entity.Company;
import com.example.demo.entity.CompanyUser;
import com.example.demo.repository.AuthRepository;
import com.example.demo.repository.CompanyRepository;
import com.example.demo.repository.CompanyUserRepository;
import com.example.demo.service.CompanyUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class CompanyUserServiceImpl implements CompanyUserService {

    @Autowired
    private CompanyUserRepository companyUserRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    AuthRepository authUserRepository;

    @Override
    public List<CompanyUser> findAll() {
        return companyUserRepository.findAll();
    }

    @Override
    public Optional<CompanyUser> findById(Long id) {
        return companyUserRepository.findById(id);
    }

    @Override
    public List<CompanyUser> findByCompany(Long companyId) {
        return companyUserRepository.findByCompanyId(companyId);
    }

    @Override
    public CompanyUser save(CompanyUser companyUser) {
        return companyUserRepository.save(companyUser);
    }

    @Override
    public void delete(Long id) {
        companyUserRepository.deleteById(id);
    }

    private CompanyUserResponseDto mapToResponseDto(CompanyUser companyUser) {
        SimpleUserDto userDto = new SimpleUserDto(
                (long) companyUser.getAuthUser().getId(),
                companyUser.getAuthUser().getNombres(),
                companyUser.getAuthUser().getApellidos()
        );

        SimpleCompanyDto companyDto = new SimpleCompanyDto(
                companyUser.getCompany().getId(),
                companyUser.getCompany().getRazonSocial(),
                companyUser.getCompany().getRuc()
        );

        SimpleUserDto supervisorDto = null;
        if (companyUser.getSupervisor() != null) {
            supervisorDto = new SimpleUserDto(
                    (long) companyUser.getSupervisor().getId(),
                    companyUser.getSupervisor().getNombres(),
                    companyUser.getSupervisor().getApellidos()
            );
        }

        return new CompanyUserResponseDto(
                companyUser.getId(),
                userDto,
                companyDto,
                supervisorDto
        );
    }

    @Override
    public CompanyUserResponseDto create(CompanyUserRequestDto dto) {
        // Buscar entidades por ID
        AuthUser user = authUserRepository.findById(dto.getAuthUserId().intValue())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Company company = companyRepository.findById(dto.getCompanyId())
                .orElseThrow(() -> new RuntimeException("Empresa no encontrada"));

        AuthUser supervisor = null;
        if (dto.getSupervisorId() != null) {
            supervisor = authUserRepository.findById(dto.getSupervisorId().intValue())
                    .orElseThrow(() -> new RuntimeException("Supervisor no encontrado"));
        }

        // Crear entidad
        CompanyUser companyUser = new CompanyUser();
        companyUser.setAuthUser(user);
        companyUser.setCompany(company);
        companyUser.setSupervisor(supervisor);

        companyUserRepository.save(companyUser);

        // Retornar DTO de respuesta limpio
        return mapToResponseDto(companyUser);
    }
}
