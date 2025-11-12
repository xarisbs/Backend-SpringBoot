package com.example.demo.repository;

import com.example.demo.dto.SimpleCompanyDto;
import com.example.demo.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {
    Company findByRuc(String ruc);

    Optional<Object> findById(SimpleCompanyDto company);
}
