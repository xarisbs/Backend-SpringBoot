package com.example.demo.repository;

import com.example.demo.dto.SimpleUserDto;
import com.example.demo.entity.AuthUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthRepository extends JpaRepository<AuthUser, Integer> {
    Optional<AuthUser> findByUserName(String userName);

    Optional<Object> findById(SimpleUserDto authUser);
}