package com.example.springdemo.services;

import com.example.springdemo.entities.Patient;
import com.example.springdemo.entities.User;
import com.example.springdemo.errorhandler.ResourceNotFoundException;
import com.example.springdemo.repositories.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PatientService {

    private final PatientRepository patientRepository;
    private final UserService userService;

    @Autowired
    public PatientService(PatientRepository patientRepository, UserService userService) {
        this.patientRepository = patientRepository;
        this.userService = userService;
    }

    public Patient findPatientById(Integer id){
        Optional<Patient> patient  = patientRepository.findById(id);

        if (!patient.isPresent()) {
            throw new ResourceNotFoundException("Patient", "patient id", id);
        }
        return patient.get();
    }

    public List<Patient> findAll(){
        return patientRepository.findAll();
    }

    public Integer insert(Patient patient) {
        return patientRepository
                .save(patient)
                .getIdPatient();
    }

    public Integer update(Patient patient) {

        Optional<Patient> patientReturned = patientRepository.findById(patient.getIdPatient());

        if(!patientReturned.isPresent()){
            throw new ResourceNotFoundException("Patient", "Patient id", patient.getIdPatient().toString());
        }

        return patientRepository.save(patient).getIdPatient();
    }

    public void delete(Patient patient){
        this.patientRepository.deleteById(patient.getIdPatient());
    }

     public List<Patient> getAllPatientByDoctorId(Integer id) {
        return patientRepository.getAllPatientByDoctorId(id);
    }

    public List<Patient> getAllPatientByCaregiverId(Integer id) {
        return patientRepository.getAllPatientByCareGiverId(id);
    }

    public Integer addPatient(Patient patient,Integer idDoctor, Integer idPatient,String usernameCaregiver)
    {
        User user = userService.getUserByUsername(usernameCaregiver);
        if(user == null)
        {
            return -1;
        }

        if(!user.getRole().getRoleName().equals("caregiver"))
        {
            return -1;
        }
        patient.setDoctor(userService.findUserById(idDoctor));
        patient.setCareGiver(userService.getUserByUsername(usernameCaregiver));
        patient.setUser(userService.findUserById(idPatient));
        return this.insert(patient);
    }
}
