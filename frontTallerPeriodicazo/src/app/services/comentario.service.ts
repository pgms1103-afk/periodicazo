import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Comentario } from '../models/comentario.model';

@Injectable({
  providedIn: 'root'
})
export class ComentarioService {
  private urlComentario = 'http://localhost:8080/private/comentario';

  constructor(private http: HttpClient) {}

  obtenerPorTitulo(titulo: string): Observable<Comentario[]> {
    const params = new HttpParams().set('titulo', titulo);
    return this.http.get<Comentario[]>(`${this.urlComentario}/mostrarportitulo`, { params });
  }

  crearComentario(comentario: Comentario): Observable<any> {
    return this.http.post(`${this.urlComentario}/crearcomentario`, comentario, { responseType: 'text' });
  }
}
