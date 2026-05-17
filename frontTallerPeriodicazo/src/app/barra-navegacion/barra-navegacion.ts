import { Component, OnInit } from '@angular/core';
import { RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common'; // Necesario para *ngIf

@Component({
  selector: 'app-barra-navegacion',
  standalone: true,
  imports: [RouterModule, CommonModule],
  templateUrl: './barra-navegacion.html',
  styleUrls: ['./barra-navegacion.css']
})
export class BarraNavegacion implements OnInit {
  rolActual: string = '';

  ngOnInit(): void {
    this.rolActual = localStorage.getItem('rol_diario') || '';
  }
}
