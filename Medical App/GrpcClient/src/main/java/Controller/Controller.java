package Controller;
import Model.MedicationPlanDTO;
import grpc.User;
import grpc.userGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class Controller {
	static int idPatient;
	private final ManagedChannel channel;
	private final userGrpc.userBlockingStub userStub;

	public Controller()
	{
		 this.channel = ManagedChannelBuilder.forAddress("localhost",9090).usePlaintext().build();

		 this.userStub = userGrpc.newBlockingStub(this.channel);
	}

	public String login(String username,String password)
	{
		User.LoginRequest loginrequest = User.LoginRequest.newBuilder().setUsername(username).setPassword(password).build();

		User.LoginResponse response = this.userStub.login(loginrequest);
		this.idPatient = Integer.parseInt(response.getIdPatient());
		return response.getResponseMessage();
	}

	public List<MedicationPlanDTO> getMedicationPlan() {
		List<MedicationPlanDTO> medicationPlanDTOList = new LinkedList<>();
		if(this.idPatient!=-1) {
			User.MedicationPlanRequest medPlanRequest = User.MedicationPlanRequest.newBuilder().setIdPatient(String.valueOf(this.idPatient)).build();
			User.MedicationPlanResponse response = this.userStub.getMedicationPlan(medPlanRequest);

			int sizeMedicationPlan = response.getMedicationPlanCount();
			for(int i=0;i<sizeMedicationPlan;i++) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				Date currentDate = new Date();
				Date startTime = null;
				Date endTime =null;
				try {
					startTime = sdf.parse(response.getMedicationPlanList().get(i).getStartTime());
					endTime = sdf.parse(response.getMedicationPlanList().get(i).getEndTime());
				} catch (ParseException e) {
					e.printStackTrace();
				}
				if(startTime.compareTo(currentDate) <=0 && endTime.compareTo(currentDate)>=0) {
					int sizeMedicationRecipe = response.getMedicationPlanList().get(i).getMedicationRecipeCount();
					for (int j = 0; j < sizeMedicationRecipe; j++) {
						MedicationPlanDTO medPlan = new MedicationPlanDTO();
						medPlan.setStartTime(response.getMedicationPlanList().get(i).getStartTime());
						medPlan.setEndTime(response.getMedicationPlanList().get(i).getEndTime());
						medPlan.setDosage(response.getMedicationPlanList().get(i).getMedicationRecipeList().get(j).getDosage());
						medPlan.setMedName(response.getMedicationPlanList().get(i).getMedicationRecipeList().get(j).getMedication().getName());
						medPlan.setIntakeMoments(response.getMedicationPlanList().get(i).getMedicationRecipeList().get(j).getIntake());
						medPlan.setSideEffects(response.getMedicationPlanList().get(i).getMedicationRecipeList().get(j).getMedication().getSideEffects());
						medPlan.setIdMedicationRecipe(response.getMedicationPlanList().get(i).getMedicationRecipeList().get(j).getIdMedicationRecipe());
						medPlan.setTaken(response.getMedicationPlanList().get(i).getMedicationRecipeList().get(j).getTaken());
						medicationPlanDTOList.add(medPlan);
					}
				}
				}
			}
		return medicationPlanDTOList;
	}

	public String pubishTaken(String idMedicationRecipe,String reset)
	{
		User.PublishTakenRequest publishTakenRequest = User.PublishTakenRequest.newBuilder().setIdMedicationRecipe(idMedicationRecipe).setReset(reset).build();
		User.PublishTakenResponse response = this.userStub.publishTaken(publishTakenRequest);
		return response.getResponseTaken();
	}

	public String publishNotTaken(List<String> messageAlert,String patientUsername)
	{
		User.PublishNotTakenRequest publishNotTakenRequest = User.PublishNotTakenRequest.newBuilder().addAllResponseNotTaken(messageAlert).setPatientUsername(patientUsername).build();
		User.PublishTakenResponse response = this.userStub.publishNotTaken(publishNotTakenRequest);
		return response.getResponseTaken();
	}

	public void logout()
	{
		this.idPatient = -1;
	}
}
