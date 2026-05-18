import { Component } from '@angular/core';
import { RouterOutlet, Router, NavigationEnd } from '@angular/router';
import { CommonModule } from '@angular/common';
import { BarraNavegacion } from './barra-navegacion/barra-navegacion';

/**
 * Componente raíz de la aplicación.
 * Actúa como contenedor principal, albergando la barra de navegación global y el enrutador de las vistas (RouterOutlet).
 * @class App
 */
@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, CommonModule, BarraNavegacion],
  templateUrl: './app.html',
  styleUrls: ['./app.css']
})
export class App {
  /**
   * Bandera que determina si la barra de navegación principal debe ser visible en la vista actual.
   * @type {boolean}
   */
  mostrarBarra: boolean = true;

  /**
   * Crea una instancia del componente raíz.
   * Se suscribe a los eventos del enrutador para evaluar la URL actual y ocultar la barra de navegación
   * exclusivamente cuando el usuario se encuentra en la pantalla de inicio de sesión o en la ruta base.
   * @param {Router} router - Servicio de enrutamiento de Angular para interceptar y evaluar cambios de navegación.
   */
  constructor(private router: Router) {
    this.router.events.subscribe((event) => {
      if (event instanceof NavigationEnd) {
        this.mostrarBarra = event.url !== '/inicio-sesion' && event.url !== '/';
      }
    });
  }
}
