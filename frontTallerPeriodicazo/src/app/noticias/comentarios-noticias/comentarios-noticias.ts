import { Component, Input, OnInit, OnChanges, SimpleChanges } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { ComentarioService } from '../../services/comentario.service';
import { Comentario } from '../../models/comentario.model';
import { Publicacion } from '../../models/publicacion.model';

@Component({
  selector: 'app-comentarios-noticias',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './comentarios-noticias.html',
  styleUrls: ['./comentarios-noticias.css']
})
export class ComentariosNoticias implements OnInit, OnChanges {
  @Input() articuloId?: number;
  @Input() articuloTitulo?: string;

  comentarios: Comentario[] = [];
  nuevoContenido: string = '';
  mensajeError: string = '';

  esPantallaIndependiente: boolean = false;
  rolActual: string = '';

  constructor(private comentarioService: ComentarioService, private router: Router) {
    const navegacion = this.router.getCurrentNavigation();
    if (navegacion?.extras?.state && navegacion.extras.state['articulo']) {
      const articulo: Publicacion = navegacion.extras.state['articulo'];
      this.articuloId = articulo.id;
      this.articuloTitulo = articulo.titulo;
      this.esPantallaIndependiente = true;
    }
  }

  ngOnInit(): void {
    this.rolActual = localStorage.getItem('rol_diario') || '';
    if (this.esPantallaIndependiente && !this.articuloId) {
      this.router.navigate(['/noticias']);
      return;
    }
    this.cargarComentarios();
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['articuloTitulo'] && !changes['articuloTitulo'].firstChange) {
      this.cargarComentarios();
    }
  }

  cargarComentarios(): void {
    if (!this.articuloTitulo) return;

    this.comentarioService.obtenerPorTitulo(this.articuloTitulo).subscribe({
      next: (datos) => { this.comentarios = datos || []; },
      error: (err) => { console.error("Error consultando el archivo de comentarios", err); }
    });
  }

  publicarComentario(): void {
    this.mensajeError = '';

    if (!this.nuevoContenido || !this.nuevoContenido.trim()) {
      this.mensajeError = 'El cuerpo de la carta no puede estar vacio.';
      return;
    }

    if (!this.articuloId) {
      this.mensajeError = 'Error: No se pudo identificar el articulo actual.';
      return;
    }

    const nombreUsuario = localStorage.getItem('usuario_diario') || 'Lector Anonimo';

    const nuevoComentario: Comentario = {
      contenido: this.nuevoContenido,
      publicacionId: this.articuloId,
      nombreComentador: nombreUsuario,
      fecha: new Date().toISOString()
    };

    this.comentarioService.crearComentario(nuevoComentario).subscribe({
      next: () => {
        this.nuevoContenido = '';
        this.cargarComentarios();
      },
      error: () => {
        this.mensajeError = 'Fallo interno al procesar su correspondencia.';
      }
    });
  }
}
