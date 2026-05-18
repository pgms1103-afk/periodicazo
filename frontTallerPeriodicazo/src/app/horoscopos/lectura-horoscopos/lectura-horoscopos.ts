import { Component, OnInit } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { Publicacion } from '../../models/publicacion.model';
import { ComentariosNoticias } from '../../noticias/comentarios-noticias/comentarios-noticias';

/**
 * Componente encargado de mostrar la vista de lectura detallada de un horóscopo.
 * @class LecturaHoroscopo
 * @implements {OnInit}
 */
@Component({
  selector: 'app-lectura-horoscopo',
  standalone: true,
  imports: [CommonModule, RouterModule, ComentariosNoticias],
  templateUrl: './lectura-horoscopos.html',
  styleUrls: ['./lectura-horoscopos.css']
})
export class LecturaHoroscopo implements OnInit {
  /**
   * Almacena los datos del horóscopo que se va a visualizar.
   * Se obtiene a través del estado de la navegación.
   * @type {Publicacion | null}
   */
  articulo: Publicacion | null = null;

  /**
   * Crea una instancia del componente LecturaHoroscopo.
   * Recupera el artículo de los extras de navegación si fueron enviados.
   * @param {Router} router - Servicio de enrutamiento de Angular para gestionar la navegación.
   */
  constructor(private router: Router) {
    const navegacion = this.router.getCurrentNavigation();
    if (navegacion?.extras?.state && navegacion.extras.state['articulo']) {
      this.articulo = navegacion.extras.state['articulo'];
    }
  }

  /**
   * Método del ciclo de vida de Angular que se ejecuta al inicializar el componente.
   * Verifica si se cargó un artículo; de lo contrario, redirige al usuario a la lista de horóscopos.
   * @returns {void}
   */
  ngOnInit(): void {
    if (!this.articulo) {
      this.router.navigate(['/horoscopos']);
    }
  }

  /**
   * Obtiene el símbolo emoji correspondiente a un signo zodiacal.
   * @param {string | undefined} signo - El nombre del signo zodiacal (ej. 'Aries').
   * @returns {string} El emoji del signo zodiacal, o un emoji por defecto ('✨') si no se encuentra.
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
