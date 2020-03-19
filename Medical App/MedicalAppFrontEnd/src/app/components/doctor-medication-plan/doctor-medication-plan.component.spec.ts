import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DoctorMedicationPlanComponent } from './doctor-medication-plan.component';

describe('DoctorMedicationPlanComponent', () => {
  let component: DoctorMedicationPlanComponent;
  let fixture: ComponentFixture<DoctorMedicationPlanComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DoctorMedicationPlanComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DoctorMedicationPlanComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
