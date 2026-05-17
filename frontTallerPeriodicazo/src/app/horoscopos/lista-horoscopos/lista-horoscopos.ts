import { Component, OnInit } from '@angular/core';
import { RouterModule, Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { PublicacionService } from '../../services/publicacion.service';
import { Publicacion } from '../../models/publicacion.model';

@Component({
  selector: 'app-lista-horoscopos',
  standalone: true,
  imports: [RouterModule, CommonModule],
  templateUrl: './lista-horoscopos.html',
  styleUrls: ['./lista-horoscopos.css']
})
export class ListaHoroscopos implements OnInit {
  horoscopos: Publicacion[] = [];
  mensajeError: string = '';
  rolActual: string = '';

  constructor(private publicacionService: PublicacionService, private router: Router) {}

  ngOnInit(): void {
    this.rolActual = localStorage.getItem('rol_diario') || '';
    this.cargarHoroscopos();
  }

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

  editarHoroscopo(horoscopo: Publicacion) {
    this.router.navigate(['/editor-horoscopos'], { state: { horoscopoAEditar: horoscopo } });
  }

  eliminarHoroscopo(id: number | undefined) {
    if (!id) return;
    if (confirm('¿Está seguro de que desea retirar esta predicción astral de los registros?')) {
      this.publicacionService.eliminarPublicacion(id).subscribe({
        next: () => this.cargarHoroscopos(),
        error: () => this.mensajeError = 'Fallo al intentar eliminar el horóscopo.'
      });
    }
  }

  leerCompleto(horoscopo: Publicacion) {
    this.router.navigate(['/lectura-horoscopo'], { state: { articulo: horoscopo } });
  }

  irAComentarios(horoscopo: Publicacion) {
    this.router.navigate(['/comentarios'], { state: { articulo: horoscopo } });
  }

  // CORREGIDO: Ahora retorna la ruta de tus archivos SVG locales
  obtenerIcono(signo: string | undefined): string {
    const iconos: { [key: string]: string } = {
      'Aries': 'aries-solid-full.svg',
      'Tauro': 'taurus-solid-full.svg',
      'Geminis': 'gemini-solid-full.svg',
      'Cancer': 'cancer-solid-full.svg',
      'Leo': 'leo-solid-full.svg',
      'Virgo': 'virgo-solid-full.svg',
      'Libra': 'libra-solid-full.svg',
      'Escorpio': 'scorpio-solid-full.svg',
      'Sagitario': 'sagittarius-solid-full.svg',
      'Capricornio': 'capricorn-solid-full.svg',
      'Acuario': 'aquarius-solid-full.svg',
      'Piscis': 'pisces-solid-full.svg'
    };
    // Si no encuentra el signo, por defecto cargará aries para evitar errores
    return signo ? (iconos[signo] || 'aries-solid-full.svg') : 'aries-solid-full.svg';
  }
}
