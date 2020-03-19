package com.example.springdemo.controller;

import com.example.springdemo.entities.Medication;
import com.example.springdemo.services.MedicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(value = "/medication")
public class MedicationController {
    private final MedicationService medicationService;

    @Autowired
    public MedicationController(MedicationService medicationService) {
        this.medicationService = medicationService;
    }

    @GetMapping(value = "/{id}")
    public Medication findMedicationById(@PathVariable("id") Integer id){
        return medicationService.findMedicationById(id);
    }

    @GetMapping("/all")
    public List<Medication> findAll(){
        return medicationService.findAll();
    }

    @PostMapping("/insert")
    public Integer insert(@RequestBody Medication medication) {
        return medicationService.insert(medication);
    }

    @PutMapping("/update")
    public Integer update(@RequestBody Medication medication) {
        return medicationService.update(medication);
    }

    @DeleteMapping("/delete/{idMedication}")
    public void delete(@PathVariable("idMedication") Integer idMedication){
        Medication medication = findMedicationById(idMedication);
        medicationService.delete(medication);
    }
}
