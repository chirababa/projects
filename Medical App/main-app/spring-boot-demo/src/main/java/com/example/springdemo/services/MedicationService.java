package com.example.springdemo.services;

import com.example.springdemo.entities.Medication;
import com.example.springdemo.errorhandler.ResourceNotFoundException;
import com.example.springdemo.repositories.MedicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MedicationService {
    private final MedicationRepository medicationRepository;

    @Autowired
    public MedicationService(MedicationRepository medicationRepository) {
        this.medicationRepository = medicationRepository;
    }

    public Medication findMedicationById(Integer id){
        Optional<Medication> medication  = medicationRepository.findById(id);

        if (!medication.isPresent()) {
            throw new ResourceNotFoundException("Medication", "medication id", id);
        }
        return medication.get();
    }

    public List<Medication> findAll(){
        return medicationRepository.findAll();
    }

    public Integer insert(Medication medication) {
        return medicationRepository
                .save(medication)
                .getIdMedication();
    }

    public Integer update(Medication medication) {

        Optional<Medication> medicationReturned = medicationRepository.findById(medication.getIdMedication());

        if(!medicationReturned.isPresent()){
            throw new ResourceNotFoundException("Medication", "Medication id", medication.getIdMedication().toString());
        }

        return medicationRepository.save(medication).getIdMedication();
    }

    public void delete(Medication medication){
        this.medicationRepository.deleteById(medication.getIdMedication());
    }
}
