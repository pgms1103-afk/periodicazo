import { Component, OnInit } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { Publicacion } from '../../models/publicacion.model';
import { ComentariosNoticias } from '../comentarios-noticias/comentarios-noticias';

@Component({
  selector: 'app-lectura-noticias',
  standalone: true,
  imports: [CommonModule, RouterModule, ComentariosNoticias],
  templateUrl: './lectura-noticias.html',
  styleUrls: ['./lectura-noticias.css']
})
export class LecturaNoticias implements OnInit {
  articulo: Publicacion | null = null;

  constructor(private router: Router) {
    const navegacion = this.router.getCurrentNavigation();
    if (navegacion?.extras?.state && navegacion.extras.state['articulo']) {
      this.articulo = navegacion.extras.state['articulo'];
    }
  }

  ngOnInit(): void {
    if (!this.articulo) {
      this.router.navigate(['/noticias']);
    }
  }
}
