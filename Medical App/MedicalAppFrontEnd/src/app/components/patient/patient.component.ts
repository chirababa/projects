import { Component, OnInit } from '@angular/core';
import {RouterModule, Router} from '@angular/router';
import {HttpclientService,User,Role,Patient,Medication,MedicationPlan} from '../../services/httpclient.service';

@Component({
  selector: 'app-patient',
  templateUrl: './patient.component.html',
  styleUrls: ['./patient.component.css']
})
export class PatientComponent implements OnInit {


  constructor(private router: Router,private httpClientService:HttpclientService) { }
  patient:Patient = new Patient("",null,null,null,"",null,null);
  ngOnInit() {
    this.getPatientById();
  }

  getPatientById(){
    this.httpClientService.getPatientById(sessionStorage.getItem("idUser")).subscribe(
      response=>{
      this.patient = response;
  }
  );
  }

  logout()
  {
      sessionStorage.removeItem("idUser");
      sessionStorage.removeItem("username");
      sessionStorage.removeItem("role");
      this.router.navigate([""]);
  }

}
