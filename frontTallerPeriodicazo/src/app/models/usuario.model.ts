/**
 * Representa la estructura de datos de un usuario dentro del sistema.
 * Incluye los detalles de autenticación y las banderas de seguridad (compatibles con Spring Security).
 * @interface Usuario
 */
export interface Usuario {
  /**
   * Identificador único del usuario en la base de datos.
   * Es opcional ya que un usuario nuevo aún no tiene ID antes de ser guardado.
   * @type {number | undefined}
   */
  id?: number;

  /**
   * Nombre de usuario utilizado para iniciar sesión en el sistema.
   * @type {string}
   */
  username: string;

  /**
   * Contraseña de acceso del usuario.
   * Es opcional para no exponerla en respuestas del servidor o al editar perfiles sin cambiar la clave.
   * @type {string | undefined}
   */
  password?: string;

  /**
   * Rol o cargo asignado al usuario (ej. 'ADMIN', 'EDITOR', 'COMENTADOR', 'USUARIO').
   * Determina los permisos de acceso dentro del sistema.
   * @type {string}
   */
  role: string;

  /**
   * Indica si la cuenta del usuario no ha expirado.
   * @type {boolean | undefined}
   */
  accountNonExpired?: boolean;

  /**
   * Indica si la cuenta del usuario no está bloqueada.
   * @type {boolean | undefined}
   */
  accountNonLocked?: boolean;

  /**
   * Indica si las credenciales (contraseña) del usuario no han expirado.
   * @type {boolean | undefined}
   */
  credentialsNonExpired?: boolean;

  /**
   * Indica si el usuario está habilitado y activo en el sistema.
   * @type {boolean | undefined}
   */
  enabled?: boolean;
}
