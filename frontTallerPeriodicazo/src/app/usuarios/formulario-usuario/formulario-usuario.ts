import { Component, EventEmitter, Input, OnChanges, Output, SimpleChanges } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { UsuarioService } from '../../services/usuario.service';
import { Usuario } from '../../models/usuario.model';

/**
 * Componente que representa un formulario (modal o integrado) para la creación y edición de usuarios.
 * Gestiona la captura de datos y se comunica con el servicio para actualizar o registrar usuarios.
 * @class FormularioUsuario
 * @implements {OnChanges}
 */
@Component({
  selector: 'app-formulario-usuario',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './formulario-usuario.html',
  styleUrls: ['./formulario-usuario.css']
})
export class FormularioUsuario implements OnChanges {
  /**
   * Controla la visibilidad del componente (útil si se implementa como ventana modal).
   * @type {boolean}
   */
  @Input() mostrar: boolean = false;

  /**
   * Indica si el formulario se encuentra en modo de edición de un usuario existente (true)
   * o en modo de creación de un usuario nuevo (false).
   * @type {boolean}
   */
  @Input() modoEditar: boolean = false;

  /**
   * Contiene la información del usuario que se va a editar.
   * Se utiliza únicamente cuando 'modoEditar' es verdadero.
   * @type {Usuario | null}
   */
  @Input() usuarioAEditar: Usuario | null = null;

  /**
   * Evento que se emite hacia el componente padre para indicar que el formulario debe cerrarse.
   * @type {EventEmitter<void>}
   */
  @Output() alCerrar = new EventEmitter<void>();

  /**
   * Nombre de usuario capturado en el formulario.
   * @type {string}
   */
  username: string = '';

  /**
   * Rol asignado al usuario en el formulario (por defecto 'USUARIO').
   * @type {string}
   */
  role: string = 'USUARIO';

  /**
   * Contraseña capturada en el formulario. Es opcional al editar.
   * @type {string | undefined}
   */
  password?: string = '';

  /**
   * Almacena y muestra mensajes de error en la interfaz durante el guardado.
   * @type {string}
   */
  mensajeError: string = '';

  /**
   * Almacena y muestra mensajes de confirmación de éxito en la interfaz.
   * @type {string}
   */
  mensajeExito: string = '';

  /**
   * Crea una instancia del componente FormularioUsuario.
   * @param {UsuarioService} usuarioService - Servicio de conexión para operaciones CRUD de usuarios.
   */
  constructor(private usuarioService: UsuarioService) {}

  /**
   * Extrae un mensaje de error legible a partir de un objeto de error de una respuesta HTTP.
   * @param {unknown} err - El objeto de error devuelto por la petición al servidor.
   * @returns {string} El mensaje de error decodificado o un texto genérico.
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
   * Método del ciclo de vida que reacciona a los cambios en las propiedades de entrada (@Input).
   * Se encarga de inicializar y limpiar el formulario cada vez que se muestra en pantalla.
   * @param {SimpleChanges} changes - Objeto que contiene las propiedades que han cambiado.
   * @returns {void}
   */
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

  /**
   * Emite el evento 'alCerrar' para notificar al componente padre que la vista del formulario debe ocultarse.
   * @returns {void}
   */
  cerrar() {
    this.alCerrar.emit();
  }

  /**
   * Valida los datos del formulario y decide si debe enviar una petición para crear un usuario nuevo
   * o actualizar uno existente. Cierra el formulario en caso de éxito.
   * @returns {void}
   */
  guardarUsuario() {
    this.mensajeError = '';
    this.mensajeExito = '';

    if (!this.username) {
      this.mensajeError = 'El nombre de usuario es obligatorio.';
      return;
    }

    if (this.modoEditar && this.usuarioAEditar && this.usuarioAEditar.id !== undefined) {

      const idParaActualizar = this.usuarioAEditar.id;

      const usuarioActualizado: Usuario = {
        id: idParaActualizar,
        username: this.username,
        role: this.role,
        password: this.password ? this.password : undefined
      };

      this.usuarioService.actualizarUsuario(idParaActualizar, usuarioActualizado).subscribe({
        next: () => {
          this.mensajeExito = 'Usuario actualizado correctamente.';
          setTimeout(() => { this.cerrar(); }, 1500);
        },
        error: (err) => {
          this.mensajeError = FormularioUsuario.extraerError(err);
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
          setTimeout(() => { this.cerrar(); }, 1500);
        },
        error: (err) => {
          this.mensajeError = FormularioUsuario.extraerError(err);
        }
      });
    }
  }
}
