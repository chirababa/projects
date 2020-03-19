import { Component, OnInit } from '@angular/core';
import {RouterModule, Router} from '@angular/router';
import {HttpclientService,User,Role,Patient,Medication,MedicationPlan,Activity,ActivityRequest,MonitorActivity,RecommendationRequest,RecommendationResponse,MedicationRequest,MedicationResponse} from '../../services/httpclient.service';
import { GoogleChartInterface } from 'ng2-google-charts/google-charts-interfaces';

@Component({
  selector: 'app-patient-history',
  templateUrl: './patient-history.component.html',
  styleUrls: ['./patient-history.component.css']
})

export class PatientHistoryComponent implements OnInit {

  activitiesList:any;
  activitiesRequest:ActivityRequest = new ActivityRequest("","");
  recommendationRequest:RecommendationRequest = new RecommendationRequest("","");
  recommendationResponse:RecommendationResponse = new RecommendationResponse("");
  activitiesAnomalous:MonitorActivity[]= [];
  monAct:MonitorActivity = new MonitorActivity("","","","",false,"");
  recommendation = "";
  medicationRequest:MedicationRequest =  new MedicationRequest("","");
  medicationResponse:String[];
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

  patient:Patient = new Patient("",null,null,null,"",null,null);

  constructor(private router: Router,private httpClientService:HttpclientService) { }

  ngOnInit() {
    this.getPatientById();
    this.getMedNotTaken();
    this.redrawChart();
  }

  getPatientById(){
    this.httpClientService.getPatientById(sessionStorage.getItem("idPatientHistory")).subscribe(
      response=>{
      this.patient = response;

      for (let act of this.patient.activitiesList) {
          if(act.anomalous == true && act.recommendation == null && sessionStorage.getItem("startDate") == act.startTime.split(" ")[0])
          {
            this.activitiesAnomalous.push(act);
          }
      }
  }
  );
  }

  getActivitiesHistory(idPatient:String,startDate:String)
  {
    this.activitiesRequest.idPatient = idPatient;
    this.activitiesRequest.startDate = startDate;
    this.httpClientService.getActivitiesHistory(this.activitiesRequest).subscribe(
      response=>{
        this.activitiesList = response.activitiesDuration.activity;
        for(let i=1;i<10;i++)
        {
          this.activityChart.dataTable[i][1] = this.activitiesList[i-1].duration;
        }
        //console.log(this.activitiesList);
      }
    )
  }

  addRecommendation(idActivity:String)
  {
    this.recommendationRequest.idActivity = idActivity;
    this.recommendationRequest.recommendation = this.recommendation;
    this.httpClientService.addRecommendation(this.recommendationRequest).subscribe(
      response=>{
          this.recommendationResponse = response;
      }
    )
    this.recommendation = "";
  }

  getMedNotTaken()
  {
    this.medicationRequest.idPatient = sessionStorage.getItem("idPatientHistory");
    this.medicationRequest.medPlanDate = sessionStorage.getItem("startDate");

    this.httpClientService.getNotTakenMed(this.medicationRequest).subscribe(
      response=>{
          this.medicationResponse = response.medicationName;

      }
    )
  }
  redrawChart()
  {
    let ccComponent = this.activityChart.component;
    this.getActivitiesHistory(sessionStorage.getItem("idPatientHistory"),sessionStorage.getItem("startDate"));
    ccComponent.draw();
  }
}
