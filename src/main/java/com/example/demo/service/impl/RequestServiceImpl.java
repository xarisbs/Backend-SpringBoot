package com.example.demo.service.impl;

import com.example.demo.dto.RequestDTO;
import com.example.demo.entity.CompanyUser;
import com.example.demo.entity.Request;
import com.example.demo.repository.CompanyUserRepository;
import com.example.demo.repository.RequestRepository;
import com.example.demo.service.RequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {

    private final RequestRepository repository;
    private final CompanyUserRepository companyUserRepository;

    // Convertir entity -> dto
    private RequestDTO toDTO(Request request) {
        return RequestDTO.builder()
                .id(request.getId())
                .companyUserId(request.getCompanyUser().getId())
                .expediente(request.getExpediente())
                .paso(request.getPaso())
                .linea(request.getLinea())
                .estado(request.getEstado())
                .descripcion(request.getDescripcion())
                .build();
    }

    // Convertir dto -> entity
    private Request toEntity(RequestDTO dto, CompanyUser cu) {
        return Request.builder()
                .companyUser(cu)
                .expediente(dto.getExpediente())
                .paso(dto.getPaso())
                .linea(dto.getLinea())
                .estado(dto.getEstado())
                .descripcion(dto.getDescripcion())
                .build();
    }

    @Override
    public RequestDTO create(RequestDTO dto) {

        CompanyUser cu = companyUserRepository.findById(dto.getCompanyUserId())
                .orElseThrow(() -> new RuntimeException("CompanyUser no encontrado"));

        Request request = toEntity(dto, cu);

        return toDTO(repository.save(request));
    }

    @Override
    public RequestDTO update(Long id, RequestDTO dto) {

        Request request = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Request no encontrado"));

        CompanyUser cu = companyUserRepository.findById(dto.getCompanyUserId())
                .orElseThrow(() -> new RuntimeException("CompanyUser no encontrado"));

        request.setCompanyUser(cu);
        request.setExpediente(dto.getExpediente());
        request.setPaso(dto.getPaso());
        request.setLinea(dto.getLinea());
        request.setEstado(dto.getEstado());
        request.setDescripcion(dto.getDescripcion());

        return toDTO(repository.save(request));
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    public RequestDTO findById(Long id) {
        Request request = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Request no encontrado"));
        return toDTO(request);
    }

    @Override
    public List<RequestDTO> findAll() {
        return repository.findAll()
                .stream()
                .map(this::toDTO)
                .toList();
    }
}
