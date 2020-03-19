import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DoctorCaregiverComponent } from './doctor-caregiver.component';

describe('DoctorCaregiverComponent', () => {
  let component: DoctorCaregiverComponent;
  let fixture: ComponentFixture<DoctorCaregiverComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DoctorCaregiverComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DoctorCaregiverComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
