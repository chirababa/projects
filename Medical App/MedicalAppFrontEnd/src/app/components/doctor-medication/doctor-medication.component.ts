import { Component, OnInit } from '@angular/core';
import {RouterModule, Router} from '@angular/router';
import {HttpclientService,User,Role,Patient,Medication} from '../../services/httpclient.service';

@Component({
  selector: 'app-doctor-medication',
  templateUrl: './doctor-medication.component.html',
  styleUrls: ['./doctor-medication.component.css']
})
export class DoctorMedicationComponent implements OnInit {

  medication:Medication[];
  medicationInsert:Medication = new Medication("","","");
  errorMessageMedication:String = "";
  initIdObjectFromTable:String ="";

  constructor(private router: Router,private httpClientService:HttpclientService) { }

  ngOnInit() {
    this.getAllMedication();
  }

  getAllMedication(){
    this.httpClientService.getAllMedication().subscribe(
      response=>{
      this.medication = response;
    }
    )
  }

  addMedication(){
    if(this.medicationInsert.name == "" || this.medicationInsert.sideEffects == ""){
      this.errorMessageMedication = "Complete all mandatory fields"
    }
    else{
      this.httpClientService.addMedication(this.medicationInsert).subscribe(
        response=>{
          this.medicationInsert.name = "";
          this.medicationInsert.sideEffects = "";
          this.errorMessageMedication = "";
          this.getAllMedication();
        }
      )
    }
  }

  deleteMedication(medication:Medication)  {
    this.httpClientService.deleteMedication(medication.idMedication).subscribe(
      response=>{
        this.getAllMedication();
      }
    );
  }

  updateMedication()  {
    if(this.medicationInsert.idMedication != ""){
        this.httpClientService.updateMedication(this.medicationInsert).subscribe(
          response=>
          {
            this.initIdObjectFromTable = "";
            this.getAllMedication();
          }
        )
    }
  }

  onRowClickMedication(event,field,medication){
    let s = event.target.outerText;

    if(medication.idMedication!=this.initIdObjectFromTable[1]){
      this.medicationInsert = medication;
      this.initIdObjectFromTable = medication.idMedication;
    }

    if(s != ""){
      switch(field)  {
        case "name":{
          this.medicationInsert.name = event.target.outerText;
          break;
        }
        case "sideEffects":{
          this.medicationInsert.sideEffects = event.target.outerText;
            break;
        }
      }
    }
  }

}
