import {inject} from '@angular/core';
import {CanActivateFn, Router} from '@angular/router';

/**
 * Guard de ruta de Angular que protege el acceso a las vistas basándose en el rol del usuario.
 * Verifica el 'rol_diario' en el almacenamiento local y lo compara con los roles permitidos.
 * Si el usuario no está autenticado, lo redirige al inicio de sesión. Si no tiene permisos, lo envía a la portada.
 * * @type {CanActivateFn}
 * @param route - La instantánea de la ruta a la que se intenta acceder, contiene los datos y roles permitidos.
 * @param state - El estado actual del enrutador durante el intento de navegación.
 * @returns {boolean} Retorna `true` si se permite el acceso, o `false` si se deniega.
 */
export const roleGuard: CanActivateFn = (route, state) => {
  const router = inject(Router);
  const rolActual = localStorage.getItem('rol_diario');

  if (!rolActual) {
    router.navigate(['/inicio-sesion']);
    return false;
  }

  const rolesPermitidos = route.data?.['roles'] as Array<string>;

  if (rolesPermitidos && rolesPermitidos.length > 0) {
    if (rolesPermitidos.includes(rolActual)) {
      return true;
    } else {

      router.navigate(['/noticias']);
      return false;
    }
  }
  return true;
};
