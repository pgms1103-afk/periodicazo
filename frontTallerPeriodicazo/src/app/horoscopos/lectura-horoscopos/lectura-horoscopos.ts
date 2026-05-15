import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { ComentariosNoticias } from '../../noticias/comentarios-noticias/comentarios-noticias';

@Component({
  selector: 'app-lectura-horoscopo',
  standalone: true,
  imports: [RouterModule, CommonModule, ComentariosNoticias],
  templateUrl: './lectura-horoscopos.html',
  styleUrls: ['./lectura-horoscopos.css']
})
export class LecturaHoroscopo {}
