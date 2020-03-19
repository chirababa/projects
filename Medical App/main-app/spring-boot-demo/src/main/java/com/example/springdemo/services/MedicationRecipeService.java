package com.example.springdemo.services;

import com.example.springdemo.entities.Medication;
import com.example.springdemo.entities.MedicationPlan;
import com.example.springdemo.entities.MedicationRecipe;
import com.example.springdemo.entities.User;
import com.example.springdemo.errorhandler.ResourceNotFoundException;
import com.example.springdemo.repositories.MedicationRecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;

@Service
public class MedicationRecipeService {

    private final MedicationRecipeRepository medicationRecipeRepository;
    private final MedicationService medicationService;
    private final MedicationPlanService medicationPlanService;
    @Autowired
    public MedicationRecipeService(MedicationRecipeRepository medicationRecipeRepository, MedicationService medicationService, MedicationPlanService medicationPlanService) {
        this.medicationRecipeRepository = medicationRecipeRepository;
        this.medicationService = medicationService;
        this.medicationPlanService = medicationPlanService;
    }

    public MedicationRecipe findMedicationRecipeById(Integer id){
        Optional<MedicationRecipe> medicationRecipe  = medicationRecipeRepository.findById(id);

        if (!medicationRecipe.isPresent()) {
            throw new ResourceNotFoundException("MedicationRecipe", "medicationRecipe id", id);
        }
        return medicationRecipe.get();
    }

    public List<MedicationRecipe> findAll(){
        return medicationRecipeRepository.findAll();
    }

    public Integer insert(MedicationRecipe medicationRecipe) {
        return medicationRecipeRepository
                .save(medicationRecipe)
                .getIdMedicationRecipe();
    }

    public Integer update(MedicationRecipe medicationRecipe) {

        Optional<MedicationRecipe> medicationRecipeReturned = medicationRecipeRepository.findById(medicationRecipe.getIdMedicationRecipe());

        if(!medicationRecipeReturned.isPresent()){
            throw new ResourceNotFoundException("MedicationRecipe", "MedicationRecipe id", medicationRecipe.getIdMedicationRecipe().toString());
        }

        return medicationRecipeRepository.save(medicationRecipe).getIdMedicationRecipe();
    }

    public void delete(MedicationRecipe medicationRecipe){
        this.medicationRecipeRepository.deleteById(medicationRecipe.getIdMedicationRecipe());
    }

    public Integer addMedicationRecipe(MedicationRecipe medicationRecipe, Integer medicationPlanId, Integer medicationId)
    {
        System.out.println(medicationId);
        System.out.println(medicationPlanId);
        MedicationPlan medicationPlan = medicationPlanService.findMedicationPlanById(medicationPlanId);
        Medication medication = medicationService.findMedicationById(medicationId);
        if(medicationPlan == null || medication == null)
        {
            return -1;
        }
        medicationRecipe.setMedication(medication);
        medicationRecipe.setMedicationPlan(medicationPlan);
        return this.insert(medicationRecipe);
    }
}
