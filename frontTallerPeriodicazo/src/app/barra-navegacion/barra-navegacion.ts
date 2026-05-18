import { Component, OnInit } from '@angular/core';
import { RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';

/**
 * Representa la barra de navegación principal de la aplicación.
 * @class BarraNavegacion
 * @implements {OnInit}
 */
@Component({
  selector: 'app-barra-navegacion',
  standalone: true,
  imports: [RouterModule, CommonModule],
  templateUrl: './barra-navegacion.html',
  styleUrls: ['./barra-navegacion.css']
})
export class BarraNavegacion implements OnInit {
  /**
   * Almacena el rol del usuario actual obtenido de la sesión.
   * @type {string}
   */
  rolActual: string = '';

  /**
   * Inicializa el componente y recupera el rol del usuario desde el almacenamiento local.
   * @returns {void}
   */
  ngOnInit(): void {
    this.rolActual = localStorage.getItem('rol_diario') || '';
  }
}
