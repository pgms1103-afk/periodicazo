import { ApplicationConfig, provideZoneChangeDetection } from '@angular/core';
import { provideRouter } from '@angular/router';
import { provideHttpClient, withInterceptors } from '@angular/common/http';

import { routes } from './app.routes';
import { authInterceptor } from './interceptors/auth.interceptor';

/**
 * Configuración global de la aplicación Angular (arquitectura Standalone).
 * Define los proveedores principales de servicios y configuraciones a nivel raíz de la aplicación.
 * Configura la detección de cambios, las rutas de navegación y el cliente HTTP con sus respectivos interceptores.
 * @type {ApplicationConfig}
 */
export const appConfig: ApplicationConfig = {
  providers: [
    /**
     * Configura la detección de cambios de zona (Zone.js).
     * `eventCoalescing: true` optimiza el rendimiento agrupando múltiples eventos
     * para disparar un único ciclo de detección de cambios.
     */
    provideZoneChangeDetection({ eventCoalescing: true }),

    /**
     * Provee el sistema de enrutamiento principal inyectando las rutas definidas en `app.routes.ts`.
     */
    provideRouter(routes),

    /**
     * Provee el cliente HTTP global de la aplicación y configura el interceptor de autenticación
     * (`authInterceptor`) para adjuntar automáticamente el token en cada petición saliente.
     */
    provideHttpClient(withInterceptors([authInterceptor]))
  ]
};
