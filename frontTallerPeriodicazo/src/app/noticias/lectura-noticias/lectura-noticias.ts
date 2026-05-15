import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { ComentariosNoticias } from '../comentarios-noticias/comentarios-noticias';

@Component({
  selector: 'app-lectura-noticia',
  standalone: true,
  imports: [RouterModule, CommonModule, ComentariosNoticias],
  templateUrl: './lectura-noticias.html',
  styleUrls: ['./lectura-noticias.css']
})
export class LecturaNoticia {}
