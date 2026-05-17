import { Component, OnInit } from '@angular/core';
import { RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormularioUsuario } from '../formulario-usuario/formulario-usuario';
import { UsuarioService } from '../../services/usuario.service';
import { Usuario } from '../../models/usuario.model';

@Component({
  selector: 'app-lista-usuarios',
  standalone: true,
  imports: [RouterModule, CommonModule, FormularioUsuario],
  templateUrl: './lista-usuarios.html',
  styleUrls: ['./lista-usuarios.css']
})
export class ListaUsuarios implements OnInit {
  usuarios: Usuario[] = [];
  mensajeError: string = '';

  modalVisible: boolean = false;
  modoEdicionActivo: boolean = false;

  usuarioSeleccionado: Usuario | null = null;

  constructor(private usuarioService: UsuarioService) {}

  ngOnInit(): void {
    this.cargarUsuarios();
  }

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

  abrirParaCrear() {
    this.usuarioSeleccionado = null;
    this.modoEdicionActivo = false;
    this.modalVisible = true;
  }

  abrirParaEditar(usuario: Usuario) {
    this.usuarioSeleccionado = usuario;
    this.modoEdicionActivo = true;
    this.modalVisible = true;
  }

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

  cerrarModal() {
    this.modalVisible = false;
    this.cargarUsuarios();
  }
}
