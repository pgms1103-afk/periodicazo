import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-inicio-sesion',
  standalone: true,
  imports: [RouterModule, CommonModule],
  templateUrl: './inicio-sesion.html',
  styleUrls: ['./inicio-sesion.css']
})
export class InicioSesion {
  vistaLogin: boolean = true;

  cambiarPestana(esLogin: boolean) {
    this.vistaLogin = esLogin;
  }
}
