import { Component, Input, Output, EventEmitter, OnChanges, SimpleChanges } from '@angular/core';
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
  password: string = '';
  role: string = 'USUARIO';

  mensajeError: string = '';
  mensajeExito: string = '';

  constructor(private usuarioService: UsuarioService) {}


  ngOnChanges(changes: SimpleChanges): void {
    if (changes['usuarioAEditar'] && this.usuarioAEditar && this.modoEditar) {
      this.username = this.usuarioAEditar.username;
      this.role = this.usuarioAEditar.role;
      this.password = '';
    }
  }

  cerrar(): void {
    this.username = '';
    this.password = '';
    this.role = 'USUARIO';
    this.mensajeError = '';
    this.mensajeExito = '';
    this.alCerrar.emit();
  }

  guardarUsuario(): void {
    this.mensajeError = '';
    this.mensajeExito = '';

    if (!this.username || (!this.modoEditar && !this.password)) {
      this.mensajeError = 'El seudónimo ' + (!this.modoEditar ? 'y la contraseña de redacción son obligatorios.' : 'es obligatorio.');
      return;
    }

    const datosUsuario: Usuario = {
      username: this.username,
      role: this.role
    };

    if (this.password) {
      datosUsuario.password = this.password;
    }

    if (this.modoEditar && this.usuarioAEditar?.id) {
      // CORRECCIÓN: Si estamos editando, invoca al método de actualización
      this.usuarioService.actualizarUsuario(this.usuarioAEditar.id, datosUsuario).subscribe({
        next: () => {
          this.mensajeExito = '¡Usuario modificado con éxito en los archivos!';
          setTimeout(() => {
            this.cerrar();
          }, 1500);
        },
        error: () => {
          this.mensajeError = 'Fallo interno al procesar la actualización del usuario.';
        }
      });
    } else {
      // Modo Creación estándar
      this.usuarioService.crearUsuario(datosUsuario).subscribe({
        next: () => {
          this.mensajeExito = '¡Usuario incorporado con éxito a los archivos del diario!';
          setTimeout(() => {
            this.cerrar();
          }, 1500);
        },
        error: (err) => {
          if (err.status === 409) {
            this.mensajeError = 'Este seudónimo ya existe en nuestros archivos.';
          } else {
            this.mensajeError = 'Fallo interno al procesar el alta del usuario.';
          }
        }
      });
    }
  }
}
