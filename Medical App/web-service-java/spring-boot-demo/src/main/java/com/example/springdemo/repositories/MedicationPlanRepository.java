package com.example.springdemo.repositories;

import com.example.springdemo.entities.MedicationPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicationPlanRepository extends JpaRepository<MedicationPlan, Integer> {

}
