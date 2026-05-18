/**
 * Representa la estructura de datos de una publicación genérica dentro del sistema.
 * Puede utilizarse tanto para artículos de noticias como para predicciones de horóscopos.
 * @interface Publicacion
 */
export interface Publicacion {
  /**
   * Identificador único de la publicación en la base de datos.
   * Es opcional ya que no existe hasta que se guarda en el servidor.
   * @type {number | undefined}
   */
  id?: number;

  /**
   * Define la naturaleza de la publicación (generalmente 'NOTICIA' o 'HOROSCOPO').
   * @type {string}
   */
  tipo: string;

  /**
   * Título principal o encabezado de la publicación.
   * @type {string}
   */
  titulo: string;

  /**
   * Cuerpo de texto principal con el desarrollo de la noticia o la predicción astral.
   * @type {string}
   */
  contenido: string;

  /**
   * Nombre del autor, redactor o corresponsal que escribió el artículo.
   * @type {string | undefined}
   */
  autor?: string;

  /**
   * Sección del periódico o entidad editorial que publica el contenido.
   * @type {string | undefined}
   */
  editorial?: string;

  /**
   * Fecha y hora en la que se generó o publicó el registro (formato ISO).
   * @type {string | undefined}
   */
  fecha?: string;

  /**
   * Categoría temática (ej. 'Política', 'Deportes').
   * Se utiliza principalmente cuando el tipo de publicación es 'NOTICIA'.
   * @type {string | undefined}
   */
  categoria?: string;

  /**
   * Signo zodiacal al que hace referencia el texto.
   * Se utiliza exclusivamente cuando el tipo de publicación es 'HOROSCOPO'.
   * @type {string | undefined}
   */
  signo?: string;

  /**
   * Elemento de la naturaleza asociado al signo zodiacal (Fuego, Tierra, Aire, Agua).
   * Se utiliza exclusivamente cuando el tipo de publicación es 'HOROSCOPO'.
   * @type {string | undefined}
   */
  elemento?: string;
}
