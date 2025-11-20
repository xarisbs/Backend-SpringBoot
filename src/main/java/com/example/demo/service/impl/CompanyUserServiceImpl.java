package com.example.demo.service.impl;

import com.example.demo.dto.*;
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

    @Override
    public CompanyUserResponseDto create(CompanyUserRequestDto dto) {
        // Buscar entidades por ID
        AuthUser user = authUserRepository.findById(dto.getAuthUserId().intValue())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Company company = companyRepository.findById(dto.getCompanyId())
                .orElseThrow(() -> new RuntimeException("Empresa no encontrada"));

        AuthUser supervisor = null;

        if (dto.getSupervisorId() != null && dto.getSupervisorId() > 0) {
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

    @Override
    public CompanyUserResponseDto assignSupervisor(Long companyUserId, Long supervisorId) {

        CompanyUser companyUser = companyUserRepository.findById(companyUserId)
                .orElseThrow(() -> new RuntimeException("CompanyUser no encontrado"));

        // Supervisor puede ser null (para limpiar supervisor)
        AuthUser supervisor = null;
        if (supervisorId != null && supervisorId > 0) {
            supervisor = authUserRepository.findById(supervisorId.intValue())
                    .orElseThrow(() -> new RuntimeException("Supervisor no encontrado"));
        }

        companyUser.setSupervisor(supervisor);
        companyUserRepository.save(companyUser);

        return mapToResponseDto(companyUser);
    }

    // -------------------------------
    //      MAPPER DTO → RESPONSE
    // -------------------------------
    private CompanyUserResponseDto mapToResponseDto(CompanyUser cu) {

        SimpleUserDto userDto = new SimpleUserDto(
                (long) cu.getAuthUser().getId(),
                cu.getAuthUser().getNombres(),
                cu.getAuthUser().getApellidos()
        );

        SimpleCompanyDto companyDto = new SimpleCompanyDto(
                cu.getCompany().getId(),
                cu.getCompany().getRazonSocial(),
                cu.getCompany().getRuc()
        );

        SimpleUserDto supervisorDto = null;
        if (cu.getSupervisor() != null) {
            supervisorDto = new SimpleUserDto(
                    (long) cu.getSupervisor().getId(),
                    cu.getSupervisor().getNombres(),
                    cu.getSupervisor().getApellidos()
            );
        }

        return new CompanyUserResponseDto(
                cu.getId(),
                userDto,
                companyDto,
                supervisorDto
        );
    }

    // -------------------------------
    //      DTO METHODS
    // -------------------------------
    @Override
    public List<CompanyUserResponseDto> getAllDto() {
        return companyUserRepository.findAll()
                .stream()
                .map(this::mapToResponseDto)
                .toList();
    }

    @Override
    public List<CompanyUserResponseDto> findByCompanyDto(Long companyId) {
        return companyUserRepository.findByCompanyId(companyId)
                .stream()
                .map(this::mapToResponseDto)
                .toList();
    }

    @Override
    public List<CompanyUserResponseDto> getByCompanyDto(Long companyId) {

        return companyUserRepository.findByCompanyId(companyId)
                .stream()
                .map(this::mapToResponseDto)
                .toList();
    }
}
