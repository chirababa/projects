package com.example.springdemo.repositories;

import com.example.springdemo.entities.MedicationHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicationHistoryRepository extends JpaRepository<MedicationHistory, Integer> {
}
