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
    public ResponseEntity<List<CompanyUserResponseDto>> getAll() {
        return ResponseEntity.ok(companyUserService.getAllDto());
    }

    // ✅ Obtener usuarios por ID de empresa (también como DTO)
    @GetMapping("/company/{companyId}")
    public ResponseEntity<List<CompanyUserResponseDto>> getByCompany(@PathVariable Long companyId) {
        return ResponseEntity.ok(companyUserService.getByCompanyDto(companyId));
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

    // 🔥 NUEVO ENDPOINT
    @PutMapping("/{id}/assign-supervisor/{supervisorId}")
    public CompanyUserResponseDto assignSupervisor(
            @PathVariable Long id,
            @PathVariable Long supervisorId
    ) {
        return companyUserService.assignSupervisor(id, supervisorId);
    }

    // 🔥 Para poner supervisor en null
    @PutMapping("/{id}/clear-supervisor")
    public CompanyUserResponseDto clearSupervisor(@PathVariable Long id) {
        return companyUserService.assignSupervisor(id, null);
    }
}
