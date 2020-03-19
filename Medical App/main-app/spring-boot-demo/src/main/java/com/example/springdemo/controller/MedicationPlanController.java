package com.example.springdemo.controller;

import com.example.springdemo.entities.MedicationPlan;
import com.example.springdemo.services.MedicationPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(value = "/medication_plan")
public class MedicationPlanController {

    private final MedicationPlanService medicationPlanService;

    @Autowired
    public MedicationPlanController(MedicationPlanService medicationPlanService) {
        this.medicationPlanService = medicationPlanService;
    }

    @GetMapping(value = "/{id}")
    public MedicationPlan findMedicationPlanById(@PathVariable("id") Integer id){
        return medicationPlanService.findMedicationPlanById(id);
    }

    @GetMapping("/all")
    public List<MedicationPlan> findAll(){
        return medicationPlanService.findAll();
    }

    @PostMapping("/insert")
    public Integer insert(@RequestBody MedicationPlan medicationPlan) {
        return medicationPlanService.insert(medicationPlan);
    }

    @PutMapping("/update")
    public Integer update(@RequestBody MedicationPlan medicationPlan) {
        return medicationPlanService.update(medicationPlan);
    }

    @DeleteMapping("/delete")
    public void delete(@RequestBody MedicationPlan medicationPlan){
        medicationPlanService.delete(medicationPlan);
    }
}
