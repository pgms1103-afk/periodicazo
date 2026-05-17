import {Component, OnInit} from '@angular/core';
import {RouterModule, Router} from '@angular/router';
import {CommonModule} from '@angular/common';
import {PublicacionService} from '../../services/publicacion.service';
import {Publicacion} from '../../models/publicacion.model';

@Component({
  selector: 'app-lista-noticias',
  standalone: true,
  imports: [RouterModule, CommonModule],
  templateUrl: './lista-noticias.html',
  styleUrls: ['./lista-noticias.css']
})
export class ListaNoticias implements OnInit {
  noticias: Publicacion[] = [];
  mensajeError: string = '';
  rolActual: string = '';

  constructor(private publicacionService: PublicacionService, private router: Router) {
  }

  ngOnInit(): void {
    this.rolActual = localStorage.getItem('rol_diario') || '';
    this.cargarNoticias();
  }

  cargarNoticias(): void {
    this.mensajeError = '';
    this.publicacionService.obtenerPorTipo('NOTICIA').subscribe({
      next: (datos) => {
        this.noticias = datos || [];
      },
      error: (err) => {
        this.mensajeError = 'Error en los rotativos al cargar los titulares.';
      }
    });
  }

  editarNoticia(noticia: Publicacion) {
    this.router.navigate(['/editor-noticias'], {state: {noticiaAEditar: noticia}});
  }

  eliminarNoticia(id: number | undefined) {
    if (!id) return;
    if (confirm('¿Está seguro de que desea retirar este artículo de la hemeroteca?')) {
      this.publicacionService.eliminarPublicacion(id).subscribe({
        next: () => this.cargarNoticias(),
        error: () => this.mensajeError = 'Fallo al intentar eliminar la noticia.'
      });
    }
  }

  leerCompleto(noticia: Publicacion) {
    this.router.navigate(['/lectura-noticia'], {state: {articulo: noticia}});
  }

  irAComentarios(noticia: Publicacion) {
    this.router.navigate(['/comentarios'], {state: {articulo: noticia}});
  }
}

