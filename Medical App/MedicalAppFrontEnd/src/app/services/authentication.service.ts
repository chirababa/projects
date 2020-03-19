import { Injectable } from '@angular/core';
import { HttpClient} from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {

  constructor() { }

  isUserLoggedIn() {
     let user = sessionStorage.getItem('username');
     return !(user == null);
   }

   logOut() {
    sessionStorage.removeItem('idUser');
    sessionStorage.removeItem('username');
    sessionStorage.removeItem('role');
  }
}
