import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ComentariosHoroscopos } from './comentarios-horoscopos';

describe('ComentariosHoroscopos', () => {
  let component: ComentariosHoroscopos;
  let fixture: ComponentFixture<ComentariosHoroscopos>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ComentariosHoroscopos],
    }).compileComponents();

    fixture = TestBed.createComponent(ComentariosHoroscopos);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
