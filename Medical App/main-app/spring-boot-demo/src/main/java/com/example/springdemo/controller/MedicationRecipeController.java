package com.example.springdemo.controller;

import com.example.springdemo.entities.MedicationRecipe;
import com.example.springdemo.services.MedicationRecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(value = "/medication_recipe")
public class MedicationRecipeController {
    private final MedicationRecipeService medicationRecipeService;

    @Autowired
    public MedicationRecipeController(MedicationRecipeService medicationRecipeService) {
        this.medicationRecipeService = medicationRecipeService;
    }

    @GetMapping(value = "/{id}")
    public MedicationRecipe findMedicationRecipeById(@PathVariable("id") Integer id){
        return medicationRecipeService.findMedicationRecipeById(id);
    }

    @GetMapping("/all")
    public List<MedicationRecipe> findAll(){
        return medicationRecipeService.findAll();
    }

    @PostMapping("/insert/{medicationPlanId}/{medicationId}")
    public Integer addMedicationRecipe(@RequestBody MedicationRecipe medicationRecipe, @PathVariable("medicationPlanId") Integer medicationPlanId,@PathVariable("medicationId") Integer medicationId) {
        return medicationRecipeService.addMedicationRecipe(medicationRecipe,medicationPlanId,medicationId);
    }

    @PutMapping("/update")
    public Integer update(@RequestBody MedicationRecipe medicationRecipe) {
        return medicationRecipeService.update(medicationRecipe);
    }

    @DeleteMapping("/delete")
    public void delete(@RequestBody MedicationRecipe medicationRecipe){
        medicationRecipeService.delete(medicationRecipe);
    }
}
