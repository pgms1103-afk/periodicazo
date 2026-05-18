import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-inicio-sesion',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './inicio-sesion.html',
  styleUrls: ['./inicio-sesion.css']
})
export class InicioSesion {
  // Variables conectadas a tu HTML
  vistaLogin: boolean = true;

  usuario: string = '';
  contrasena: string = '';

  nuevoUsuario: string = '';
  nuevaContrasena: string = '';

  mensajeError: string = '';
  mensajeExito: string = '';

  constructor(private authService: AuthService, private router: Router) {}

  private extraerError(err: any): string {
    if (err.error) {
      if (typeof err.error === 'string') return err.error;
      if (err.error.message) return err.error.message;
    }
    return 'Ocurrió un error en el servidor.';
  }

  cambiarPestana(esLogin: boolean) {
    this.vistaLogin = esLogin;
    this.mensajeError = '';
    this.mensajeExito = '';
  }

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
        this.mensajeError = this.extraerError(err);
      }
    });
  }

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
        setTimeout(() => this.cambiarPestana(true), 2000);
      },
      error: (err) => {
        this.mensajeError = this.extraerError(err);
      }
    });
  }
}
