import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-lista-noticias',
  standalone: true,
  imports: [RouterModule],
  templateUrl: './lista-noticias.html',
  styleUrls: ['./lista-noticias.css']
})
export class ListaNoticias {}
