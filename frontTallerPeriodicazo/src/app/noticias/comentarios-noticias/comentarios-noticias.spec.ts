import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ComentariosNoticias } from './comentarios-noticias';

describe('ComentariosNoticias', () => {
  let component: ComentariosNoticias;
  let fixture: ComponentFixture<ComentariosNoticias>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ComentariosNoticias],
    }).compileComponents();

    fixture = TestBed.createComponent(ComentariosNoticias);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
