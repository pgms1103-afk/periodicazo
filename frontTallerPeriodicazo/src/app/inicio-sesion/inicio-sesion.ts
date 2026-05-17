import { Component } from '@angular/core';
import { RouterModule, Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-inicio-sesion',
  standalone: true,
  imports: [RouterModule, CommonModule, FormsModule],
  templateUrl: './inicio-sesion.html',
  styleUrls: ['./inicio-sesion.css']
})
export class InicioSesion {
  vistaLogin: boolean = true;

  usuario: string = '';
  contrasena: string = '';
  mensajeError: string = '';
  mensajeExito: string = '';

  nuevoUsuario: string = '';
  nuevaContrasena: string = '';

  constructor(private authService: AuthService, private router: Router) {}

  cambiarPestana(esLogin: boolean) {
    this.vistaLogin = esLogin;
    this.mensajeError = '';
    this.mensajeExito = '';
    this.usuario = '';
    this.contrasena = '';
    this.nuevoUsuario = '';
    this.nuevaContrasena = '';
  }

  entrarAlSistema() {
    this.mensajeError = '';
    this.mensajeExito = '';

    if (!this.usuario || !this.contrasena) {
      this.mensajeError = 'Complete los datos para ingresar.';
      return;
    }

    this.authService.iniciarSesion(this.usuario, this.contrasena).subscribe({
      next: (respuesta) => {
        const rol = localStorage.getItem('rol_diario');
        if (rol === 'ADMIN') {
          this.router.navigate(['/panel-principal']);
        } else {
          this.router.navigate(['/noticias']);
        }
      },
      error: (err) => {
        this.mensajeError = 'Credenciales invalidas. El lector no existe o la contraseña no coincide.';
      }
    });
  }

  registrarSistema() {
    this.mensajeError = '';
    this.mensajeExito = '';

    if (!this.nuevoUsuario || !this.nuevaContrasena) {
      this.mensajeError = 'Se requiere Seudonimo y Contrasena obligatorios.';
      return;
    }

    this.authService.registrarUsuario(this.nuevoUsuario, this.nuevaContrasena).subscribe({
      next: (respuesta) => {
        this.mensajeExito = '¡Usuario creado con exito!';
        setTimeout(() => {
          this.cambiarPestana(true);
        }, 2000);
      },
      error: (err) => {
        if (err.status === 409) {
          this.mensajeError = 'Este nombre de usuario ya esta registrado.';
        } else {
          this.mensajeError = 'Ocurrio un fallo al intentar registrar.';
        }
      }
    });
  }
}
