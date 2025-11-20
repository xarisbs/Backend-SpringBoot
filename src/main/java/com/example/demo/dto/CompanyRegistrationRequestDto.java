package com.example.demo.dto;

import com.example.demo.entity.Company;
import lombok.Data;

import java.util.List;

@Data
public class CompanyRegistrationRequestDto {
    private Company company;
    private List<CompanyUserRequestDto> users;
}
