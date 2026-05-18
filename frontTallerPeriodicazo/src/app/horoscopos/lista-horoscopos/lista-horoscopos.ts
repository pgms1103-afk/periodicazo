import { Component, OnInit } from '@angular/core';
import { RouterModule, Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { PublicacionService } from '../../services/publicacion.service';
import { Publicacion } from '../../models/publicacion.model';

/**
 * Componente que muestra y gestiona la lista de horóscopos (predicciones astrales).
 * Permite visualizar, editar, eliminar y acceder a los detalles y comentarios de cada publicación.
 * @class ListaHoroscopos
 * @implements {OnInit}
 */
@Component({
  selector: 'app-lista-horoscopos',
  standalone: true,
  imports: [RouterModule, CommonModule],
  templateUrl: './lista-horoscopos.html',
  styleUrls: ['./lista-horoscopos.css']
})
export class ListaHoroscopos implements OnInit {
  /**
   * Arreglo que almacena la lista de horóscopos obtenidos del servidor.
   * @type {Publicacion[]}
   */
  horoscopos: Publicacion[] = [];

  /**
   * Mensaje para notificar al usuario sobre errores durante las operaciones.
   * @type {string}
   */
  mensajeError: string = '';

  /**
   * Rol del usuario autenticado actualmente en la sesión.
   * @type {string}
   */
  rolActual: string = '';

  /**
   * Crea una instancia de ListaHoroscopos.
   * @param {PublicacionService} publicacionService - Servicio para consumir la API de publicaciones.
   * @param {Router} router - Servicio de enrutamiento para la navegación entre componentes.
   */
  constructor(private publicacionService: PublicacionService, private router: Router) {}

  /**
   * Método del ciclo de vida de Angular que se ejecuta al iniciar el componente.
   * Asigna el rol actual del usuario y carga la lista de horóscopos.
   * @returns {void}
   */
  ngOnInit(): void {
    this.rolActual = localStorage.getItem('rol_diario') || '';
    this.cargarHoroscopos();
  }

  /**
   * Obtiene los horóscopos desde el servicio backend y los asigna a la variable local.
   * En caso de error, muestra un mensaje informativo al usuario.
   * @returns {void}
   */
  cargarHoroscopos(): void {
    this.mensajeError = '';
    this.publicacionService.obtenerPorTipo('HOROSCOPO').subscribe({
      next: (datos) => {
        this.horoscopos = datos || [];
      },
      error: (err) => {
        this.mensajeError = 'Error al consultar las estrellas. No se pudieron cargar los horóscopos.';
      }
    });
  }

  /**
   * Navega a la vista del editor de horóscopos pasando la información del horóscopo seleccionado.
   * @param {Publicacion} horoscopo - La publicación tipo horóscopo que se desea editar.
   * @returns {void}
   */
  editarHoroscopo(horoscopo: Publicacion) {
    this.router.navigate(['/editor-horoscopos'], { state: { horoscopoAEditar: horoscopo } });
  }

  /**
   * Elimina un horóscopo específico del sistema tras confirmación del usuario.
   * Actualiza la lista si la eliminación fue exitosa.
   * @param {number | undefined} id - El identificador único del horóscopo a eliminar.
   * @returns {void}
   */
  eliminarHoroscopo(id: number | undefined) {
    if (!id) return;
    if (confirm('¿Está seguro de que desea retirar esta predicción astral de los registros?')) {
      this.publicacionService.eliminarPublicacion(id).subscribe({
        next: () => this.cargarHoroscopos(),
        error: () => this.mensajeError = 'Fallo al intentar eliminar el horóscopo.'
      });
    }
  }

  /**
   * Navega a la vista de lectura detallada del horóscopo seleccionado.
   * @param {Publicacion} horoscopo - El horóscopo que el usuario desea leer completamente.
   * @returns {void}
   */
  leerCompleto(horoscopo: Publicacion) {
    this.router.navigate(['/lectura-horoscopo'], { state: { articulo: horoscopo } });
  }

  /**
   * Navega a la sección de comentarios del horóscopo seleccionado.
   * @param {Publicacion} horoscopo - El horóscopo del cual se desean ver o añadir comentarios.
   * @returns {void}
   */
  irAComentarios(horoscopo: Publicacion) {
    this.router.navigate(['/comentarios'], { state: { articulo: horoscopo } });
  }

  /**
   * Obtiene el emoji representativo del signo zodiacal proporcionado.
   * @param {string | undefined} signo - El nombre del signo zodiacal.
   * @returns {string} El emoji del signo, o unas estrellas por defecto ('✨') si no existe.
   */
  obtenerIcono(signo: string | undefined): string {
    const iconos: { [key: string]: string } = {
      'Aries': '♈', 'Tauro': '♉', 'Geminis': '♊', 'Cancer': '♋',
      'Leo': '♌', 'Virgo': '♍', 'Libra': '♎', 'Escorpio': '♏',
      'Sagitario': '♐', 'Capricornio': '♑', 'Acuario': '♒', 'Piscis': '♓'
    };
    return signo ? (iconos[signo] || '✨') : '✨';
  }
}
