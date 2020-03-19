package com.example.springdemo.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.stereotype.Component;

import javax.persistence.*;

import java.util.List;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "medication_recipe")
public class MedicationRecipe {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Integer idMedicationRecipe;

    @ManyToOne
    @JoinColumn(name = "medication_id")
    private Medication medication;

    @ManyToOne
    @JoinColumn(name = "medication_plan_id")
    @JsonIgnore
    private MedicationPlan medicationPlan;

    @Column(name = "intake_moments", nullable = false)
    private String intakeMoments;

    @Column(name = "dosage", nullable = false)
    private String dosage;

    @Column(name = "taken",nullable = false)
	private boolean taken;

    public Integer getIdMedicationRecipe() {
        return idMedicationRecipe;
    }

    public void setIdMedicationRecipe(Integer idMedicationRecipe) {
        this.idMedicationRecipe = idMedicationRecipe;
    }

    public String getIntakeMoments() {
        return intakeMoments;
    }

    public void setIntakeMoments(String intakeMoments) {
        this.intakeMoments = intakeMoments;
    }

    public Medication getMedication() {
        return medication;
    }

    public void setMedication(Medication medication) {
        this.medication = medication;
    }

    public MedicationPlan getMedicationPlan() {
        return medicationPlan;
    }

    public void setMedicationPlan(MedicationPlan medicationPlan) {
        this.medicationPlan = medicationPlan;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

	public boolean isTaken() {
		return taken;
	}

	public void setTaken(boolean taken) {
		this.taken = taken;
	}
}
