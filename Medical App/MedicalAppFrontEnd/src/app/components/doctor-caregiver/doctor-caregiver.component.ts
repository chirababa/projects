import { Component, OnInit } from '@angular/core';
import {RouterModule, Router} from '@angular/router';
import {HttpclientService,User,Role,Patient,Medication} from '../../services/httpclient.service';

@Component({
  selector: 'app-doctor-caregiver',
  templateUrl: './doctor-caregiver.component.html',
  styleUrls: ['./doctor-caregiver.component.css']
})
export class DoctorCaregiverComponent implements OnInit {

  caregiver:User[];
  caregiverUser:User = new User("",new Role("","",[]),null,"","","","","","","");
  errorMessageSingUpCareGiver:String = "";
  initIdObjectFromTable:String = "";
  constructor(private router: Router,private httpClientService:HttpclientService) { }

  ngOnInit() {
    this.getAllCargivers();
  }

  getAllCargivers(){
    this.httpClientService.getUsersByRole("caregiver").subscribe(
      response=>{
      this.caregiver = response;
    }
    )
  }

  addCaregiver(){
    if(this.caregiverUser.username == "" || this.caregiverUser.password== ""
    || this.caregiverUser.name == "" || this.caregiverUser.email == ""
    || this.caregiverUser.birthdate == "" || this.caregiverUser.gender == ""
    ||  this.caregiverUser.address == ""){
      this.errorMessageSingUpCareGiver = "Complete all mandatory fields"
    }
    else{
      this.httpClientService.isDuplicateCredential(this.caregiverUser).subscribe(
        response=>{
          if(response == true){
            this.errorMessageSingUpCareGiver = "Username or email already exists"
          }
          else{
            this.httpClientService.addCaregiver(this.caregiverUser).subscribe(
              dataReg=>{
                    this.clearFields(this.caregiverUser);
                    this.errorMessageSingUpCareGiver = "";
                    this.getAllCargivers();
              }
            )
          }
        }
      )
    }
  }

  deleteUser(user:User){
    this.httpClientService.deleteUser(user.idUser).subscribe(
      response=>{
        this.getAllCargivers();
      }
    );
  }

  updateCaregiver()
  {
    if(this.caregiverUser.idUser != "")
    {
        this.httpClientService.updateUser(this.caregiverUser).subscribe(
          response=>
          {
            this.initIdObjectFromTable = "";
            this.getAllCargivers();
          }
        )
    }
  }

  onRowClickCareGiver(event,field,caregiver){
    let s = event.target.outerText;
    if(caregiver.IdUser!=this.initIdObjectFromTable)
    {
      this.caregiverUser = caregiver;
      this.initIdObjectFromTable = caregiver.IdUser;
    }

    if(s != "")
    {
      switch(field)  {
        case "username":{
          this.caregiverUser.username = s ;
            break;
        }
        case "name":{
          this.caregiverUser.name = event.target.outerText;
            break;
        }
        case "email":{
          this.caregiverUser.email = event.target.outerText;
            break;
        }
        case "birthdate":{
          this.caregiverUser.birthdate = event.target.outerText;
            break;
        }
        case "gender":{
          this.caregiverUser.gender = event.target.outerText;
            break;
        }
        case "address":{
          this.caregiverUser.address = event.target.outerText;
            break;
        }
      }
    }
  }


  clearFields(user:User){
    user.username = "";
    user.password = "";
    user.name = "" ;
    user.email = "";
    user.birthdate = "";
    user.gender = "";
    user.address = "";
  }

}
