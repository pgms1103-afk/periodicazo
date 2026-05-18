import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PublicacionService } from '../services/publicacion.service';
import { UsuarioService } from '../services/usuario.service';

// Declaración mágica para que Angular reconozca el CDN de Chart.js
declare var Chart: any;

@Component({
  selector: 'app-panel-principal',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './panel-principal.html',
  styleUrls: ['./panel-principal.css']
})
export class PanelPrincipal implements OnInit {

  totalAdmins = 0; totalEditores = 0; totalComentaristas = 0; totalLectores = 0;
  totalNoticias = 0; cntPolitica = 0; cntEconomia = 0; cntCultura = 0; cntDeportes = 0; cntTecnologia = 0;
  totalHoroscopos = 0;

  signosZodiacales = ['Aries', 'Tauro', 'Geminis', 'Cancer', 'Leo', 'Virgo', 'Libra', 'Escorpio', 'Sagitario', 'Capricornio', 'Acuario', 'Piscis'];
  horoscoposPorSigno: { [key: string]: number } = {};


  graficoUsuarios: any;
  graficoNoticias: any;

  constructor(
    private publicacionService: PublicacionService,
    private usuarioService: UsuarioService
  ) {
    this.signosZodiacales.forEach(s => this.horoscoposPorSigno[s] = 0);
  }

  ngOnInit(): void {
    this.cargarEstadisticas();
  }

  cargarEstadisticas() {
    this.usuarioService.obtenerUsuarios().subscribe(usuarios => {
      if (usuarios) {
        this.totalAdmins = usuarios.filter(u => u.role === 'ADMIN').length;
        this.totalEditores = usuarios.filter(u => u.role === 'EDITOR').length;
        this.totalComentaristas = usuarios.filter(u => u.role === 'COMENTADOR').length;
        this.totalLectores = usuarios.filter(u => u.role === 'USUARIO').length;
        this.dibujarGraficoUsuarios();
      }
    });

    this.publicacionService.obtenerPorTipo('NOTICIA').subscribe(noticias => {
      if (noticias) {
        this.totalNoticias = noticias.length;
        this.cntPolitica = noticias.filter(n => n.categoria?.includes('Politi')).length;
        this.cntEconomia = noticias.filter(n => n.categoria?.includes('Econo')).length;
        this.cntCultura = noticias.filter(n => n.categoria === 'Cultura').length;
        this.cntDeportes = noticias.filter(n => n.categoria === 'Deportes').length;
        this.cntTecnologia = noticias.filter(n => n.categoria?.includes('Tecnolog')).length;
        this.dibujarGraficoNoticias();
      }
    });

    this.publicacionService.obtenerPorTipo('HOROSCOPO').subscribe(horoscopos => {
      if (horoscopos) {
        this.totalHoroscopos = horoscopos.length;
        this.signosZodiacales.forEach(s => this.horoscoposPorSigno[s] = 0);
        horoscopos.forEach(h => {
          if (h.signo && this.horoscoposPorSigno[h.signo] !== undefined) {
            this.horoscoposPorSigno[h.signo]++;
          }
        });
      }
    });
  }

  private obtenerColorCSS(nombreVariable: string, colorRespaldo: string): string {
    const color = getComputedStyle(document.documentElement).getPropertyValue(nombreVariable).trim();
    return color ? color : colorRespaldo;
  }

  dibujarGraficoUsuarios() {
    if (this.graficoUsuarios) this.graficoUsuarios.destroy();


    const colorNegro = this.obtenerColorCSS('--negro-periodico', '#1a1a1a');
    const colorGris = this.obtenerColorCSS('--gris-oscuro', '#555555');
    const colorHueso = this.obtenerColorCSS('--blanco-hueso', '#f4f1ea');

    setTimeout(() => {
      const ctx = document.getElementById('canvasUsuarios');
      if (!ctx) return;

      this.graficoUsuarios = new Chart(ctx, {
        type: 'doughnut',
        data: {
          labels: ['Directores', 'Editores', 'Comentadores', 'Suscriptores'],
          datasets: [{
            data: [this.totalAdmins, this.totalEditores, this.totalComentaristas, this.totalLectores],
            backgroundColor: [colorNegro, colorGris, '#8a8a8a', '#c4c4c4'],
            borderColor: colorHueso,
            borderWidth: 3
          }]
        },
        options: {
          responsive: true, maintainAspectRatio: false,
          plugins: { legend: { position: 'bottom', labels: { font: { family: 'Georgia', size: 12 }, color: colorNegro } } }
        }
      });
    }, 100);
  }

  dibujarGraficoNoticias() {
    if (this.graficoNoticias) this.graficoNoticias.destroy();

 
    const colorNegro = this.obtenerColorCSS('--negro-periodico', '#1a1a1a');

    setTimeout(() => {
      const ctx = document.getElementById('canvasNoticias');
      if (!ctx) return;

      this.graficoNoticias = new Chart(ctx, {
        type: 'bar',
        data: {
          labels: ['Política', 'Economía', 'Cultura', 'Deportes', 'Tecnología'],
          datasets: [{
            label: 'Columnas Impresas',
            data: [this.cntPolitica, this.cntEconomia, this.cntCultura, this.cntDeportes, this.cntTecnologia],
            // Usamos el color dinámico traído del CSS
            backgroundColor: colorNegro,
            borderColor: colorNegro,
            borderWidth: 1,
            borderRadius: 2
          }]
        },
        options: {
          responsive: true, maintainAspectRatio: false,
          scales: {
            y: { beginAtZero: true, ticks: { stepSize: 1, font: { family: 'Georgia' }, color: colorNegro }, grid: { color: 'rgba(0,0,0,0.1)' } },
            x: { ticks: { font: { family: 'Georgia', weight: 'bold' }, color: colorNegro }, grid: { display: false } }
          },
          plugins: { legend: { display: false } }
        }
      });
    }, 100);
  }
}
