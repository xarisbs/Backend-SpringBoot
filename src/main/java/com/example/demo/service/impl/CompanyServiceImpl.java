package com.example.demo.service.impl;

import com.example.demo.entity.Company;
import com.example.demo.repository.CompanyRepository;
import com.example.demo.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class CompanyServiceImpl implements CompanyService {

    @Autowired
    private CompanyRepository companyRepository;

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
}
