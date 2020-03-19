package com.example.springdemo.repositories;

import com.example.springdemo.entities.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Integer> {

    @Query(value = "SELECT u " +
            "FROM Patient u " +
            "WHERE u.doctor.id=:id")

    List<Patient> getAllPatientByDoctorId(@Param("id") Integer id);

    @Query(value = "SELECT u " +
            "FROM Patient u " +
            "WHERE u.careGiver.id=:id")
    List<Patient> getAllPatientByCareGiverId(@Param("id")Integer id);
}