import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Usuario } from '../models/usuario.model';

@Injectable({
  providedIn: 'root'
})
export class UsuarioService {
  private urlAdmin = 'http://localhost:8080/admin';

  constructor(private http: HttpClient) {}

  obtenerUsuarios(): Observable<Usuario[]> {
    return this.http.get<Usuario[]>(`${this.urlAdmin}/mostrarusuario`);
  }

  crearUsuario(usuario: Usuario): Observable<any> {
    return this.http.post(`${this.urlAdmin}/crearusuario`, usuario, { responseType: 'text' });
  }

  actualizarUsuario(id: number, usuario: Usuario): Observable<any> {
    const parametros = new HttpParams().set('id', id.toString());
    return this.http.put(`${this.urlAdmin}/actualizarusuario`, usuario, {
      params: parametros,
      responseType: 'text'
    });
  }

  eliminarUsuario(id: number): Observable<any> {
    const parametros = new HttpParams().set('id', id.toString());
    return this.http.delete(`${this.urlAdmin}/eliminarusuario`, {
      params: parametros,
      responseType: 'text'
    });
  }
}
