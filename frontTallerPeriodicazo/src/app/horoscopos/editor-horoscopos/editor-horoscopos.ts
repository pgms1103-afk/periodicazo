import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { ComentariosNoticias } from '../../noticias/comentarios-noticias/comentarios-noticias';

@Component({
  selector: 'app-editor-horoscopos',
  standalone: true,
  imports: [RouterModule, CommonModule, ComentariosNoticias],
  templateUrl: './editor-horoscopos.html',
  styleUrls: ['./editor-horoscopos.css']
})
export class EditorHoroscopos {}
