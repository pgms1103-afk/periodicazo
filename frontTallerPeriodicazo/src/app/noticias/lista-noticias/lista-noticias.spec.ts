import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ListaNoticias } from './lista-noticias';

describe('ListaNoticias', () => {
  let component: ListaNoticias;
  let fixture: ComponentFixture<ListaNoticias>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ListaNoticias],
    }).compileComponents();

    fixture = TestBed.createComponent(ListaNoticias);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
