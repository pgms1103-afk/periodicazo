import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ListaHoroscopos } from './lista-horoscopos';

describe('ListaHoroscopos', () => {
  let component: ListaHoroscopos;
  let fixture: ComponentFixture<ListaHoroscopos>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ListaHoroscopos],
    }).compileComponents();

    fixture = TestBed.createComponent(ListaHoroscopos);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
