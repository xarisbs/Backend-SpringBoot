package com.example.demo.controller;

import com.example.demo.dto.RequestDTO;
import com.example.demo.service.RequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/requests")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class RequestController {

    private final RequestService service;

    @PostMapping
    public RequestDTO create(@RequestBody RequestDTO dto) {
        return service.create(dto);
    }

    @PutMapping("/{id}")
    public RequestDTO update(@PathVariable Long id, @RequestBody RequestDTO dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    @GetMapping("/{id}")
    public RequestDTO findById(@PathVariable Long id) {
        return service.findById(id);
    }

    @GetMapping
    public List<RequestDTO> findAll() {
        return service.findAll();
    }
}
