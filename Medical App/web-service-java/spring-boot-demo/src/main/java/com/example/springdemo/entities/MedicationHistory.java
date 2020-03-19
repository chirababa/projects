package com.example.springdemo.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Date;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "medicationHistory")
public class MedicationHistory {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id_med_history", unique = true, nullable = false)
	private Integer idMedication;

	@Column(name = "start_time", nullable = false)
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
	private Date medicationDay;

	@Column(name = "medication_name", length = 200)
	private String medicationName;

	@ManyToOne
	@JsonIgnore
	@JoinColumn(name = "patient_id")
	private Patient patient;

	public Integer getIdMedication() {
		return idMedication;
	}

	public void setIdMedication(Integer idMedication) {
		this.idMedication = idMedication;
	}

	public Date getMedicationDay() {
		return medicationDay;
	}

	public void setMedicationDay(Date medicationDay) {
		this.medicationDay = medicationDay;
	}

	public String getMedicationName() {
		return medicationName;
	}

	public void setMedicationName(String medicationName) {
		this.medicationName = medicationName;
	}

	public Patient getPatient() {
		return patient;
	}

	public void setPatient(Patient patient) {
		this.patient = patient;
	}
}
