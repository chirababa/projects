import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { MatTabsModule } from '@angular/material';
import { AngularBillboardModule } from 'angular-billboard';
import { GoogleChartsModule } from 'angular-google-charts';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LoginComponent } from './components/login/login.component';
import { CaregiverComponent } from './components/caregiver/caregiver.component';
import { DoctorComponent } from './components/doctor/doctor.component';
import { PatientComponent } from './components/patient/patient.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { DoctorPatientComponent } from './components/doctor-patient/doctor-patient.component';
import { DoctorCaregiverComponent } from './components/doctor-caregiver/doctor-caregiver.component';
import { DoctorMedicationComponent } from './components/doctor-medication/doctor-medication.component';
import { DoctorMedicationPlanComponent } from './components/doctor-medication-plan/doctor-medication-plan.component';
import { Ng2GoogleChartsModule } from 'ng2-google-charts';
import { PatientHistoryComponent } from './components/patient-history/patient-history.component';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    CaregiverComponent,
    DoctorComponent,
    PatientComponent,
    DoctorPatientComponent,
    DoctorCaregiverComponent,
    DoctorMedicationComponent,
    DoctorMedicationPlanComponent,
    PatientHistoryComponent,
  ],
  imports: [
    HttpClientModule,
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    MatTabsModule,
    BrowserAnimationsModule,
    AngularBillboardModule,
    //GoogleChartsModule,
    Ng2GoogleChartsModule,
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
