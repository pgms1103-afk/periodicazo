import { Component, OnInit } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { Publicacion } from '../../models/publicacion.model';
import { ComentariosNoticias } from '../../noticias/comentarios-noticias/comentarios-noticias';

@Component({
  selector: 'app-lectura-horoscopo',
  standalone: true,
  imports: [CommonModule, RouterModule, ComentariosNoticias],
  templateUrl: './lectura-horoscopos.html',
  styleUrls: ['./lectura-horoscopos.css']
})
export class LecturaHoroscopo implements OnInit {
  articulo: Publicacion | null = null;

  constructor(private router: Router) {
    const navegacion = this.router.getCurrentNavigation();
    if (navegacion?.extras?.state && navegacion.extras.state['articulo']) {
      this.articulo = navegacion.extras.state['articulo'];
    }
  }

  ngOnInit(): void {
    if (!this.articulo) {
      this.router.navigate(['/horoscopos']);
    }
  }


  obtenerIcono(signo: string | undefined): string {
    const iconos: { [key: string]: string } = {
      'Aries': '/aries-solid-full.svg',
      'Tauro': '/taurus-solid-full.svg',
      'Geminis': '/gemini-solid-full.svg',
      'Cancer': '/cancer-solid-full.svg',
      'Leo': '/leo-solid-full.svg',
      'Virgo': '/virgo-solid-full.svg',
      'Libra': '/libra-solid-full.svg',
      'Escorpio': '/scorpio-solid-full.svg',
      'Sagitario': '/sagittarius-solid-full.svg',
      'Capricornio': '/capricorn-solid-full.svg',
      'Acuario': '/aquarius-solid-full.svg',
      'Piscis': '/pisces-solid-full.svg'
    };
    return signo ? (iconos[signo] || '/aries-solid-full.svg') : '/aries-solid-full.svg';
  }
}
