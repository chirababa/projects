package Model;


public class MedicationPlanDTO {
	private String startTime;
	private String endTime;
	private String intakeMoments;
	private String dosage;
	private String medName;
	private String sideEffects;
	private String idMedicationRecipe;
	private String taken;

	public MedicationPlanDTO() {
	}

	public MedicationPlanDTO(String startTime, String endTime, String intakeMoments, String dosage, String medName, String sideEffects, String idMedicationRecipe, String taken) {
		this.startTime = startTime;
		this.endTime = endTime;
		this.intakeMoments = intakeMoments;
		this.dosage = dosage;
		this.medName = medName;
		this.sideEffects = sideEffects;
		this.idMedicationRecipe = idMedicationRecipe;
		this.taken = taken;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getIntakeMoments() {
		return intakeMoments;
	}

	public void setIntakeMoments(String intakeMoments) {
		this.intakeMoments = intakeMoments;
	}

	public String getDosage() {
		return dosage;
	}

	public void setDosage(String dosage) {
		this.dosage = dosage;
	}

	public String getMedName() {
		return medName;
	}

	public void setMedName(String medName) {
		this.medName = medName;
	}

	public String getSideEffects() {
		return sideEffects;
	}

	public void setSideEffects(String sideEffects) {
		this.sideEffects = sideEffects;
	}

	public String getIdMedicationRecipe() {
		return idMedicationRecipe;
	}

	public void setIdMedicationRecipe(String idMedicationRecipe) {
		this.idMedicationRecipe = idMedicationRecipe;
	}

	public String getTaken() {
		return taken;
	}

	public void setTaken(String taken) {
		this.taken = taken;
	}
}
