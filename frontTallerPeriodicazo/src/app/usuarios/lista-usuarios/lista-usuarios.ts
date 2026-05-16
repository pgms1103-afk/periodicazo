import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormularioUsuario } from '../formulario-usuario/formulario-usuario';

@Component({
  selector: 'app-lista-usuarios',
  standalone: true,
  imports: [RouterModule, CommonModule, FormularioUsuario],
  templateUrl: './lista-usuarios.html',
  styleUrls: ['./lista-usuarios.css']
})
export class ListaUsuarios {

  modalVisible: boolean = false;
  modoEdicionActivo: boolean = false;

  abrirParaCrear() {
    this.modoEdicionActivo = false;
    this.modalVisible = true;
  }

  abrirParaEditar() {
    this.modoEdicionActivo = true;
    this.modalVisible = true;
  }

  cerrarModal() {
    this.modalVisible = false;
  }
}
