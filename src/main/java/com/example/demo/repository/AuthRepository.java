package com.example.demo.repository;

import com.example.demo.dto.SimpleUserDto;
import com.example.demo.entity.AuthUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface AuthRepository extends JpaRepository<AuthUser, Integer> {
    Optional<AuthUser> findByUserName(String userName);

    Optional<Object> findById(SimpleUserDto authUser);

    List<AuthUser> findByRoles_Name(String name);

    List<AuthUser> findDistinctByRoles_IdIn(Set<Long> roleIds);

    @Query("""
       SELECT u FROM AuthUser u
       WHERE 'ROLE_ESTUDIANTE' NOT IN 
             (SELECT r.name FROM u.roles r)
       """)
    List<AuthUser> findUsersWithoutRole(String roleName);
}