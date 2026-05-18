import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { tap } from 'rxjs/operators';
import { Observable } from 'rxjs';

/**
 * Servicio encargado de gestionar la autenticación de usuarios en el sistema.
 * Proporciona métodos para iniciar sesión, registrar nuevos usuarios y manejar el ciclo de vida del token JWT.
 * @class AuthService
 */
@Injectable({
  providedIn: 'root'
})
export class AuthService {

  /**
   * URL base del servidor backend para los endpoints públicos (sin autenticación requerida).
   * @type {string}
   * @private
   */
  private urlServidor = 'http://localhost:8080/public';

  /**
   * Crea una instancia de AuthService.
   * @param {HttpClient} http - Cliente HTTP de Angular para realizar peticiones al servidor.
   */
  constructor(private http: HttpClient) {}

  /**
   * Envía las credenciales del usuario al servidor para iniciar sesión.
   * Si las credenciales son válidas, intercepta la respuesta para guardar el token JWT,
   * el rol y el nombre de usuario en el almacenamiento local.
   * @param {string} usuario - El nombre de usuario.
   * @param {string} contrasena - La contraseña del usuario.
   * @returns {Observable<unknown>} Un Observable con la respuesta del servidor (generalmente el token y detalles del usuario).
   */
  iniciarSesion(usuario: string, contrasena: string): Observable<any> {
    const body = { username: usuario, password: contrasena };

    return this.http.post<any>(`${this.urlServidor}/login`, body).pipe(
      tap(respuesta => {
        if (respuesta && respuesta.token) {
          this.guardarToken(respuesta.token);
          if (respuesta.role) {
            localStorage.setItem('rol_diario', respuesta.role);
          }
          localStorage.setItem('usuario_diario', usuario);
        }
      })
    );
  }

  /**
   * Envía una petición al servidor para registrar un nuevo usuario con rol predeterminado.
   * @param {string} nombreUsuario - El nombre de usuario deseado.
   * @param {string} contrasena - La contraseña elegida.
   * @returns {Observable<unknown>} Un Observable con la confirmación textual del servidor.
   */
  registrarUsuario(nombreUsuario: string, contrasena: string): Observable<any> {
    const body = { username: nombreUsuario, password: contrasena };

    return this.http.post(`${this.urlServidor}/registrarusuario`, body, {
      responseType: 'text'
    });
  }

  /**
   * Almacena el token JWT de autenticación en el `localStorage` del navegador.
   * @param {string} token - La cadena del token JWT proporcionada por el servidor.
   * @returns {void}
   */
  guardarToken(token: string) {
    localStorage.setItem('token_diario', token);
  }

  /**
   * Recupera el token JWT de autenticación desde el `localStorage`.
   * Utilizado principalmente por el interceptor HTTP para adjuntarlo a las cabeceras de peticiones seguras.
   * @returns {string | null} El token almacenado, o `null` si no existe una sesión activa.
   */
  obtenerToken(): string | null {
    return localStorage.getItem('token_diario');
  }

  /**
   * Destruye la sesión activa eliminando el token, el rol y el nombre de usuario del `localStorage`.
   * @returns {void}
   */
  cerrarSesion() {
    localStorage.removeItem('token_diario');
    localStorage.removeItem('rol_diario');
    localStorage.removeItem('usuario_diario');
  }
}
