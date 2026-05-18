import { Component, OnInit } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { Publicacion } from '../../models/publicacion.model';
import { ComentariosNoticias } from '../comentarios-noticias/comentarios-noticias';

/**
 * Componente que maneja la vista de lectura detallada de una noticia.
 * Renderiza el contenido completo del artículo y su respectiva sección de comentarios.
 * @class LecturaNoticias
 * @implements {OnInit}
 */
@Component({
  selector: 'app-lectura-noticias',
  standalone: true,
  imports: [CommonModule, RouterModule, ComentariosNoticias],
  templateUrl: './lectura-noticias.html',
  styleUrls: ['./lectura-noticias.css']
})
export class LecturaNoticias implements OnInit {
  /**
   * Almacena los datos del artículo (noticia) que se va a visualizar.
   * Se obtiene a través del estado de la navegación al seleccionar una noticia en la lista.
   * @type {Publicacion | null}
   */
  articulo: Publicacion | null = null;

  /**
   * Crea una instancia del componente LecturaNoticias.
   * Recupera el artículo de los extras de navegación si fueron enviados por el componente anterior.
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
   * Verifica si se cargó un artículo; de lo contrario, redirige al usuario al listado principal de noticias.
   * @returns {void}
   */
  ngOnInit(): void {
    if (!this.articulo) {
      this.router.navigate(['/noticias']);
    }
  }
}
