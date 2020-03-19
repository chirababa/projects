import { Component, OnInit } from '@angular/core';
import {RouterModule, Router} from '@angular/router';
import {HttpclientService,User,Role,Patient,Medication} from '../../services/httpclient.service';

@Component({
  selector: 'app-doctor',
  templateUrl: './doctor.component.html',
  styleUrls: ['./doctor.component.css']
})
export class DoctorComponent implements OnInit {

  constructor(private router: Router,private httpClientService:HttpclientService) { }

  ngOnInit() {

  }

  logout() {
   sessionStorage.removeItem('idUser');
   sessionStorage.removeItem('username');
   sessionStorage.removeItem('role');
   this.router.navigate([""]);
  }
}
