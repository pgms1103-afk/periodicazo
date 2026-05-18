import { Component, OnInit } from '@angular/core';
import { RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormularioUsuario } from '../formulario-usuario/formulario-usuario';
import { UsuarioService } from '../../services/usuario.service';
import { Usuario } from '../../models/usuario.model';

/**
 * Componente que muestra y gestiona el listado completo de usuarios en el sistema.
 * Proporciona opciones para crear, editar y eliminar (suspender) cuentas de usuario mediante un modal.
 * @class ListaUsuarios
 * @implements {OnInit}
 */
@Component({
  selector: 'app-lista-usuarios',
  standalone: true,
  imports: [RouterModule, CommonModule, FormularioUsuario],
  templateUrl: './lista-usuarios.html',
  styleUrls: ['./lista-usuarios.css']
})
export class ListaUsuarios implements OnInit {
  /**
   * Arreglo que almacena los usuarios obtenidos desde el servidor.
   * @type {Usuario[]}
   */
  usuarios: Usuario[] = [];

  /**
   * Mensaje de error para notificar al administrador en caso de fallos de conexión.
   * @type {string}
   */
  mensajeError: string = '';

  /**
   * Controla la visibilidad de la ventana modal del formulario de usuarios.
   * @type {boolean}
   */
  modalVisible: boolean = false;

  /**
   * Indica si la ventana modal se abre en modo de edición (true) o creación (false).
   * @type {boolean}
   */
  modoEdicionActivo: boolean = false;

  /**
   * Almacena los datos del usuario seleccionado temporalmente para ser editado en el modal.
   * @type {Usuario | null}
   */
  usuarioSeleccionado: Usuario | null = null;

  /**
   * Crea una instancia del componente ListaUsuarios.
   * @param {UsuarioService} usuarioService - Servicio para consumir la API de administración de usuarios.
   */
  constructor(private usuarioService: UsuarioService) {}

  /**
   * Método del ciclo de vida de Angular que se ejecuta al iniciar el componente.
   * Invoca la carga inicial de los usuarios.
   * @returns {void}
   */
  ngOnInit(): void {
    this.cargarUsuarios();
  }

  /**
   * Consulta el servicio para obtener la lista de usuarios y actualiza el arreglo local.
   * Muestra un mensaje de error si falla la comunicación con el servidor.
   * @returns {void}
   */
  cargarUsuarios(): void {
    this.mensajeError = '';
    this.usuarioService.obtenerUsuarios().subscribe({
      next: (datos) => {
        this.usuarios = datos || [];
      },
      error: (err) => {
        this.mensajeError = 'Fallo al conectar con la imprenta. No se pudieron recuperar los registros.';
      }
    });
  }

  /**
   * Prepara el estado del componente para abrir el modal en modo de creación de un usuario nuevo.
   * @returns {void}
   */
  abrirParaCrear() {
    this.usuarioSeleccionado = null;
    this.modoEdicionActivo = false;
    this.modalVisible = true;
  }

  /**
   * Prepara el estado del componente para abrir el modal en modo de edición con los datos de un usuario existente.
   * @param {Usuario} usuario - Objeto con los datos del usuario a editar.
   * @returns {void}
   */
  abrirParaEditar(usuario: Usuario) {
    this.usuarioSeleccionado = usuario;
    this.modoEdicionActivo = true;
    this.modalVisible = true;
  }

  /**
   * Elimina un usuario del sistema previa confirmación a través de una alerta nativa del navegador.
   * Recarga la lista de usuarios si la operación es exitosa.
   * @param {number | undefined} id - Identificador único del usuario a eliminar.
   * @returns {void}
   */
  suspenderUsuario(id: number | undefined) {
    if (!id) return;

    // Alerta nativa para evitar borrados accidentales
    if (confirm('¿Está seguro de que desea suspender y eliminar a este lector de los registros?')) {
      this.usuarioService.eliminarUsuario(id).subscribe({
        next: (respuesta) => {
          this.cargarUsuarios();
        },
        error: (err) => {
          this.mensajeError = 'Error en los rotativos al intentar eliminar el registro.';
        }
      });
    }
  }

  /**
   * Cierra el modal y solicita recargar la lista de usuarios para reflejar posibles cambios.
   * Se ejecuta habitualmente tras emitir el evento de cierre desde el componente hijo.
   * @returns {void}
   */
  cerrarModal() {
    this.modalVisible = false;
    this.cargarUsuarios();
  }
}
