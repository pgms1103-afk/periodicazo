/**
 * Representa la estructura de datos de un comentario realizado en una publicación (noticia u horóscopo).
 * @interface Comentario
 */
export interface Comentario {
  /**
   * Identificador único del comentario en la base de datos.
   * Es opcional ya que no existe hasta que se guarda en el servidor.
   * @type {number | undefined}
   */
  id?: number;

  /**
   * Cuerpo o texto principal del comentario escrito por el usuario.
   * @type {string}
   */
  contenido: string;

  /**
   * Nombre del usuario que escribió el comentario.
   * Si no se provee, se puede asumir como un lector anónimo.
   * @type {string | undefined}
   */
  nombreComentador?: string;

  /**
   * Fecha y hora en la que se realizó el comentario (generalmente en formato ISO).
   * @type {string | undefined}
   */
  fecha?: string;

  /**
   * Identificador de la publicación (artículo u horóscopo) a la que pertenece este comentario.
   * @type {number}
   */
  publicacionId: number;

  /**
   * Título de la publicación a la que está asociado.
   * Sirve para mostrar referencias cruzadas sin consultar toda la publicación.
   * @type {string | undefined}
   */
  nombrePublicacion?: string;
}
