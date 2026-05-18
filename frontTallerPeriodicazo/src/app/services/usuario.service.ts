import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Usuario } from '../models/usuario.model';

/**
 * Servicio encargado de gestionar las operaciones CRUD de los usuarios del sistema.
 * Se comunica con los endpoints de administración del backend, los cuales requieren privilegios elevados.
 * @class UsuarioService
 */
@Injectable({
  providedIn: 'root'
})
export class UsuarioService {
  /**
   * URL base del servidor backend para las operaciones de administración de usuarios.
   * @type {string}
   * @private
   */
  private urlAdmin = 'http://localhost:8080/admin';

  /**
   * Crea una instancia de UsuarioService.
   * @param {HttpClient} http - Cliente HTTP de Angular para realizar las peticiones al servidor.
   */
  constructor(private http: HttpClient) {}

  /**
   * Obtiene la lista completa de todos los usuarios registrados en el sistema.
   * @returns {Observable<Usuario[]>} Un Observable que emite un arreglo de objetos tipo Usuario.
   */
  obtenerUsuarios(): Observable<Usuario[]> {
    return this.http.get<Usuario[]>(`${this.urlAdmin}/mostrarusuario`);
  }

  /**
   * Envía una petición para registrar un nuevo usuario desde el panel de administración.
   * @param {Usuario} usuario - El objeto con los datos del nuevo usuario a crear.
   * @returns {Observable<any>} Un Observable con la respuesta en texto del servidor confirmando la creación.
   */
  crearUsuario(usuario: Usuario): Observable<any> {
    return this.http.post(`${this.urlAdmin}/crearusuario`, usuario, { responseType: 'text' });
  }

  /**
   * Actualiza los datos y/o credenciales de un usuario existente mediante petición PUT.
   * @param {number} id - El identificador único del usuario que se desea actualizar.
   * @param {Usuario} usuario - El objeto con los nuevos datos del usuario.
   * @returns {Observable<any>} Un Observable con la respuesta en texto del servidor confirmando la actualización.
   */
  actualizarUsuario(id: number, usuario: Usuario): Observable<any> {
    const parametros = new HttpParams().set('id', id.toString());
    return this.http.put(`${this.urlAdmin}/actualizarusuario`, usuario, {
      params: parametros,
      responseType: 'text'
    });
  }

  /**
   * Elimina permanentemente un usuario del sistema mediante petición DELETE.
   * @param {number} id - El identificador único del usuario a eliminar.
   * @returns {Observable<any>} Un Observable con la respuesta en texto del servidor confirmando la eliminación.
   */
  eliminarUsuario(id: number): Observable<any> {
    const parametros = new HttpParams().set('id', id.toString());
    return this.http.delete(`${this.urlAdmin}/eliminarusuario`, {
      params: parametros,
      responseType: 'text'
    });
  }
}
