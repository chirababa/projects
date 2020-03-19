package com.example.springdemo.services;

import com.example.springdemo.entities.*;
import grpc.User.LoginRequest;
import grpc.userGrpc.userImplBase;
import grpc.User.MedicationPlanRequest;
import grpc.User.MedicationPlanResponse;
import grpc.User.PublishTakenRequest;
import grpc.User.PublishTakenResponse;
import grpc.User.PublishNotTakenRequest;
import io.grpc.stub.StreamObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Component
public class GrpcService extends userImplBase {

	@Autowired
	private UserService userService;

	@Autowired
	private PatientService patientService;

	@Autowired
	private MedicationRecipeService medicationRecipeService;

	@Autowired
	private MedicationHistoryService medicationHistoryService;

	@Override
	public void login(LoginRequest request, StreamObserver<grpc.User.LoginResponse> responseObserver)
	{
		String username = request.getUsername();
		String password = request.getPassword();
		User user =  new User();
		user.setUsername(username);
		user.setPassword(password);
		User result = userService.login(user);
		grpc.User.LoginResponse.Builder response = grpc.User.LoginResponse.newBuilder();
		if(result!=null) {
			response.setIdPatient(result.getIdUser().toString());
			response.setResponseMessage(user.getUsername());
		}
		else {
			response.setIdPatient("-1");
			response.setResponseMessage("INVALID USERNAME OR PASSWORD");
		}
		responseObserver.onNext(response.build());
		responseObserver.onCompleted();
	}

	@Override
	public void getMedicationPlan(MedicationPlanRequest medicationPlanRequest,StreamObserver<MedicationPlanResponse> responseObserver)
	{
		String idPatient = medicationPlanRequest.getIdPatient();
		Patient patient = patientService.findPatientById(Integer.parseInt(idPatient));
		MedicationPlanResponse.Builder medPlanResponse = MedicationPlanResponse.newBuilder();
		for(MedicationPlan m:patient.getMedicationPlanList()) {
			grpc.User.MedicationPlan.Builder medplan = grpc.User.MedicationPlan.newBuilder();
			medplan.setStartTime(m.getStartTime().toString()).setEndTime(m.getEndTime().toString());
			for(MedicationRecipe r:m.getMedicationRecipeList())	{
				grpc.User.Medication.Builder medication = grpc.User.Medication.newBuilder();
				medication.setName(r.getMedication().getName()).setSideEffects(r.getMedication().getSideEffects()).build();
				medplan.addMedicationRecipeBuilder().setMedication(medication).setIntake(r.getIntakeMoments()).
						setDosage(r.getDosage()).setIdMedicationRecipe(String.valueOf(r.getIdMedicationRecipe())).setTaken(String.valueOf(r.isTaken())).build();
			}
			medPlanResponse.addMedicationPlan(medplan);
		}
		responseObserver.onNext(medPlanResponse.build());
		responseObserver.onCompleted();
	}

	@Override
	public void publishTaken(PublishTakenRequest publishTakenRequest,StreamObserver<PublishTakenResponse> responseObserver)
	{
		String idMedicationRecipe = publishTakenRequest.getIdMedicationRecipe();
		PublishTakenResponse.Builder response = PublishTakenResponse.newBuilder();
		MedicationRecipe medicationRecipe =  medicationRecipeService.findMedicationRecipeById(Integer.parseInt(idMedicationRecipe));
		if(publishTakenRequest.getReset().equalsIgnoreCase("reset")) {
			medicationRecipe.setTaken(false);
		}else {
			medicationRecipe.setTaken(true);
		}
		medicationRecipeService.update(medicationRecipe);
		response.setResponseTaken("SUCCESS");
		responseObserver.onNext(response.build());
		responseObserver.onCompleted();
	}

	@Override
	public void publishNotTaken(PublishNotTakenRequest publishNotTakenRequest, StreamObserver<PublishTakenResponse> responseObserver)
	{

		User user = userService.getUserByUsername(publishNotTakenRequest.getPatientUsername());
		PublishTakenResponse.Builder response = PublishTakenResponse.newBuilder();
		Patient patient =  patientService.findPatientById(user.getIdUser());
		if(!publishNotTakenRequest.getResponseNotTakenList().isEmpty()) {
			for(String med:publishNotTakenRequest.getMedicationNameList())
			{
				try {
					Date date1=new SimpleDateFormat("yyyy-MM-dd").parse(publishNotTakenRequest.getDate());
					MedicationHistory medicationHistory =  new MedicationHistory();
					medicationHistory.setPatient(patient);
					medicationHistory.setMedicationName(med);
					medicationHistory.setMedicationDay(date1);
					medicationHistoryService.insert(medicationHistory);
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
			System.out.println("caregiver " + patient.getCareGiver().getUsername());
			System.out.println(publishNotTakenRequest.getResponseNotTakenList());
		}
		response.setResponseTaken("SUCCESS");
		responseObserver.onNext(response.build());
		responseObserver.onCompleted();
	}
}
