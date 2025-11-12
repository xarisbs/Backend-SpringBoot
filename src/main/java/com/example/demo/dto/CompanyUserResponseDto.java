package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompanyUserResponseDto {
    private Long id;
    private SimpleUserDto authUser;
    private SimpleCompanyDto company;
    private SimpleUserDto supervisor;
}
