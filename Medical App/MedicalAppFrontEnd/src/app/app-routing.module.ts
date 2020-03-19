import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import {LoginComponent} from './components/login/login.component'
import {CaregiverComponent} from './components/caregiver/caregiver.component'
import {DoctorComponent} from './components/doctor/doctor.component'
import {PatientComponent} from './components/patient/patient.component'
import {AuthGuardService} from './services/auth-guard.service';
import {RoleGuardService} from './services/role-guard.service';
import {PatientHistoryComponent} from './components/patient-history/patient-history.component'

const routes: Routes = [
  {
    path:'',
    component: LoginComponent,
  },
  {
    path:'caregiver',
    component: CaregiverComponent,
    canActivate:[AuthGuardService,RoleGuardService],
    data:{expectedRole:'caregiver'},
  },
  {
    path:'doctor',
    component: DoctorComponent,
    canActivate:[AuthGuardService,RoleGuardService],
    data:{expectedRole:'doctor'},
  },
  {
    path:'patient',
    component: PatientComponent,
    canActivate:[AuthGuardService,RoleGuardService],
    data:{expectedRole:'patient'},
  },

  {
    path:'history',
    component: PatientHistoryComponent,
    canActivate:[AuthGuardService,RoleGuardService],
    data:{expectedRole:'doctor'},
  },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
