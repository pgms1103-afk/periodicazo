import { Component, Input, OnInit, OnChanges, SimpleChanges } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { ComentarioService } from '../../services/comentario.service';
import { Comentario } from '../../models/comentario.model';
import { Publicacion } from '../../models/publicacion.model';

/**
 * Componente que gestiona la visualización, creación, edición y eliminación de comentarios
 * asociados a una publicación específica. Puede funcionar de manera integrada o como una vista independiente.
 * @class ComentariosNoticias
 * @implements {OnInit}
 * @implements {OnChanges}
 */
@Component({
  selector: 'app-comentarios-noticias',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './comentarios-noticias.html',
  styleUrls: ['./comentarios-noticias.css']
})
export class ComentariosNoticias implements OnInit, OnChanges {
  /**
   * Identificador de la publicación a la que pertenecen los comentarios.
   * @type {number | undefined}
   */
  @Input() articuloId?: number;

  /**
   * Título de la publicación a la que pertenecen los comentarios.
   * @type {string | undefined}
   */
  @Input() articuloTitulo?: string;

  /**
   * Lista de comentarios asociados al artículo actual.
   * @type {Comentario[]}
   */
  comentarios: Comentario[] = [];

  /**
   * Contenido del nuevo comentario que el usuario está redactando.
   * @type {string}
   */
  nuevoContenido: string = '';

  /**
   * Mensaje de error para mostrar notificaciones al usuario.
   * @type {string}
   */
  mensajeError: string = '';

  /**
   * Indica si el componente se está renderizando como una página independiente (ruta completa)
   * o si está incrustado dentro de otra vista.
   * @type {boolean}
   */
  esPantallaIndependiente: boolean = false;

  /**
   * Rol del usuario autenticado actualmente.
   * @type {string}
   */
  rolActual = '';

  /**
   * Identificador del comentario que se encuentra actualmente en modo de edición.
   * @type {number | undefined}
   */
  comentarioEnEdicionId?: number;

  /**
   * Contenido temporal del comentario mientras está siendo editado.
   * @type {string}
   */
  contenidoEditado = '';

  /**
   * Crea una instancia del componente ComentariosNoticias.
   * Verifica si se pasaron datos a través del estado de la navegación.
   * @param {ComentarioService} comentarioService - Servicio para gestionar operaciones de comentarios.
   * @param {Router} router - Servicio de enrutamiento para gestionar la navegación.
   */
  constructor(private comentarioService: ComentarioService, private router: Router) {
    const navegacion = this.router.getCurrentNavigation();
    if (navegacion?.extras?.state && navegacion.extras.state['articulo']) {
      const articulo: Publicacion = navegacion.extras.state['articulo'];
      this.articuloId = articulo.id;
      this.articuloTitulo = articulo.titulo;
      this.esPantallaIndependiente = true;
    }
  }

  /**
   * Método del ciclo de vida que se ejecuta al iniciar el componente.
   * Obtiene el rol del usuario y carga los comentarios si hay un artículo definido.
   * @returns {void}
   */
  ngOnInit(): void {
    this.rolActual = localStorage.getItem('rol_diario') || '';
    if (this.esPantallaIndependiente && !this.articuloId) {
      this.router.navigate(['/noticias']);
      return;
    }
    this.cargarComentarios();
  }

  /**
   * Método del ciclo de vida que se ejecuta cuando cambian las propiedades de entrada (@Input).
   * Recarga los comentarios si el título del artículo cambia.
   * @param {SimpleChanges} changes - Objeto con los cambios detectados en las propiedades de entrada.
   * @returns {void}
   */
  ngOnChanges(changes: SimpleChanges): void {
    if (changes['articuloTitulo'] && !changes['articuloTitulo'].firstChange) {
      this.cargarComentarios();
    }
  }

  /**
   * Consulta el servicio para obtener la lista de comentarios basándose en el título del artículo.
   * @returns {void}
   */
  cargarComentarios(): void {
    if (!this.articuloTitulo) return;

    this.comentarioService.obtenerPorTitulo(this.articuloTitulo).subscribe({
      next: (datos) => { this.comentarios = datos || []; },
      error: (err) => { console.error("Error consultando el archivo de comentarios", err); }
    });
  }

  /**
   * Envía un nuevo comentario al servidor asociado al artículo actual.
   * @returns {void}
   */
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
      error: () => { this.mensajeError = 'Fallo interno al procesar su correspondencia.'; }
    });
  }

  /**
   * Habilita el modo de edición para un comentario específico.
   * @param {Comentario} c - El comentario que se desea editar.
   * @returns {void}
   */
  iniciarEdicion(c: Comentario): void {
    this.comentarioEnEdicionId = c.id;
    this.contenidoEditado = c.contenido;
    this.mensajeError = '';
  }

  /**
   * Cancela el modo de edición actual y limpia los datos temporales.
   * @returns {void}
   */
  cancelarEdicion(): void {
    this.comentarioEnEdicionId = undefined;
    this.contenidoEditado = '';
    this.mensajeError = '';
  }

  /**
   * Guarda las modificaciones realizadas a un comentario existente.
   * @param {Comentario} c - El comentario original que está siendo actualizado.
   * @returns {void}
   */
  guardarEdicion(c: Comentario): void {
    if (!this.contenidoEditado || !this.contenidoEditado.trim()) {
      this.mensajeError = 'La correspondencia no puede quedar en blanco.';
      return;
    }
    if (!c.id) return;

    const comentarioActualizado: Comentario = {
      ...c,
      contenido: this.contenidoEditado,
      fecha: new Date().toISOString()
    };

    this.comentarioService.actualizarComentario(c.id, comentarioActualizado).subscribe({
      next: () => {
        this.cancelarEdicion();
        this.cargarComentarios();
      },
      error: () => { this.mensajeError = 'Error en imprenta al actualizar el comentario.'; }
    });
  }

  /**
   * Elimina un comentario permanentemente del sistema, previa confirmación del usuario.
   * @param {number | undefined} id - El identificador único del comentario a eliminar.
   * @returns {void}
   */
  borrarComentario(id: number | undefined): void {
    if (!id) return;
    if (window.confirm('¿Está seguro de retirar definitivamente esta correspondencia del diario?')) {
      this.comentarioService.eliminarComentario(id).subscribe({
        next: () => { this.cargarComentarios(); },
        error: () => { this.mensajeError = 'Error al intentar eliminar el comentario.'; }
      });
    }
  }
}
