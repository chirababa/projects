package com.example.springdemo.repositories;

import com.example.springdemo.entities.MedicationRecipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicationRecipeRepository extends JpaRepository<MedicationRecipe, Integer> {

}
