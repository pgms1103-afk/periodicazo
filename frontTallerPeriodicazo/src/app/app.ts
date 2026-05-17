import { Component } from '@angular/core';
import { RouterOutlet, Router, NavigationEnd } from '@angular/router';
import { CommonModule } from '@angular/common';
import { BarraNavegacion } from './barra-navegacion/barra-navegacion';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, CommonModule, BarraNavegacion],
  templateUrl: './app.html',
  styleUrls: ['./app.css']
})
export class App {
  mostrarBarra: boolean = true;

  constructor(private router: Router) {
    this.router.events.subscribe((event) => {
      if (event instanceof NavigationEnd) {
        this.mostrarBarra = event.url !== '/inicio-sesion' && event.url !== '/';
      }
    });
  }
}
