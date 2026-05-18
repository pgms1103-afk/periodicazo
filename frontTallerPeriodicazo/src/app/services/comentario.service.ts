import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Comentario } from '../models/comentario.model';

/**
 * Servicio encargado de gestionar las operaciones CRUD para los comentarios de las publicaciones.
 * Se comunica con los endpoints privados del backend que requieren autenticación.
 * @class ComentarioService
 */
@Injectable({
  providedIn: 'root'
})
export class ComentarioService {
  /**
   * URL base del servidor backend para la gestión de comentarios.
   * @type {string}
   * @private
   */
  private urlComentario = 'http://localhost:8080/private/comentario';

  /**
   * Crea una instancia de ComentarioService.
   * @param {HttpClient} http - Cliente HTTP de Angular para realizar las peticiones al servidor.
   */
  constructor(private http: HttpClient) {}

  /**
   * Obtiene una lista de comentarios asociados al título de una publicación específica.
   * @param {string} titulo - El título exacto de la publicación (noticia u horóscopo).
   * @returns {Observable<Comentario[]>} Un Observable que emite un arreglo de objetos tipo Comentario.
   */
  obtenerPorTitulo(titulo: string): Observable<Comentario[]> {
    const params = new HttpParams().set('titulo', titulo);
    return this.http.get<Comentario[]>(`${this.urlComentario}/mostrarportitulo`, { params });
  }

  /**
   * Envía una petición para crear un nuevo comentario en el sistema.
   * @param {Comentario} comentario - El objeto con los datos del nuevo comentario a crear.
   * @returns {Observable<any>} Un Observable con la respuesta en texto del servidor.
   */
  crearComentario(comentario: Comentario): Observable<any> {
    return this.http.post(`${this.urlComentario}/crearcomentario`, comentario, { responseType: 'text' });
  }

  /**
   * Actualiza el contenido de un comentario existente mediante petición PUT.
   * @param {number} id - El identificador único del comentario que se desea actualizar.
   * @param {Comentario} comentario - El objeto con los nuevos datos del comentario.
   * @returns {Observable<unknown>} Un Observable con la respuesta en texto del servidor confirmando la operación.
   */
  actualizarComentario(id: number, comentario: Comentario): Observable<unknown> {
    const params = new HttpParams().set('id', id.toString());
    return this.http.put(`${this.urlComentario}/actualizarcomentario`, comentario, { params, responseType: 'text' });
  }

  /**
   * Elimina permanentemente un comentario del sistema mediante petición DELETE.
   * @param {number} id - El identificador único del comentario a eliminar.
   * @returns {Observable<unknown>} Un Observable con la respuesta en texto del servidor confirmando la eliminación.
   */
  eliminarComentario(id: number): Observable<unknown> {
    const params = new HttpParams().set('id', id.toString());
    return this.http.delete(`${this.urlComentario}/eliminarcomentario`, { params, responseType: 'text' });
  }
}
