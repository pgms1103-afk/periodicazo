import {inject} from '@angular/core';
import {CanActivateFn, Router} from '@angular/router';

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
