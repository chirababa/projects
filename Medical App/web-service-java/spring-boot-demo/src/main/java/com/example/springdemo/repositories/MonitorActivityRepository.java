package com.example.springdemo.repositories;

import com.example.springdemo.entities.MonitorActivity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MonitorActivityRepository extends JpaRepository<MonitorActivity, Integer> {
}
