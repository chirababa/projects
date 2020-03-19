package com.example.springdemo.controller;

import com.example.springdemo.entities.MedicationHistory;
import com.example.springdemo.entities.MedicationPlan;
import com.example.springdemo.services.MedicationHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(value = "/medication_history")
public class MedicationHistoryController {

	private final MedicationHistoryService medicationHistoryService;

	@Autowired
	public MedicationHistoryController(MedicationHistoryService medicationHistoryService) {
		this.medicationHistoryService = medicationHistoryService;
	}

	@GetMapping(value = "/{id}")
	public MedicationHistory findMedicationHistoryById(@PathVariable("id") Integer id){
		return medicationHistoryService.findById(id);
	}

	@GetMapping("/all")
	public List<MedicationHistory> findAll(){
		return medicationHistoryService.findAll();
	}

	@PostMapping("/insert")
	public Integer insert(@RequestBody MedicationHistory medicationHistory) {
		return medicationHistoryService.insert(medicationHistory);
	}
}
