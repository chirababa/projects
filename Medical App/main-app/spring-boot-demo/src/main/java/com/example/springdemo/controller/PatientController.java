package com.example.springdemo.controller;

import com.example.springdemo.entities.Patient;
import com.example.springdemo.entities.Role;
import com.example.springdemo.entities.User;
import com.example.springdemo.repositories.RoleRepository;
import com.example.springdemo.services.PatientService;
import com.example.springdemo.services.RoleService;
import com.example.springdemo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(value = "/patient")
public class PatientController {

    private final PatientService patientService;

    @Autowired
    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @GetMapping(value = "/{id}")
    public Patient findPatientById(@PathVariable("id") Integer id){
        return patientService.findPatientById(id);
    }

    @GetMapping("/all")
    public List<Patient> findAll(){
        return patientService.findAll();
    }

    @PostMapping("/insert")
    public Integer insert(@RequestBody Patient patient) {
        return patientService.insert(patient);
    }

    @PutMapping("/update")
    public Integer update(@RequestBody Patient patient) {
        return patientService.update(patient);
    }

    @DeleteMapping("/delete/{patientVar}")
    public void delete(@PathVariable("patientVar") Integer idPatient){
        System.out.println(idPatient);
        Patient patient = findPatientById(idPatient);
        patientService.delete(patient);
    }

    @GetMapping("/patientByDoctor/{id}")
    public @ResponseBody
    List<Patient> getAllPatientByDoctorId(@PathVariable("id") Integer id)
    {
        return patientService.getAllPatientByDoctorId(id);
    }

    @GetMapping("/patientByCaregiver/{id}")
    public @ResponseBody
    List<Patient> getAllPatientByCaregiverId(@PathVariable("id") Integer id)
    {
        return patientService.getAllPatientByCaregiverId(id);
    }

    @PostMapping("/addPatient/{idDoctor}/{idPatient}/{usernameCaregiver}")
    public Integer addPatient(@RequestBody Patient patient,@PathVariable("idDoctor") Integer idDoctor,@PathVariable("idPatient") Integer idPatient,@PathVariable("usernameCaregiver") String usernameCaregiver) {
            return patientService.addPatient(patient,idDoctor,idPatient,usernameCaregiver);
    }

}
