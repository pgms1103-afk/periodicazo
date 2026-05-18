import { Component, OnInit } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { PublicacionService } from '../../services/publicacion.service';
import { Publicacion } from '../../models/publicacion.model';

/**
 * Componente que gestiona el formulario para la creación y edición de predicciones astrales (Horóscopos).
 * @class EditorHoroscopos
 * @implements {OnInit}
 */
@Component({
  selector: 'app-editor-horoscopos',
  standalone: true,
  imports: [RouterModule, CommonModule, FormsModule],
  templateUrl: './editor-horoscopos.html',
  styleUrls: ['./editor-horoscopos.css']
})
export class EditorHoroscopos implements OnInit {
  /**
   * Título principal del horóscopo.
   * @type {string}
   */
  titulo: string = '';

  /**
   * Signo zodiacal al que aplica el horóscopo.
   * @type {string}
   */
  signo: string = 'Aries';

  /**
   * Elemento asociado al signo zodiacal (Fuego, Tierra, Aire, Agua).
   * @type {string}
   */
  elemento: string = 'Fuego';

  /**
   * Autor o redactor responsable de la predicción.
   * @type {string}
   */
  autor: string = '';

  /**
   * Sección o editorial a la que pertenece el horóscopo.
   * @type {string}
   */
  editorial: string = '';

  /**
   * Contenido detallado con el presagio o predicción.
   * @type {string}
   */
  contenido: string = '';

  /**
   * Almacena los datos del horóscopo en caso de estar en modo edición.
   * @type {Publicacion | null}
   */
  horoscopoExistente: Publicacion | null = null;

  /**
   * Bandera que indica si el formulario está en modo de edición (true) o creación (false).
   * @type {boolean}
   */
  modoEditar: boolean = false;

  /**
   * Mensaje de error para mostrar validaciones o fallos al usuario.
   * @type {string}
   */
  mensajeError: string = '';

  /**
   * Crea una instancia del componente EditorHoroscopos.
   * @param {PublicacionService} publicacionService - Servicio para gestionar las publicaciones.
   * @param {Router} router - Enrutador para gestionar la navegación y recuperar datos de estado.
   */
  constructor(private publicacionService: PublicacionService, private router: Router) {
    const navegacion = this.router.getCurrentNavigation();
    if (navegacion?.extras?.state && navegacion.extras.state['horoscopoAEditar']) {
      this.horoscopoExistente = navegacion.extras.state['horoscopoAEditar'];
      this.modoEditar = true;
    }
  }

  /**
   * Inicializa el componente.
   * Si está en modo edición, pre-llena los campos del formulario con los datos del horóscopo existente.
   * @returns {void}
   */
  ngOnInit(): void {
    if (this.modoEditar && this.horoscopoExistente) {
      this.titulo = this.horoscopoExistente.titulo;
      this.signo = this.horoscopoExistente.signo || 'Aries';
      this.elemento = this.horoscopoExistente.elemento || 'Fuego';
      this.autor = this.horoscopoExistente.autor || '';
      this.editorial = this.horoscopoExistente.editorial || '';
      this.contenido = this.horoscopoExistente.contenido;
    }
  }

  /**
   * Valida y guarda el horóscopo en el sistema.
   * Si está en modo edición, actualiza el registro; de lo contrario, crea uno nuevo.
   * Navega a la lista de horóscopos si la operación es exitosa.
   * @returns {void}
   */
  guardarHoroscopo(): void {
    this.mensajeError = '';

    if (!this.titulo || !this.contenido) {
      this.mensajeError = 'El título astral y el presagio son obligatorios.';
      return;
    }

    const datos: any = {
      tipo: 'HOROSCOPO',
      titulo: this.titulo,
      signo: this.signo,
      elemento: this.elemento,
      autor: this.autor,
      editorial: this.editorial,
      contenido: this.contenido
    };

    if (this.modoEditar && this.horoscopoExistente?.id) {
      datos.fecha = this.horoscopoExistente.fecha;

      this.publicacionService.actualizarPublicacion(this.horoscopoExistente.id, datos as Publicacion).subscribe({
        next: () => this.router.navigate(['/horoscopos']),
        error: () => this.mensajeError = 'Fallo al actualizar la predicción.'
      });
    } else {
      this.publicacionService.crearPublicacion(datos as Publicacion).subscribe({
        next: () => this.router.navigate(['/horoscopos']),
        error: () => this.mensajeError = 'Fallo al guardar la predicción.'
      });
    }
  }
}
