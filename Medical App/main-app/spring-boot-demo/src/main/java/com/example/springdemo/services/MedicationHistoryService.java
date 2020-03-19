package com.example.springdemo.services;

import com.example.springdemo.entities.MedicationHistory;
import com.example.springdemo.errorhandler.ResourceNotFoundException;
import com.example.springdemo.repositories.MedicationHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MedicationHistoryService {

	private final MedicationHistoryRepository medicationHistoryRepository;

	@Autowired
	public MedicationHistoryService(MedicationHistoryRepository medicationHistoryRepository) {
		this.medicationHistoryRepository = medicationHistoryRepository;
	}

	public MedicationHistory findById(Integer id){
		Optional<MedicationHistory> medicationHistory  = medicationHistoryRepository.findById(id);

		if (!medicationHistory.isPresent()) {
			throw new ResourceNotFoundException("medication", "medication id", id);
		}
		return medicationHistory.get();
	}


	public List<MedicationHistory> findAll(){
		return medicationHistoryRepository.findAll();
	}

	public Integer insert(MedicationHistory medicationHistory) {
		return medicationHistoryRepository
				.save(medicationHistory)
				.getIdMedication();
	}

	public Integer update(MedicationHistory medicationHistory) {

		Optional<MedicationHistory> userReturned = medicationHistoryRepository.findById(medicationHistory.getIdMedication());

		if(!userReturned.isPresent()){
			throw new ResourceNotFoundException("User", "user id", medicationHistory.getIdMedication().toString());
		}

		return medicationHistoryRepository.save(medicationHistory).getIdMedication();
	}

	public void delete(MedicationHistory medicationHistory){
		this.medicationHistoryRepository.deleteById(medicationHistory.getIdMedication());
	}
}
