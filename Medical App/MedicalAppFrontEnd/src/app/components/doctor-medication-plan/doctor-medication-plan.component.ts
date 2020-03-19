import { Component, OnInit } from '@angular/core';
import {RouterModule, Router} from '@angular/router';
import {HttpclientService,User,Role,Patient,Medication,MedicationPlan,MedicationRecipe} from '../../services/httpclient.service';

@Component({
  selector: 'app-doctor-medication-plan',
  templateUrl: './doctor-medication-plan.component.html',
  styleUrls: ['./doctor-medication-plan.component.css']
})
export class DoctorMedicationPlanComponent implements OnInit {

  patientsByDoctor:Patient[];
  medicationRecipe:MedicationRecipe = new MedicationRecipe("",null,null,"","");
  medicationPlanId:String = "";
  medicationId:String = "";
  constructor(private router: Router,private httpClientService:HttpclientService) { }
  ngOnInit() {
      this.getAllPatientByDoctorId();
  }

  getAllPatientByDoctorId()
  {
    this.httpClientService.getAllPatientByDoctorId(sessionStorage.getItem("idUser")).subscribe(
      response=>{
      this.patientsByDoctor = response;
    }
    )
  }

  addMedicationRecipe()
  {
    this.httpClientService.addMedicationRecipe(this.medicationRecipe,this.medicationPlanId,this.medicationId).subscribe(
      response=>{
        this.getAllPatientByDoctorId();
        this.medicationPlanId = "";
        this.medicationId = "";
        this.medicationRecipe.intakeMoments = "";
        this.medicationRecipe.dosage = "";
      }
    );
  }

}
