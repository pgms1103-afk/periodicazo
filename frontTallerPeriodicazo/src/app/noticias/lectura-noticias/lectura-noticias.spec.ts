import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LecturaNoticias } from './lectura-noticias';

describe('LecturaNoticias', () => {
  let component: LecturaNoticias;
  let fixture: ComponentFixture<LecturaNoticias>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [LecturaNoticias],
    }).compileComponents();

    fixture = TestBed.createComponent(LecturaNoticias);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
