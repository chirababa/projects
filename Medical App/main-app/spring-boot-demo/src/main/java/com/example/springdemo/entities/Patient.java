package com.example.springdemo.entities;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;

import java.util.List;

@Entity
@Table(name = "patient")
public class Patient {
    @Id
    private Integer id;

    @OneToOne(cascade = CascadeType.ALL)
    @MapsId
    private User user;

    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private User doctor;

    @ManyToOne
    @JoinColumn(name = "caregiver_id")
    private User careGiver;

    @Column(name = "medical_record", nullable = false)
    private String medicalRecord;

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(mappedBy = "patient",cascade = CascadeType.ALL)
    private List<MedicationPlan> medicationPlanList;

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(mappedBy = "patient",cascade = CascadeType.ALL)
    private List<MonitorActivity> activitiesList;

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(mappedBy = "patient",cascade = CascadeType.ALL)
    private List<MedicationHistory> medicationHistoryList;

    public Integer getIdPatient() {
        return id;
    }

    public void setIdPatient(Integer id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getDoctor() {
        return doctor;
    }

    public void setDoctor(User doctor) {
        this.doctor = doctor;
    }

    public User getCareGiver() {
        return careGiver;
    }

    public void setCareGiver(User careGiver) {
        this.careGiver = careGiver;
    }

    public String getMedicalRecord() {
        return medicalRecord;
    }

    public void setMedicalRecord(String medicalRecord) {
        this.medicalRecord = medicalRecord;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<MedicationPlan> getMedicationPlanList() {
        return medicationPlanList;
    }

    public void setMedicationPlanList(List<MedicationPlan> medicationPlanList) {
        this.medicationPlanList = medicationPlanList;
    }

    public List<MonitorActivity> getActivitiesList() {
        return activitiesList;
    }

    public void setActivitiesList(List<MonitorActivity> activitiesList) {
        this.activitiesList = activitiesList;
    }

    public List<MedicationHistory> getMedicationHistoryList() {
        return medicationHistoryList;
    }

    public void setMedicationHistoryList(List<MedicationHistory> medicationHistoryList) {
        this.medicationHistoryList = medicationHistoryList;
    }
}
