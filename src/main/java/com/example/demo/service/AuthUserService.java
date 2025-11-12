package com.example.demo.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.example.demo.dto.AuthUserDto;
import com.example.demo.dto.LoginResponseDto;
import com.example.demo.entity.AuthUser;
import com.example.demo.dto.TokenDto;

public interface AuthUserService {

    public List<AuthUser> list();

    public Optional<AuthUser> listarPorId(Integer id);

    public AuthUser save(AuthUserDto authUserDto);

    // public TokenDto login(AuthUserDto authUserDto);

    LoginResponseDto login(AuthUserDto authUserDto);

    public void logout(String token);

    public TokenDto validate(String token);

    AuthUser update(Integer id, AuthUserDto authUserDto);

    void delete(Integer id);

    void assignRolesToUser(Long userId, Set<Long> roleIds);
    void removeRolesFromUser(Long userId, Set<Long> roleIds);
}