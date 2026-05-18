import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Publicacion } from '../models/publicacion.model';

/**
 * Servicio encargado de gestionar las operaciones CRUD para las publicaciones (noticias y horóscopos).
 * Se comunica con los endpoints privados del backend que requieren autenticación.
 * @class PublicacionService
 */
@Injectable({
  providedIn: 'root'
})
export class PublicacionService {
  /**
   * URL base del servidor backend para la gestión de publicaciones.
   * @type {string}
   * @private
   */
  private urlPublicacion = 'http://localhost:8080/private/publicacion';

  /**
   * Crea una instancia de PublicacionService.
   * @param {HttpClient} http - Cliente HTTP de Angular para realizar las peticiones al servidor.
   */
  constructor(private http: HttpClient) {}

  /**
   * Obtiene una lista de publicaciones filtradas por su tipo.
   * @param {string} tipo - El tipo de publicación a buscar (por ejemplo, 'NOTICIA' o 'HOROSCOPO').
   * @returns {Observable<Publicacion[]>} Un Observable que emite un arreglo de objetos tipo Publicacion.
   */
  obtenerPorTipo(tipo: string): Observable<Publicacion[]> {
    const params = new HttpParams().set('tipo', tipo);
    return this.http.get<Publicacion[]>(`${this.urlPublicacion}/mostrarportipo`, { params });
  }

  /**
   * Envía una petición para crear una nueva publicación en el sistema.
   * @param {Publicacion} pub - El objeto con los datos de la nueva publicación a crear.
   * @returns {Observable<any>} Un Observable con la respuesta en texto del servidor confirmando la operación.
   */
  crearPublicacion(pub: Publicacion): Observable<any> {
    return this.http.post(`${this.urlPublicacion}/crearpublicacion`, pub, { responseType: 'text' });
  }

  /**
   * Actualiza los datos de una publicación existente mediante petición PUT.
   * @param {number} id - El identificador único de la publicación que se desea actualizar.
   * @param {Publicacion} pub - El objeto con los nuevos datos de la publicación.
   * @returns {Observable<any>} Un Observable con la respuesta en texto del servidor confirmando la operación.
   */
  actualizarPublicacion(id: number, pub: Publicacion): Observable<any> {
    const parametros = new HttpParams().set('id', id.toString());
    return this.http.put(`${this.urlPublicacion}/editarPublicacion`, pub, {
      params: parametros,
      responseType: 'text'
    });
  }

  /**
   * Elimina permanentemente una publicación del sistema mediante petición DELETE.
   * @param {number} id - El identificador único de la publicación a eliminar.
   * @returns {Observable<any>} Un Observable con la respuesta en texto del servidor confirmando la eliminación.
   */
  eliminarPublicacion(id: number): Observable<any> {
    const parametros = new HttpParams().set('id', id.toString());
    return this.http.delete(`${this.urlPublicacion}/eliminarpublicacion`, {
      params: parametros,
      responseType: 'text'
    });
  }
}
