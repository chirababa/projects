import { Component, OnInit } from '@angular/core';
import {RouterModule, Router} from '@angular/router';
import {HttpclientService,User,Role} from '../../services/httpclient.service';
import {AuthenticationService} from '../../services/authentication.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  constructor(private router: Router,private httpClientService:HttpclientService,private loginservice: AuthenticationService) { }
  userLogin:User =  new User("",null,null,"","","","","","","");
  userRegister:User =  new User("",new Role("","",[]),null,"","","","","","","");
  errorMessageSingIn:string = "";
  errorMessageSingUp="";
  ngOnInit() {
  }

  login()
  {
    if(this.userLogin.username == "" || this.userLogin.password== "")
    {
      this.errorMessageSingIn = "Complete all mandatory fields"
    }
    else
    {
    this.httpClientService.login(this.userLogin).subscribe(
      response=>{
        if(response == null)
        {
          this.errorMessageSingIn = "Invalid user or password";
        }
        else
        {
          sessionStorage.setItem("idUser",response.idUser.toString());
          sessionStorage.setItem("username",response.username.toString());
          sessionStorage.setItem("role",response.role.roleName.toString());
          this.router.navigate(["/"+response.role.roleName]);
          this.errorMessageSingIn = "";
        }
      }
    )
    }
  }

  register()
  {
    if(this.userRegister.username == "" || this.userRegister.password== ""
    || this.userRegister.name == "" || this.userRegister.email == ""
    || this.userRegister.birthdate == "" || this.userRegister.gender == ""
    ||  this.userRegister.address == "")
    {
      this.errorMessageSingUp = "Complete all mandatory fields"
    }
    else
    {
      this.httpClientService.isDuplicateCredential(this.userRegister).subscribe(
        response=>
        {
          if(response == true)
          {
            this.errorMessageSingUp = "Username or password already exists"
          }
          else
          {
            this.httpClientService.register(this.userRegister).subscribe(
              dataReg=>
              {
                    this.clearFields(this.userRegister);
                    this.errorMessageSingUp = "";
              }
            )
          }
        }
      )
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
