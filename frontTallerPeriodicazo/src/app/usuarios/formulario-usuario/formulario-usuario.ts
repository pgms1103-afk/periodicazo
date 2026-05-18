import { Component, EventEmitter, Input, OnChanges, Output, SimpleChanges } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { UsuarioService } from '../../services/usuario.service';
import { Usuario } from '../../models/usuario.model';

@Component({
  selector: 'app-formulario-usuario',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './formulario-usuario.html',
  styleUrls: ['./formulario-usuario.css']
})
export class FormularioUsuario implements OnChanges {
  @Input() mostrar: boolean = false;
  @Input() modoEditar: boolean = false;
  @Input() usuarioAEditar: Usuario | null = null;
  @Output() alCerrar = new EventEmitter<void>();

  username: string = '';
  role: string = 'USUARIO';
  password?: string = '';

  mensajeError: string = '';
  mensajeExito: string = '';

  constructor(private usuarioService: UsuarioService) {}

  private extraerError(err: any): string {
    if (err.error) {
      if (typeof err.error === 'string') return err.error;
      if (err.error.message) return err.error.message;
    }
    return 'Ocurrió un error en el servidor.';
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['mostrar'] && this.mostrar) {
      this.mensajeError = '';
      this.mensajeExito = '';
      if (this.modoEditar && this.usuarioAEditar) {
        this.username = this.usuarioAEditar.username || '';
        this.role = this.usuarioAEditar.role || 'USUARIO';
        this.password = '';
      } else {
        this.username = '';
        this.role = 'USUARIO';
        this.password = '';
      }
    }
  }

  cerrar() {
    this.alCerrar.emit();
  }

  guardarUsuario() {
    this.mensajeError = '';
    this.mensajeExito = '';

    if (!this.username) {
      this.mensajeError = 'El nombre de usuario es obligatorio.';
      return;
    }

    if (this.modoEditar && this.usuarioAEditar?.id) {
      const usuarioActualizado: Usuario = {
        id: this.usuarioAEditar.id,
        username: this.username,
        role: this.role,
        password: this.password ? this.password : undefined
      };

      this.usuarioService.actualizarUsuario(usuarioActualizado.id!, usuarioActualizado).subscribe({
        next: () => {
          this.mensajeExito = 'El usuario se actualizó correctamente.';
          setTimeout(() => this.cerrar(), 1500);
        },
        error: (err) => {
          this.mensajeError = this.extraerError(err);
        }
      });
    } else {
      if (!this.password) {
        this.mensajeError = 'La contraseña es obligatoria para registrar un usuario nuevo.';
        return;
      }

      const nuevoUser: Usuario = {
        username: this.username,
        role: this.role,
        password: this.password
      };

      this.usuarioService.crearUsuario(nuevoUser).subscribe({
        next: () => {
          this.mensajeExito = 'Usuario registrado exitosamente.';
          setTimeout(() => this.cerrar(), 1500);
        },
        error: (err) => {
          this.mensajeError = this.extraerError(err);
        }
      });
    }
  }
}
