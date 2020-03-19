package com.example.springdemo.repositories;

import com.example.springdemo.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    @Query("SELECT p FROM Role p WHERE p.roleName=:name")
    Role getRoleByName(@Param("name") String name);
}
