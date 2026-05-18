import { Component, OnInit } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { PublicacionService } from '../../services/publicacion.service';
import { Publicacion } from '../../models/publicacion.model';

/**
 * Componente que gestiona el formulario para crear y editar artículos de noticias.
 * @class EditorNoticias
 * @implements {OnInit}
 */
@Component({
  selector: 'app-editor-noticias',
  standalone: true,
  imports: [RouterModule, CommonModule, FormsModule],
  templateUrl: './editor-noticias.html',
  styleUrls: ['./editor-noticias.css']
})
export class EditorNoticias implements OnInit {
  /**
   * Titular principal de la noticia.
   * @type {string}
   */
  titulo: string = '';

  /**
   * Categoría temática del artículo (ej. Política, Economía).
   * @type {string}
   */
  categoria: string = 'Política';

  /**
   * Nombre del periodista o redactor.
   * @type {string}
   */
  autor: string = '';

  /**
   * Editorial o sección del diario a la que pertenece el artículo.
   * @type {string}
   */
  editorial: string = '';

  /**
   * Cuerpo principal del texto de la noticia.
   * @type {string}
   */
  contenido: string = '';

  /**
   * Objeto que contiene los datos de la noticia si se está editando una existente.
   * @type {Publicacion | null}
   */
  noticiaExistente: Publicacion | null = null;

  /**
   * Indica si el formulario se encuentra en modo edición (true) o creación (false).
   * @type {boolean}
   */
  modoEditar: boolean = false;

  /**
   * Mensaje de error para informar al usuario de problemas de validación o del servidor.
   * @type {string}
   */
  mensajeError: string = '';

  /**
   * Crea una instancia de EditorNoticias.
   * Revisa el estado de la navegación para determinar si debe cargar una noticia a editar.
   * @param {PublicacionService} publicacionService - Servicio de conexión con la API de publicaciones.
   * @param {Router} router - Servicio de enrutamiento para navegar tras guardar.
   */
  constructor(private publicacionService: PublicacionService, private router: Router) {
    const navegacion = this.router.getCurrentNavigation();
    if (navegacion?.extras?.state && navegacion.extras.state['noticiaAEditar']) {
      this.noticiaExistente = navegacion.extras.state['noticiaAEditar'];
      this.modoEditar = true;
    }
  }

  /**
   * Inicializa el componente y carga los datos de la noticia en el formulario
   * si el componente se encuentra en modo de edición.
   * @returns {void}
   */
  ngOnInit(): void {
    if (this.modoEditar && this.noticiaExistente) {
      this.titulo = this.noticiaExistente.titulo;
      this.categoria = this.noticiaExistente.categoria || 'Política';
      this.autor = this.noticiaExistente.autor || '';
      this.editorial = this.noticiaExistente.editorial || '';
      this.contenido = this.noticiaExistente.contenido;
    }
  }

  /**
   * Valida los campos obligatorios y envía la información al servicio correspondiente.
   * Llama al método de actualizar o crear dependiendo del modo actual del componente.
   * Redirige al listado de noticias en caso de éxito.
   * @returns {void}
   */
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
