package com.example.springdemo.services;

import com.example.springdemo.entities.MonitorActivity;
import com.example.springdemo.errorhandler.ResourceNotFoundException;
import com.example.springdemo.repositories.MonitorActivityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MonitorActivityService {
	private final MonitorActivityRepository monitorActivityRepository;

	@Autowired
	public MonitorActivityService(MonitorActivityRepository monitorActivityRepository) {
		this.monitorActivityRepository = monitorActivityRepository;
	}

	public MonitorActivity findMonitorActivitybyId(Integer id){
		Optional<MonitorActivity> monitorActivity  = monitorActivityRepository.findById(id);

		if (!monitorActivity.isPresent()) {
			throw new ResourceNotFoundException("MonitorActivity", "MonitorActivity id", id);
		}
		return monitorActivity.get();
	}

	public List<MonitorActivity> findAll(){
		return monitorActivityRepository.findAll();
	}

	public Integer insert(MonitorActivity monitorActivity) {
		return monitorActivityRepository
				.save(monitorActivity).getIdMonitorActivity();
	}

	public Integer update(MonitorActivity monitorActivity) {

		Optional<MonitorActivity> monitorActivityReturned = monitorActivityRepository.findById(monitorActivity.getIdMonitorActivity());

		if(!monitorActivityReturned.isPresent()){
			throw new ResourceNotFoundException("monitorActivity", "monitorActivity id", monitorActivity.getIdMonitorActivity().toString());
		}

		return monitorActivityRepository.save(monitorActivity).getIdMonitorActivity();
	}

	public void delete(MonitorActivity role){
		this.monitorActivityRepository.deleteById(role.getIdMonitorActivity());
	}

}
