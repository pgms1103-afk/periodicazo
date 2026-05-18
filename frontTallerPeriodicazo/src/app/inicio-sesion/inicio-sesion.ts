import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

/**
 * Componente que gestiona el inicio de sesión y el registro de nuevos usuarios en el sistema.
 * @class InicioSesion
 */
@Component({
  selector: 'app-inicio-sesion',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './inicio-sesion.html',
  styleUrls: ['./inicio-sesion.css']
})
export class InicioSesion {
  /**
   * Indica si se muestra el formulario de inicio de sesión (true) o el de registro (false).
   * @type {boolean}
   */
  vistaLogin = true;

  /**
   * Nombre de usuario para iniciar sesión.
   * @type {string}
   */
  usuario = '';

  /**
   * Contraseña para iniciar sesión.
   * @type {string}
   */
  contrasena = '';

  /**
   * Nombre del nuevo usuario a registrar.
   * @type {string}
   */
  nuevoUsuario = '';

  /**
   * Contraseña del nuevo usuario a registrar.
   * @type {string}
   */
  nuevaContrasena = '';

  /**
   * Almacena y muestra los mensajes de error de validación o del servidor.
   * @type {string}
   */
  mensajeError = '';

  /**
   * Almacena y muestra los mensajes de éxito, como un registro correcto.
   * @type {string}
   */
  mensajeExito = '';

  /**
   * Crea una instancia del componente InicioSesion.
   * @param {AuthService} authService - Servicio encargado de la autenticación y registro.
   * @param {Router} router - Servicio de enrutamiento para navegar entre vistas.
   */
  constructor(private authService: AuthService, private router: Router) {}

  /**
   * Extrae un mensaje de error legible a partir de un objeto de error devuelto por el backend.
   * @param {unknown} err - El objeto de error capturado de la petición.
   * @returns {string} El mensaje de error extraído o un mensaje por defecto.
   */
  private static extraerError(err: unknown): string {
    const errorObj = err as { error?: string | { message?: string } };
    if (errorObj?.error) {
      if (typeof errorObj.error === 'string') return errorObj.error;
      if (errorObj.error.message) return errorObj.error.message;
    }
    return 'Ocurrió un error en el servidor.';
  }

  /**
   * Alterna la vista de la interfaz entre el modo de inicio de sesión y el modo de registro.
   * @param {boolean} esLogin - Si es `true`, muestra el inicio de sesión; si es `false`, muestra el registro.
   * @returns {void}
   */
  cambiarPestana(esLogin: boolean) {
    this.vistaLogin = esLogin;
    this.mensajeError = '';
    this.mensajeExito = '';
  }

  /**
   * Procesa el formulario de inicio de sesión, valida las credenciales y redirige al panel principal.
   * @returns {void}
   */
  entrarAlSistema() {
    this.mensajeError = '';
    this.mensajeExito = '';

    if (!this.usuario || !this.contrasena) {
      this.mensajeError = 'Debe ingresar usuario y contraseña.';
      return;
    }

    this.authService.iniciarSesion(this.usuario, this.contrasena).subscribe({
      next: () => {
        this.router.navigate(['/noticias']);
      },
      error: (err) => {
        this.mensajeError = InicioSesion.extraerError(err);
      }
    });
  }

  /**
   * Procesa el formulario de registro, crea el usuario y vuelve al login tras una notificación exitosa.
   * @returns {void}
   */
  registrarSistema() {
    this.mensajeError = '';
    this.mensajeExito = '';

    if (!this.nuevoUsuario || !this.nuevaContrasena) {
      this.mensajeError = 'Debe completar todos los campos.';
      return;
    }

    this.authService.registrarUsuario(this.nuevoUsuario, this.nuevaContrasena).subscribe({
      next: () => {
        this.mensajeExito = 'Suscripción exitosa. Ya puede ingresar.';
        this.nuevoUsuario = '';
        this.nuevaContrasena = '';
        setTimeout(() => { this.cambiarPestana(true); }, 2000);
      },
      error: (err) => {
        this.mensajeError = InicioSesion.extraerError(err);
      }
    });
  }
}
