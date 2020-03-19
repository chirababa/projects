import { Component, OnInit } from '@angular/core';
import {RouterModule, Router} from '@angular/router';
import {HttpclientService,User,Role,Patient,Medication,MedicationPlan,Activity,ActivityRequest} from '../../services/httpclient.service';
import { GoogleChartInterface } from 'ng2-google-charts/google-charts-interfaces';

@Component({
  selector: 'app-doctor-patient',
  templateUrl: './doctor-patient.component.html',
  styleUrls: ['./doctor-patient.component.css']
})
export class DoctorPatientComponent implements OnInit {

  patientsByDoctor:Patient[];
  patientUser:User = new User("",new Role("","",[]),null,"","","","","","","");
  patient:Patient = new Patient("",null,null,null,"",null,null);
  caregiverUseraname:String = "";
  patientUpdate:String = "";
  errorMessageSignUp:String = "";
  initIdObjectFromTable:String = "";
  medicationPlan:MedicationPlan =  new MedicationPlan("",null,"","",null);
  activitiesRequest:ActivityRequest = new ActivityRequest("","");

   public activityChart:GoogleChartInterface  = {
      chartType: 'PieChart',
      dataTable: [
         ['Activity', 'Duration'],
         ['Breakfast', 0.0],
         ['Grooming', 0.0],
         ['Toileting', 0.0],
         ['Sleeping', 0.0],
         ['Leaving', 0.0],
         ['Spare_Time/TV', 0.0],
         ['Showering', 0.0],
         ['Snack', 0.0],
         ['Lunch', 0.0]
      ],
      options: {'title': 'Activities history',  width:550,  height:300,  'is3D':true},
   }

  constructor(private router: Router,private httpClientService:HttpclientService) { }

  ngOnInit() {
    sessionStorage.setItem("idPatientHistory","");
    sessionStorage.setItem("startDate","");
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

  addPatient()
  {
    if(this.patientUser.username == "" || this.patientUser.password== ""
    || this.patientUser.name == "" || this.patientUser.email == ""
    || this.patientUser.birthdate == "" || this.patientUser.gender == ""
    ||  this.patientUser.address == "" || this.caregiverUseraname == "")
    {
      this.errorMessageSignUp = "Complete all mandatory fields"
    }
    else
    {
      this.httpClientService.isDuplicateCredential(this.patientUser).subscribe(
        response=>
        {
          if(response == true)
          {
            this.errorMessageSignUp = "Username or email already exists"
          }
          else
          {
            this.httpClientService.addUserPatient(this.patientUser).subscribe(
              responseUser=>{
                this.httpClientService.addPatient(this.patient,sessionStorage.getItem("idUser"),responseUser,this.caregiverUseraname).subscribe(
                  patientResponse=>{
                      if(patientResponse == "-1")
                      {
                          this.errorMessageSignUp = "Caregiver doesnt exist";
                      }
                      else
                      {
                          this.clearFields(this.patientUser);
                          this.patient.medicalRecord = "";
                          this.caregiverUseraname = "";
                          this.errorMessageSignUp = "";
                          this.getAllPatientByDoctorId();
                      }
                  }
                )
              }
            )
          }
        }
      )
      }
    }

    addMedicationPlan(patient:Patient)
    {
      this.medicationPlan.patient = patient;
      this.httpClientService.addMedicationPlan(this.medicationPlan).subscribe(
        response=>{
          this.medicationPlan.startTime = "";
          this.medicationPlan.endTime = "";
        }
      );
    }

    deletePatient(patient:Patient)
    {
      this.httpClientService.deletePatient(patient.idPatient).subscribe(
        response=>{
          console.log("Success");
          this.getAllPatientByDoctorId();
        }
      );
    }

    updatePatient()
    {
      if(this.patient.idPatient != "")
      {
          this.httpClientService.updatePatient(this.patient).subscribe(
            response=>
            {
              this.initIdObjectFromTable = "";
              this.getAllPatientByDoctorId();
            }
          )
      }
    }

    onRowClickPatient(event,field,patient){
      let s = event.target.outerText;
      if(patient.idPatient!=this.initIdObjectFromTable)
      {
        this.patient = patient;
        this.initIdObjectFromTable = patient.idPatient;
      }

      if(s != "")
      {
        switch(field)  {
          case "username":{
            this.patient.user.username = s ;
              break;
          }
          case "name":{
            this.patient.user.name = event.target.outerText;
              break;
          }
          case "email":{
            this.patient.user.email = event.target.outerText;
              break;
          }
          case "birthdate":{
            this.patient.user.birthdate = event.target.outerText;
              break;
          }
          case "gender":{
            this.patient.user.gender = event.target.outerText;
              break;
          }
          case "address":{
            this.patient.user.address = event.target.outerText;
              break;
          }
          case "medicalRecord":{
            this.patient.medicalRecord = event.target.outerText;
              break;
          }
        }
      }
    }

    navigateToHistory(idPatient:String,startDate:String)
    {
      sessionStorage.setItem("idPatientHistory",idPatient.toString());
      sessionStorage.setItem("startDate",startDate.toString());
      if(sessionStorage.getItem("idPatientHistory")!="" &&  sessionStorage.getItem("startDate")!="")
      {
        this.router.navigate(["/history"]);
      }
    }

    clearFields(user:User)
    {
      user.username = "";
      user.password = "";
      user.name = "" ;
      user.email = "";
      user.birthdate = "";
      user.gender = "";
      user.address = "";
    }

}
