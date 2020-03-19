package com.example.springdemo.controller;

import com.example.springdemo.HistoryClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import soapServices.*;

@RestController
@RequestMapping("/soap")
public class SoapController {

	@Autowired
	private HistoryClient historyClient;

	@PostMapping("/history")
	public ActivityResponse historyActivities(@RequestBody ActivityRequest activityRequest)
	{
		ActivityResponse activityResponse= (ActivityResponse)historyClient.soapFunction("http://localhost:9093/soap",activityRequest);
		return activityResponse;
	}

	@PostMapping("/recommendation")
	public RecommendationResponse addRecommendation(@RequestBody RecommendationRequest recommendationRequest)
	{
		RecommendationResponse recommendationResponse = (RecommendationResponse) historyClient.soapFunction("http://localhost:9093/soap",recommendationRequest);
		return  recommendationResponse;
	}

	@PostMapping("/medication")
	public MedicationResponse getMedication(@RequestBody MedicationRequest medicationRequest)
	{
		MedicationResponse medicationResponse = (MedicationResponse) historyClient.soapFunction("http://localhost:9093/soap",medicationRequest);
		return  medicationResponse;
	}
}
