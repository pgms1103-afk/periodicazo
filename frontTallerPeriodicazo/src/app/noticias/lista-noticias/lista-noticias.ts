import {Component, OnInit} from '@angular/core';
import {RouterModule, Router} from '@angular/router';
import {CommonModule} from '@angular/common';
import {PublicacionService} from '../../services/publicacion.service';
import {Publicacion} from '../../models/publicacion.model';

/**
 * Componente que muestra y gestiona la lista de artículos de noticias.
 * Permite visualizar el listado, acceder a la lectura completa, editar, eliminar y ver comentarios.
 * @class ListaNoticias
 * @implements {OnInit}
 */
@Component({
  selector: 'app-lista-noticias',
  standalone: true,
  imports: [RouterModule, CommonModule],
  templateUrl: './lista-noticias.html',
  styleUrls: ['./lista-noticias.css']
})
export class ListaNoticias implements OnInit {
  /**
   * Arreglo que almacena la lista de noticias obtenidas del servidor.
   * @type {Publicacion[]}
   */
  noticias: Publicacion[] = [];

  /**
   * Mensaje para notificar al usuario sobre errores durante la carga o ejecución de acciones.
   * @type {string}
   */
  mensajeError: string = '';

  /**
   * Rol del usuario autenticado actualmente en la sesión.
   * @type {string}
   */
  rolActual: string = '';

  /**
   * Crea una instancia del componente ListaNoticias.
   * @param {PublicacionService} publicacionService - Servicio para consumir la API de publicaciones.
   * @param {Router} router - Servicio de enrutamiento para la navegación entre componentes.
   */
  constructor(private publicacionService: PublicacionService, private router: Router) {
  }

  /**
   * Método del ciclo de vida de Angular que se ejecuta al iniciar el componente.
   * Asigna el rol actual del usuario y carga la lista de noticias.
   * @returns {void}
   */
  ngOnInit(): void {
    this.rolActual = localStorage.getItem('rol_diario') || '';
    this.cargarNoticias();
  }

  /**
   * Obtiene las noticias desde el servicio backend filtrando por el tipo 'NOTICIA'.
   * En caso de error, muestra un mensaje informativo al usuario.
   * @returns {void}
   */
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

  /**
   * Navega a la vista del editor pasando la información de la noticia seleccionada para su edición.
   * @param {Publicacion} noticia - La publicación tipo noticia que se desea editar.
   * @returns {void}
   */
  editarNoticia(noticia: Publicacion) {
    this.router.navigate(['/editor-noticias'], {state: {noticiaAEditar: noticia}});
  }

  /**
   * Elimina una noticia específica del sistema tras confirmación del usuario.
   * Actualiza la lista si la eliminación fue exitosa.
   * @param {number | undefined} id - El identificador único de la noticia a eliminar.
   * @returns {void}
   */
  eliminarNoticia(id: number | undefined) {
    if (!id) return;
    if (confirm('¿Está seguro de que desea retirar este artículo de la hemeroteca?')) {
      this.publicacionService.eliminarPublicacion(id).subscribe({
        next: () => this.cargarNoticias(),
        error: () => this.mensajeError = 'Fallo al intentar eliminar la noticia.'
      });
    }
  }

  /**
   * Navega a la vista de lectura detallada de la noticia seleccionada.
   * @param {Publicacion} noticia - La noticia que el usuario desea leer completamente.
   * @returns {void}
   */
  leerCompleto(noticia: Publicacion) {
    this.router.navigate(['/lectura-noticia'], {state: {articulo: noticia}});
  }

  /**
   * Navega a la sección de comentarios de la noticia seleccionada.
   * @param {Publicacion} noticia - La noticia de la cual se desean ver o añadir comentarios.
   * @returns {void}
   */
  irAComentarios(noticia: Publicacion) {
    this.router.navigate(['/comentarios'], {state: {articulo: noticia}});
  }
}
