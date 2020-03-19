import { Injectable } from '@angular/core';
import { HttpClient,HttpHeaders,HttpParams } from '@angular/common/http';
import {Observable} from 'rxjs';

export class User{
  constructor(
  public idUser:String,
  public role:Role,
  public patient:Patient,
  public username:String,
  public password:String,
  public name:String,
  public email:String,
  public birthdate:String,
  public gender:String,
  public address:String,
){}
}

export class Role{
  constructor(
  public idRole:String,
  public roleName:String,
  public userList:User[],
){}
}

export class MonitorActivity{
  constructor(
  public idMonitorActivity:String,
  public startTime:String,
  public endTime:String,
  public activityName:String,
  public anomalous:boolean,
  public recommendation:String,
){}
}

export class Patient{
  constructor(
  public idPatient:String,
  public user:User,
  public doctor:User,
  public careGiver:User,
  public medicalRecord:String,
  public medicationPlanList:MedicationPlan[],
  public activitiesList:MonitorActivity[],
){}
}

export class Medication{
  constructor(
  public idMedication:String,
  public name:String,
  public sideEffects:String,
){}
}

export class MedicationPlan{
  constructor(
    public idMedicationPlan:String,
    public patient:Patient,
    public startTime:String,
    public endTime:String,
    public medicationRecipeList:MedicationRecipe[],
  ){}
}

export class MedicationRecipe{
  constructor(
    public idMedicationRecipe:String,
    public medication:Medication,
    public medicationPlan:MedicationPlan,
    public intakeMoments:String,
    public dosage:String,
  ){}
}

export class Activity{
  constructor(
    public activityName:any,
    public duration:any,
  ){}
}

export class ActivitiesDuration{
    constructor(
  public activitiesDuration:any,
  ){}
}

export class ActivityRequest{
    constructor(
  public idPatient:String,
  public startDate:String,
  ){}
}

export class RecommendationRequest{
    constructor(
  public idActivity:String,
  public recommendation:String,
  ){}
}

export class RecommendationResponse{
    constructor(
  public response:String,
  ){}
}

export class MedicationRequest{
    constructor(
  public idPatient:String,
  public medPlanDate:String,
  ){}
}

export class MedicationResponse{
    constructor(
  public medicationName:String[],
  ){}
}

@Injectable({
  providedIn: 'root'
})

export class HttpclientService {

  constructor(private httpClient:HttpClient) {
   }

   login(user:User)
   {
     return this.httpClient.post<User>('server/user/login',user);
   }

   register(user:User)
   {
     return this.httpClient.post<User>('server/user/register',user);
   }

   isDuplicateCredential(user:User)
   {
     return this.httpClient.post<Boolean>('server/user/isDuplicate',user);
   }

   getPatientById(id:String)
   {
       return this.httpClient.get<Patient>('server/patient/'+ id);
   }

   getAllPatientByCareGiver(id:String)
   {
      return this.httpClient.get<Patient[]>('server/patient/patientByCaregiver/'+ id);
   }

   getAllPatientByDoctorId(id:String)
   {
      return this.httpClient.get<Patient[]>('server/patient/patientByDoctor/'+ id);
   }

   getUsersByRole(roleName:String)
   {
     return this.httpClient.get<User[]>('server/user/userByRole/'+ roleName);
   }

   getAllMedication()
   {
      return this.httpClient.get<Medication[]>('server/medication/all');
   }

   addUserPatient(user:User)
   {
      return this.httpClient.post<String>('server/user/addUserPatient',user);
   }

   addPatient(patient:Patient,idDoctor:String, idPatient:String, usernameCaregiver:String)
   {
      return this.httpClient.post<String>('server/patient/addPatient/'+ idDoctor + '/' + idPatient + '/' + usernameCaregiver,patient);
   }

   addCaregiver(user:User)
   {
      return this.httpClient.post<String>('server/user/addCaregiver',user);
   }

   addMedicationPlan(medicationPlan:MedicationPlan)
   {
    return this.httpClient.post<String>('server/medication_plan/insert',medicationPlan);
   }

   addMedicationRecipe(medicationRecipe:MedicationRecipe,medicationPlanId:String,medicationId:String)
   {
     return this.httpClient.post<String>('server/medication_recipe/insert/'+ medicationPlanId + '/' + medicationId,medicationRecipe);
   }

   deletePatient(idPatient:String)
   {
      return this.httpClient.delete('server/patient/delete/'+ idPatient);
   }

   deleteUser(idUser:String)
   {
      return this.httpClient.delete('server/user/delete/'+ idUser);
   }

   addMedication(medication:Medication)
   {
     return this.httpClient.post<String>('server/medication/insert',medication);
   }

   deleteMedication(idMedication:String)
   {
       return this.httpClient.delete('server/medication/delete/'+ idMedication);
   }
   
   updateUser(user:User)
   {
       return this.httpClient.put<String>('server/user/update',user);
   }

   updateMedication(medication:Medication)
   {
       return this.httpClient.put<String>('server/medication/update',medication);
   }

   updatePatient(patient:Patient)
   {
       return this.httpClient.put<String>('server/patient/update',patient);
   }

   getActivitiesHistory(activitiesRequest:ActivityRequest)
   {
     return this.httpClient.post<ActivitiesDuration>('server/soap/history',activitiesRequest);
   }

   addRecommendation(recommendationRequest:RecommendationRequest)
   {
    return this.httpClient.post<RecommendationResponse>('server/soap/recommendation',recommendationRequest);
   }

   getNotTakenMed(medicationRequest:MedicationRequest)
   {
       return this.httpClient.post<MedicationResponse>('server/soap/medication',medicationRequest);
   }
}
