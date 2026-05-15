import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EditorNoticias } from './editor-noticias';

describe('EditorNoticias', () => {
  let component: EditorNoticias;
  let fixture: ComponentFixture<EditorNoticias>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EditorNoticias],
    }).compileComponents();

    fixture = TestBed.createComponent(EditorNoticias);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
