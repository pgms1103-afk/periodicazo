import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PublicacionService } from '../services/publicacion.service';
import { UsuarioService } from '../services/usuario.service';

@Component({
  selector: 'app-panel-principal',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './panel-principal.html',
  styleUrls: ['./panel-principal.css']
})
export class PanelPrincipal implements OnInit {
  // Estadísticas de Usuarios
  totalAdmins: number = 0;
  totalEditores: number = 0;
  totalComentaristas: number = 0;
  totalLectores: number = 0;

  // Estadísticas de Noticias
  totalNoticias: number = 0;
  cntPolitica: number = 0;
  cntEconomia: number = 0;
  cntCultura: number = 0;
  cntDeportes: number = 0;
  cntTecnologia: number = 0;

  // Estadísticas de Horóscopos
  totalHoroscopos: number = 0;
  horoscoposPorSigno: { [key: string]: number } = {
    'Aries': 0, 'Tauro': 0, 'Geminis': 0, 'Cancer': 0,
    'Leo': 0, 'Virgo': 0, 'Libra': 0, 'Escorpio': 0,
    'Sagitario': 0, 'Capricornio': 0, 'Acuario': 0, 'Piscis': 0
  };

  // Array para dibujar la cuadrícula del zodiaco en orden
  signosZodiacales = ['Aries', 'Tauro', 'Geminis', 'Cancer', 'Leo', 'Virgo', 'Libra', 'Escorpio', 'Sagitario', 'Capricornio', 'Acuario', 'Piscis'];

  constructor(
    private publicacionService: PublicacionService,
    private usuarioService: UsuarioService
  ) {}

  ngOnInit(): void {
    this.cargarEstadisticas();
  }

  cargarEstadisticas(): void {
    // 1. Contar Usuarios por Rol
    this.usuarioService.obtenerUsuarios().subscribe({
      next: (users) => {
        if (users) {
          this.totalAdmins = users.filter(u => u.role === 'ADMIN').length;
          this.totalEditores = users.filter(u => u.role === 'EDITOR').length;
          this.totalComentaristas = users.filter(u => u.role === 'COMENTADOR').length;
          this.totalLectores = users.filter(u => u.role === 'USUARIO').length;
        }
      }
    });

    // 2. Contar Noticias por Categoría
    this.publicacionService.obtenerPorTipo('NOTICIA').subscribe({
      next: (noticias) => {
        if (noticias) {
          this.totalNoticias = noticias.length;
          this.cntPolitica = noticias.filter(n => n.categoria === 'Politica').length;
          this.cntEconomia = noticias.filter(n => n.categoria === 'Economia').length;
          this.cntCultura = noticias.filter(n => n.categoria === 'Cultura').length;
          this.cntDeportes = noticias.filter(n => n.categoria === 'Deportes').length;
          this.cntTecnologia = noticias.filter(n => n.categoria === 'Tecnologia').length;
        }
      }
    });

    // 3. Contar Horóscopos por Signo
    this.publicacionService.obtenerPorTipo('HOROSCOPO').subscribe({
      next: (horos) => {
        if (horos) {
          this.totalHoroscopos = horos.length;
          horos.forEach(h => {
            if (h.signo && this.horoscoposPorSigno[h.signo] !== undefined) {
              this.horoscoposPorSigno[h.signo]++;
            }
          });
        }
      }
    });
  }
}
