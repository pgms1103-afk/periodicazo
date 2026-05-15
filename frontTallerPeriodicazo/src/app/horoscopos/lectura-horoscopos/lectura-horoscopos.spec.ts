import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LecturaHoroscopos } from './lectura-horoscopos';

describe('LecturaHoroscopos', () => {
  let component: LecturaHoroscopos;
  let fixture: ComponentFixture<LecturaHoroscopos>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [LecturaHoroscopos],
    }).compileComponents();

    fixture = TestBed.createComponent(LecturaHoroscopos);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
