package com.example.springdemo;

import com.example.springdemo.entities.MedicationHistory;
import com.example.springdemo.entities.MonitorActivity;
import com.example.springdemo.entities.Patient;
import com.example.springdemo.services.MonitorActivityService;
import com.example.springdemo.services.PatientService;
import com.medicalplatformp.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Endpoint
public class ActivitiesHistoryEndpoint {

	@Autowired
	private PatientService patientService;

	@Autowired
	private MonitorActivityService monitorActivityService;

	@PayloadRoot(namespace = "http://www.medicalPlatformP.com",localPart = "activityRequest")
	@ResponsePayload
	public ActivityResponse activitiesHistory(@RequestPayload ActivityRequest request)
	{
		ActivityResponse response = new ActivityResponse();
		Patient patient =  patientService.findPatientById(Integer.parseInt(request.getIdPatient()));
		DurationList dList =  new DurationList();

		if(patient !=null) {
			List<MonitorActivity> allMonitorActivities = patient.getActivitiesList();
			List<MonitorActivity> monitorActivities =  new ArrayList<>();
			SimpleDateFormat formatter =new SimpleDateFormat("yyyy-MM-dd");

			for (MonitorActivity a : allMonitorActivities) {
				if(request.getStartDate().equalsIgnoreCase(formatter.format(a.getStartTime())))
				{
					System.out.println(request.getStartDate() + ":" + formatter.format(a.getStartTime()) + " " + a.getIdMonitorActivity() +" "+ a.getActivityName());
					monitorActivities.add(a);
				}
			}

			HashMap<String, Integer> totalDuration = new HashMap<>();
			totalDuration.put("Sleeping", 0);
			totalDuration.put("Breakfast", 0);
			totalDuration.put("Grooming", 0);
			totalDuration.put("Showering", 0);
			totalDuration.put("Toileting", 0);
			totalDuration.put("Snack", 0);
			totalDuration.put("Spare_Time/TV", 0);
			totalDuration.put("Leaving", 0);
			totalDuration.put("Lunch", 0);
			for (MonitorActivity m : monitorActivities)
			{
				long diffinMillies = Math.abs(m.getEndTime().getTime() - m.getStartTime().getTime());
				long diff = TimeUnit.MINUTES.convert(diffinMillies, TimeUnit.MILLISECONDS);
				int value = totalDuration.get(m.getActivityName());
				value += (int) diff;
				totalDuration.put(m.getActivityName(),value);
			}

			for (Map.Entry<String, Integer> entry : totalDuration.entrySet())
			{
				DurationObject db=  new DurationObject();
				db.setActivityName((entry.getKey()));
				db.setDuration(entry.getValue());
				dList.getActivity().add(db);
			}
			response.setActivitiesDuration(dList);
		}
		return response;
	}

	@PayloadRoot(namespace = "http://www.medicalPlatformP.com",localPart = "recommendationRequest")
	@ResponsePayload
	public RecommendationResponse addRecommendation(@RequestPayload RecommendationRequest recommendationRequest) {
		RecommendationResponse recommendationResponse = new RecommendationResponse();
		MonitorActivity monitorActivity = monitorActivityService.findMonitorActivitybyId(Integer.parseInt(recommendationRequest.getIdActivity()));
		monitorActivity.setRecommendation(recommendationRequest.getRecommendation());
		int result = monitorActivityService.update(monitorActivity);
		if(result>0)
		{
			recommendationResponse.setResponse("Success");
		}
		else
		{
			recommendationResponse.setResponse("Fail");
		}
		return recommendationResponse;
	}

	@PayloadRoot(namespace = "http://www.medicalPlatformP.com",localPart = "medicationRequest")
	@ResponsePayload
	public MedicationResponse medNotTaken(@RequestPayload MedicationRequest medicationRequest) {
		MedicationResponse medicationResponse = new MedicationResponse();
		Patient patient =  patientService.findPatientById(Integer.parseInt(medicationRequest.getIdPatient()));
		SimpleDateFormat formatter =new SimpleDateFormat("yyyy-MM-dd");
		for(MedicationHistory h:patient.getMedicationHistoryList())
		{
			if(formatter.format(h.getMedicationDay()).equals(medicationRequest.getMedPlanDate()))
			{
				System.out.println(formatter.format(h.getMedicationDay()) + ":" + medicationRequest.getMedPlanDate());
				medicationResponse.getMedicationName().add(h.getMedicationName());
			}
		}
		return medicationResponse;
	}
}
