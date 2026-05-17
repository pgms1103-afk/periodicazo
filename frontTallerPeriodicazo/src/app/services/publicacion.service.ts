import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Publicacion } from '../models/publicacion.model';

@Injectable({
  providedIn: 'root'
})
export class PublicacionService {
  private urlPublicacion = 'http://localhost:8080/private/publicacion';

  constructor(private http: HttpClient) {}

  obtenerPorTipo(tipo: string): Observable<Publicacion[]> {
    const params = new HttpParams().set('tipo', tipo);
    return this.http.get<Publicacion[]>(`${this.urlPublicacion}/mostrarportipo`, { params });
  }

  crearPublicacion(pub: Publicacion): Observable<any> {
    return this.http.post(`${this.urlPublicacion}/crearpublicacion`, pub, { responseType: 'text' });
  }

  actualizarPublicacion(id: number, pub: Publicacion): Observable<any> {
    const parametros = new HttpParams().set('id', id.toString());
    return this.http.put(`${this.urlPublicacion}/editarPublicacion`, pub, {
      params: parametros,
      responseType: 'text'
    });
  }

  eliminarPublicacion(id: number): Observable<any> {
    const parametros = new HttpParams().set('id', id.toString());
    return this.http.delete(`${this.urlPublicacion}/eliminarpublicacion`, {
      params: parametros,
      responseType: 'text'
    });
  }
}
