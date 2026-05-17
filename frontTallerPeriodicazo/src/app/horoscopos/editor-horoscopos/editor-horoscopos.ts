import { Component, OnInit } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { PublicacionService } from '../../services/publicacion.service';
import { Publicacion } from '../../models/publicacion.model';

@Component({
  selector: 'app-editor-horoscopos',
  standalone: true,
  imports: [RouterModule, CommonModule, FormsModule],
  templateUrl: './editor-horoscopos.html',
  styleUrls: ['./editor-horoscopos.css']
})
export class EditorHoroscopos implements OnInit {
  titulo: string = '';
  signo: string = 'Aries';
  elemento: string = 'Fuego';
  autor: string = '';
  editorial: string = '';
  contenido: string = '';

  horoscopoExistente: Publicacion | null = null;
  modoEditar: boolean = false;
  mensajeError: string = '';

  constructor(private publicacionService: PublicacionService, private router: Router) {
    const navegacion = this.router.getCurrentNavigation();
    if (navegacion?.extras?.state && navegacion.extras.state['horoscopoAEditar']) {
      this.horoscopoExistente = navegacion.extras.state['horoscopoAEditar'];
      this.modoEditar = true;
    }
  }

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
        error: () => this.mensajeError = 'Fallo al guardar la predicción en la imprenta.'
      });
    }
  }
}
