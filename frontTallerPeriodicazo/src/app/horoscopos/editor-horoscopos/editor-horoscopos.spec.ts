import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EditorHoroscopos } from './editor-horoscopos';

describe('EditorHoroscopos', () => {
  let component: EditorHoroscopos;
  let fixture: ComponentFixture<EditorHoroscopos>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EditorHoroscopos],
    }).compileComponents();

    fixture = TestBed.createComponent(EditorHoroscopos);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
