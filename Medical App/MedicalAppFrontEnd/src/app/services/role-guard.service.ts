import { Injectable } from '@angular/core';
import {ActivatedRouteSnapshot, CanActivate, Router} from "@angular/router";

@Injectable({
  providedIn: 'root'
})
export class RoleGuardService implements CanActivate {

  constructor(public router: Router) { }

  canActivate(route: ActivatedRouteSnapshot): boolean {
    const expectedRole = route.data.expectedRole;
    if (!sessionStorage.getItem("username") || sessionStorage.getItem("role") !== expectedRole) {
      this.router.navigate(["/"+sessionStorage.getItem("role")]);
      return false;
    }
    return true;
  }

}
