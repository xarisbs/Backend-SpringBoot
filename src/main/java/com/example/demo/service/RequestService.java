package com.example.demo.service;

import com.example.demo.dto.RequestDTO;

import java.util.List;

public interface RequestService {

    RequestDTO create(RequestDTO dto);

    RequestDTO update(Long id, RequestDTO dto);

    void delete(Long id);

    RequestDTO findById(Long id);

    List<RequestDTO> findAll();
}
