package com.example.springdemo.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

import java.util.List;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "medication")
public class Medication {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id_medication", unique = true, nullable = false)
    private Integer idMedication;

    @Column(name = "name",  nullable = false, length = 100)
    private String name;

    @Column(name = "side_effects", length = 200)
    private String sideEffects;

    @OneToMany(mappedBy = "medication",cascade = CascadeType.ALL)
    @JsonIgnore
    private List<MedicationRecipe> medicationRecipe;

    public List<MedicationRecipe> getMedicationRecipe() {
        return medicationRecipe;
    }

    public void setMedicationRecipe(List<MedicationRecipe> medicationRecipe) {
        this.medicationRecipe = medicationRecipe;
    }

    public Integer getIdMedication() {
        return idMedication;
    }

    public void setIdMedication(Integer idMedication) {
        this.idMedication = idMedication;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSideEffects() {
        return sideEffects;
    }

    public void setSideEffects(String sideEffects) {
        this.sideEffects = sideEffects;
    }
}
