package com.example.springdemo.services;

import com.example.springdemo.entities.MedicationPlan;
import com.example.springdemo.entities.MedicationRecipe;
import com.example.springdemo.errorhandler.ResourceNotFoundException;
import com.example.springdemo.repositories.MedicationPlanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MedicationPlanService {

    private final MedicationPlanRepository medicationPlanRepository;

    @Autowired
    public MedicationPlanService(MedicationPlanRepository medicationPlanRepository) {
        this.medicationPlanRepository = medicationPlanRepository;
    }

    public MedicationPlan findMedicationPlanById(Integer id){
        Optional<MedicationPlan> medicationPlan  = medicationPlanRepository.findById(id);

        if (!medicationPlan.isPresent()) {
            throw new ResourceNotFoundException("MedicationPlan", "medicationPlan id", id);
        }
        return medicationPlan.get();
    }

    public List<MedicationPlan> findAll(){
        return medicationPlanRepository.findAll();
    }

    public Integer insert(MedicationPlan medication) {
        return medicationPlanRepository
                .save(medication)
                .getIdMedicationPlan();
    }

    public Integer update(MedicationPlan medication) {

        Optional<MedicationPlan> medicationPlanReturned = medicationPlanRepository.findById(medication.getIdMedicationPlan());

        if(!medicationPlanReturned.isPresent()){
            throw new ResourceNotFoundException("MedicationPlan", "MedicationPlan id", medication.getIdMedicationPlan().toString());
        }

        return medicationPlanRepository.save(medication).getIdMedicationPlan();
    }

    public void delete(MedicationPlan medicationPlan){
        this.medicationPlanRepository.deleteById(medicationPlan.getIdMedicationPlan());
    }
}
