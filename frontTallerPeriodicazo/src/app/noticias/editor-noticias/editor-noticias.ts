import { Component, OnInit } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { PublicacionService } from '../../services/publicacion.service';
import { Publicacion } from '../../models/publicacion.model';

@Component({
  selector: 'app-editor-noticias',
  standalone: true,
  imports: [RouterModule, CommonModule, FormsModule],
  templateUrl: './editor-noticias.html',
  styleUrls: ['./editor-noticias.css']
})
export class EditorNoticias implements OnInit {
  titulo: string = '';
  categoria: string = 'Política';
  autor: string = '';
  editorial: string = '';
  contenido: string = '';

  noticiaExistente: Publicacion | null = null;
  modoEditar: boolean = false;
  mensajeError: string = '';

  constructor(private publicacionService: PublicacionService, private router: Router) {
    const navegacion = this.router.getCurrentNavigation();
    if (navegacion?.extras?.state && navegacion.extras.state['noticiaAEditar']) {
      this.noticiaExistente = navegacion.extras.state['noticiaAEditar'];
      this.modoEditar = true;
    }
  }

  ngOnInit(): void {
    if (this.modoEditar && this.noticiaExistente) {
      this.titulo = this.noticiaExistente.titulo;
      this.categoria = this.noticiaExistente.categoria || 'Política';
      this.autor = this.noticiaExistente.autor || '';
      this.editorial = this.noticiaExistente.editorial || '';
      this.contenido = this.noticiaExistente.contenido;
    }
  }

  guardarPublicacion(): void {
    this.mensajeError = '';

    if (!this.titulo || !this.contenido) {
      this.mensajeError = 'El titular y el contenido son obligatorios para imprimir.';
      return;
    }

    const datos: any = {
      tipo: 'NOTICIA',
      titulo: this.titulo,
      categoria: this.categoria,
      autor: this.autor,
      editorial: this.editorial,
      contenido: this.contenido
    };

    if (this.modoEditar && this.noticiaExistente?.id) {
      datos.fecha = this.noticiaExistente.fecha;

      this.publicacionService.actualizarPublicacion(this.noticiaExistente.id, datos as Publicacion).subscribe({
        next: () => this.router.navigate(['/noticias']),
        error: () => this.mensajeError = 'Fallo al actualizar el artículo.'
      });
    } else {

      this.publicacionService.crearPublicacion(datos as Publicacion).subscribe({
        next: () => this.router.navigate(['/noticias']),
        error: () => this.mensajeError = 'Fallo al guardar el artículo en los rotativos.'
      });
    }
  }
}
