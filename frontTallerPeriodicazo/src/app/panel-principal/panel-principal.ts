import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PublicacionService } from '../services/publicacion.service';
import { UsuarioService } from '../services/usuario.service';

/**
 * Declaración de Chart.js para evitar errores de tipado en TypeScript.
 * @type {new (ctx: unknown, config: unknown) => unknown}
 */
declare const Chart: new (ctx: unknown, config: unknown) => unknown;

/**
 * Componente del panel principal (Dashboard) que muestra estadísticas generales del sistema,
 * incluyendo usuarios registrados, noticias publicadas por categoría y distribución de horóscopos.
 * @class PanelPrincipal
 * @implements {OnInit}
 */
@Component({
  selector: 'app-panel-principal',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './panel-principal.html',
  styleUrls: ['./panel-principal.css']
})
export class PanelPrincipal implements OnInit {

  /** Total de usuarios con rol ADMIN. @type {number} */
  totalAdmins = 0;
  /** Total de usuarios con rol EDITOR. @type {number} */
  totalEditores = 0;
  /** Total de usuarios con rol COMENTADOR. @type {number} */
  totalComentaristas = 0;
  /** Total de usuarios con rol USUARIO. @type {number} */
  totalLectores = 0;

  /** Total general de noticias publicadas. @type {number} */
  totalNoticias = 0;
  /** Cantidad de noticias en la categoría Política. @type {number} */
  cntPolitica = 0;
  /** Cantidad de noticias en la categoría Economía. @type {number} */
  cntEconomia = 0;
  /** Cantidad de noticias en la categoría Cultura. @type {number} */
  cntCultura = 0;
  /** Cantidad de noticias en la categoría Deportes. @type {number} */
  cntDeportes = 0;
  /** Cantidad de noticias en la categoría Tecnología. @type {number} */
  cntTecnologia = 0;

  /** Total general de horóscopos publicados. @type {number} */
  totalHoroscopos = 0;

  /**
   * Arreglo con los nombres de los 12 signos zodiacales.
   * @type {string[]}
   */
  signosZodiacales = ['Aries', 'Tauro', 'Geminis', 'Cancer', 'Leo', 'Virgo', 'Libra', 'Escorpio', 'Sagitario', 'Capricornio', 'Acuario', 'Piscis'];

  /**
   * Diccionario para almacenar el conteo de horóscopos por cada signo zodiacal.
   * @type {{ [key: string]: number }}
   */
  horoscoposPorSigno: { [key: string]: number } = {};

  /**
   * Referencia a la instancia del gráfico de dona de usuarios de Chart.js.
   * @type {unknown}
   */
  graficoUsuarios: unknown = null;

  /**
   * Referencia a la instancia del gráfico de barras de noticias de Chart.js.
   * @type {unknown}
   */
  graficoNoticias: unknown = null;

  /**
   * Inicializa el componente y prepara el diccionario de signos zodiacales.
   * @param {PublicacionService} publicacionService - Servicio para obtener datos de publicaciones.
   * @param {UsuarioService} usuarioService - Servicio para obtener datos de usuarios.
   */
  constructor(
    private publicacionService: PublicacionService,
    private usuarioService: UsuarioService
  ) {
    this.signosZodiacales.forEach(s => { this.horoscoposPorSigno[s] = 0; });
  }

  /**
   * Método del ciclo de vida de Angular que se ejecuta al iniciar el componente.
   * Llama a la función para cargar todas las estadísticas desde el backend.
   * @returns {void}
   */
  ngOnInit(): void {
    this.cargarEstadisticas();
  }

  /**
   * Consulta los servicios backend para obtener la cantidad de usuarios por rol,
   * noticias por categoría y horóscopos por signo. Luego dispara el renderizado de gráficos.
   * @returns {void}
   */
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

  /**
   * Lee el valor de una variable CSS definida en el archivo de estilos global.
   * @param {string} nombreVariable - El nombre de la variable CSS (ej. '--mi-color').
   * @param {string} colorRespaldo - Un color hexadecimal a usar si la variable no se encuentra.
   * @returns {string} El valor del color encontrado o el color de respaldo.
   */
  private static obtenerColorCSS(nombreVariable: string, colorRespaldo: string): string {
    const color = getComputedStyle(document.documentElement).getPropertyValue(nombreVariable).trim();
    return color ? color : colorRespaldo;
  }

  /**
   * Renderiza un gráfico tipo dona (doughnut) de Chart.js mostrando la distribución de roles de usuario.
   * Destruye el gráfico anterior si ya existía para evitar superposiciones.
   * @returns {void}
   */
  dibujarGraficoUsuarios() {
    if (this.graficoUsuarios) (this.graficoUsuarios as { destroy: () => void }).destroy();

    const colorNegro = PanelPrincipal.obtenerColorCSS('--negro-periodico', '#1a1a1a');
    const colorGris = PanelPrincipal.obtenerColorCSS('--gris-oscuro', '#555555');
    const colorHueso = PanelPrincipal.obtenerColorCSS('--blanco-hueso', '#f4f1ea');

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

  /**
   * Renderiza un gráfico de barras (bar) de Chart.js mostrando la cantidad de noticias por categoría.
   * Destruye el gráfico anterior si ya existía para evitar superposiciones.
   * @returns {void}
   */
  dibujarGraficoNoticias() {
    if (this.graficoNoticias) (this.graficoNoticias as { destroy: () => void }).destroy();

    const colorNegro = PanelPrincipal.obtenerColorCSS('--negro-periodico', '#1a1a1a');

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
