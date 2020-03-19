package com.example.springdemo.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;

import java.util.Date;
import java.util.List;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "medication_plan")
public class MedicationPlan {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Integer idMedicationPlan;

    @ManyToOne
    @JoinColumn(name = "id_patient")
    @JsonIgnore
    private Patient patient;

    @Column(name = "start_time", nullable = false)
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    private Date startTime;

    @Column(name = "end_time", nullable = false)
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    private Date endTime;

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(mappedBy = "medicationPlan",cascade = CascadeType.ALL)
    private List<MedicationRecipe> medicationRecipeList;

    public List<MedicationRecipe> getMedicationRecipeList() {
        return medicationRecipeList;
    }

    public void setMedicationRecipeList(List<MedicationRecipe> medicationRecipeList) {
        this.medicationRecipeList = medicationRecipeList;
    }

    public Integer getIdMedicationPlan() {
        return idMedicationPlan;
    }

    public void setIdMedicationPlan(Integer idMedicationPlan) {
        idMedicationPlan = idMedicationPlan;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
}
