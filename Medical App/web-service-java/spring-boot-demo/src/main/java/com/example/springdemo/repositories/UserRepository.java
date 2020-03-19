package com.example.springdemo.repositories;

import com.example.springdemo.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    @Query(value = "SELECT u " +
            "FROM User u " +
            "WHERE u.username=:username")
    User getUserByUsername (@Param("username")String username);

    @Query(value = "SELECT u " +
            "FROM User u " +
            "WHERE u.role.roleName=:roleName")
    List<User> getAllUsersByRole (@Param("roleName")String roleName);
}
