package com.example.demo.service.impl;

import com.example.demo.dto.*;
import com.example.demo.entity.Company;
import com.example.demo.repository.CompanyRepository;
import com.example.demo.service.CompanyService;
import com.example.demo.service.CompanyUserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class CompanyServiceImpl implements CompanyService {

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private CompanyUserService companyUserService;

    @Override
    public List<Company> findAll() {
        return companyRepository.findAll();
    }

    @Override
    public Optional<Company> findById(Long id) {
        return companyRepository.findById(id);
    }

    @Override
    public Company save(Company company) {
        return companyRepository.save(company);
    }

    @Override
    public Company update(Long id, Company company) {
        return companyRepository.findById(id).map(existing -> {
            existing.setRazonSocial(company.getRazonSocial());
            existing.setRuc(company.getRuc());
            existing.setDireccionFiscal(company.getDireccionFiscal());
            existing.setResponsable(company.getResponsable());
            return companyRepository.save(existing);
        }).orElseThrow(() -> new RuntimeException("Company not found"));
    }

    @Override
    public void delete(Long id) {
        companyRepository.deleteById(id);
    }

    @Override
    @Transactional
    public CompanyRegistrationResponseDto register(CompanyRegistrationRequestDto dto) {

        // 1. Registrar la compañía
        Company company = new Company();
        company.setRazonSocial(dto.getCompany().getRazonSocial());
        company.setRuc(dto.getCompany().getRuc());
        company.setDireccionFiscal(dto.getCompany().getDireccionFiscal());
        company.setResponsable(dto.getCompany().getResponsable());

        Company savedCompany = companyRepository.save(company);

        // 2. Registrar usuarios
        CompanyUserResponseDto lastUser = null;

        for (CompanyUserRequestDto userDto : dto.getUsers()) {
            userDto.setCompanyId(savedCompany.getId());
            lastUser = companyUserService.create(userDto);
        }

        // 3. Construcción de respuesta
        SimpleCompanyDto companyDto = new SimpleCompanyDto(
                savedCompany.getId(),
                savedCompany.getRazonSocial(),
                savedCompany.getRuc()
        );

        return new CompanyRegistrationResponseDto(companyDto, lastUser);
    }
}
