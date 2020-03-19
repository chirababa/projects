import { Component, OnInit } from '@angular/core';
import {RouterModule, Router} from '@angular/router';
import {HttpclientService,User,Role,Patient} from '../../services/httpclient.service';
import { WebSocketAPIService } from '../../services/web-socket-api.service';
import * as Stomp from 'stompjs';
import * as SockJS from 'sockjs-client';
import $ from 'jquery';

@Component({
  selector: 'app-caregiver',
  templateUrl: './caregiver.component.html',
  styleUrls: ['./caregiver.component.css']
})
export class CaregiverComponent implements OnInit {

  patient:Patient[] = [];
  //messageAlert: String = "";

  constructor(private router: Router,private httpClientService:HttpclientService,private webSocketAPI:WebSocketAPIService) {
    this.connect();
  }

  ngOnInit() {
    this.getAllPatientByCareGiver();
  }

  getAllPatientByCareGiver()
  {
    this.httpClientService.getAllPatientByCareGiver(sessionStorage.getItem("idUser")).subscribe(
      response=>{
      this.patient = response;
    }
    )
  }

  connect(){
    this.webSocketAPI.connect();
  }

  disconnect(){
    this.webSocketAPI.disconnect();
  }

  logout()
  {
      sessionStorage.removeItem("idUser");
      sessionStorage.removeItem("username");
      sessionStorage.removeItem("role");
      this.disconnect();
      this.router.navigate([""]);
  }
}
