package com.example.demo.controller;

import com.example.demo.dto.CompanyUserRequestDto;
import com.example.demo.dto.CompanyUserResponseDto;
import com.example.demo.entity.CompanyUser;
import com.example.demo.service.CompanyUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/company-users")
@CrossOrigin(origins = "*")
public class CompanyUserController {

    @Autowired
    private CompanyUserService companyUserService;

    @GetMapping
    public ResponseEntity<List<CompanyUser>> getAll() {
        return ResponseEntity.ok(companyUserService.findAll());
    }

    @GetMapping("/company/{companyId}")
    public ResponseEntity<List<CompanyUser>> getByCompany(@PathVariable Long companyId) {
        return ResponseEntity.ok(companyUserService.findByCompany(companyId));
    }

    @PostMapping
    public ResponseEntity<CompanyUserResponseDto> create(@RequestBody CompanyUserRequestDto dto) {
        CompanyUserResponseDto response = companyUserService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        companyUserService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
