package co.edu.unbosque.periodicazo.dto;

import java.time.LocalDateTime;
import java.util.Objects;
import co.edu.unbosque.periodicazo.entity.Publicacion.Categoria;
import co.edu.unbosque.periodicazo.entity.Publicacion.Tipo;

/**
 * Objeto de transferencia de datos (DTO) para la entidad Publicacion.
 * <p>
 * Se utiliza para transportar la información de las publicaciones entre
 * las capas de la aplicación (controller, service) sin exponer directamente
 * la entidad JPA. Soporta dos tipos de publicaciones: noticias y horóscopos,
 * donde cada tipo utiliza un subconjunto diferente de campos.
 * </p>
 * <ul>
 *   <li><b>NOTICIA:</b> usa título, contenido, autor, editorial y categoría.</li>
 *   <li><b>HOROSCOPO:</b> usa título, contenido, signo y elemento.</li>
 * </ul>
 */
public class PublicacionDTO {

	/** Identificador único de la publicación. */
	private Long id;

	/** Tipo de publicación: {@link Tipo#NOTICIA} o {@link Tipo#HOROSCOPO}. */
	private Tipo tipo;

	/** Título de la publicación. */
	private String titulo;

	/** Contenido o cuerpo de la publicación. */
	private String contenido;

	/** Nombre del autor de la publicación. Solo aplica para noticias. */
	private String autor;

	/** Nombre de la editorial de la publicación. Solo aplica para noticias. */
	private String editorial;

	/** Fecha y hora de creación de la publicación. Se inicializa con la fecha actual. */
	private LocalDateTime fecha;

	/**
	 * Categoría temática de la publicación.
	 * Solo aplica para noticias ({@link Tipo#NOTICIA}).
	 */
	private Categoria categoria;

	/**
	 * Signo zodiacal al que va dirigido el horóscopo.
	 * Solo aplica para horóscopos ({@link Tipo#HOROSCOPO}).
	 */
	private String signo;

	/**
	 * Elemento zodiacal asociado al horóscopo (fuego, tierra, aire, agua).
	 * Solo aplica para horóscopos ({@link Tipo#HOROSCOPO}).
	 */
	private String elemento;

	/**
	 * Constructor vacío que inicializa la fecha con el momento actual.
	 * Requerido para la deserialización JSON y el uso con ModelMapper.
	 */
	public PublicacionDTO() {
		this.fecha = LocalDateTime.now();
	}

	/**
	 * Constructor completo con todos los campos del DTO.
	 *
	 * @param id        identificador único de la publicación
	 * @param tipo      tipo de publicación (NOTICIA o HOROSCOPO)
	 * @param titulo    título de la publicación
	 * @param contenido contenido o cuerpo de la publicación
	 * @param autor     nombre del autor (solo para noticias)
	 * @param editorial nombre de la editorial (solo para noticias)
	 * @param fecha     fecha y hora de creación
	 * @param categoria categoría temática (solo para noticias)
	 * @param signo     signo zodiacal (solo para horóscopos)
	 * @param elemento  elemento zodiacal (solo para horóscopos)
	 */
	public PublicacionDTO(Long id, Tipo tipo, String titulo, String contenido, String autor, String editorial,
			LocalDateTime fecha, Categoria categoria, String signo, String elemento) {
		super();
		this.id = id;
		this.tipo = tipo;
		this.titulo = titulo;
		this.contenido = contenido;
		this.autor = autor;
		this.editorial = editorial;
		this.fecha = fecha;
		this.categoria = categoria;
		this.signo = signo;
		this.elemento = elemento;
	}

	/**
	 * Obtiene el identificador único de la publicación.
	 *
	 * @return identificador de la publicación
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Establece el identificador único de la publicación.
	 *
	 * @param id identificador de la publicación
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Obtiene el tipo de publicación.
	 *
	 * @return tipo de publicación (NOTICIA o HOROSCOPO)
	 */
	public Tipo getTipo() {
		return tipo;
	}

	/**
	 * Establece el tipo de publicación.
	 *
	 * @param tipo tipo de publicación (NOTICIA o HOROSCOPO)
	 */
	public void setTipo(Tipo tipo) {
		this.tipo = tipo;
	}

	/**
	 * Obtiene el título de la publicación.
	 *
	 * @return título de la publicación
	 */
	public String getTitulo() {
		return titulo;
	}

	/**
	 * Establece el título de la publicación.
	 *
	 * @param titulo título de la publicación
	 */
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	/**
	 * Obtiene el contenido o cuerpo de la publicación.
	 *
	 * @return contenido de la publicación
	 */
	public String getContenido() {
		return contenido;
	}

	/**
	 * Establece el contenido o cuerpo de la publicación.
	 *
	 * @param contenido contenido de la publicación
	 */
	public void setContenido(String contenido) {
		this.contenido = contenido;
	}

	/**
	 * Obtiene el nombre del autor de la publicación.
	 *
	 * @return nombre del autor (solo aplica para noticias)
	 */
	public String getAutor() {
		return autor;
	}

	/**
	 * Establece el nombre del autor de la publicación.
	 *
	 * @param autor nombre del autor (solo aplica para noticias)
	 */
	public void setAutor(String autor) {
		this.autor = autor;
	}

	/**
	 * Obtiene el nombre de la editorial de la publicación.
	 *
	 * @return nombre de la editorial (solo aplica para noticias)
	 */
	public String getEditorial() {
		return editorial;
	}

	/**
	 * Establece el nombre de la editorial de la publicación.
	 *
	 * @param editorial nombre de la editorial (solo aplica para noticias)
	 */
	public void setEditorial(String editorial) {
		this.editorial = editorial;
	}

	/**
	 * Obtiene la fecha y hora de creación de la publicación.
	 *
	 * @return fecha y hora de la publicación
	 */
	public LocalDateTime getFecha() {
		return fecha;
	}

	/**
	 * Establece la fecha y hora de creación de la publicación.
	 *
	 * @param fecha fecha y hora de la publicación
	 */
	public void setFecha(LocalDateTime fecha) {
		this.fecha = fecha;
	}

	/**
	 * Obtiene la categoría temática de la publicación.
	 *
	 * @return categoría de la publicación (solo aplica para noticias)
	 */
	public Categoria getCategoria() {
		return categoria;
	}

	/**
	 * Establece la categoría temática de la publicación.
	 *
	 * @param categoria categoría de la publicación (solo aplica para noticias)
	 */
	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}

	/**
	 * Obtiene el signo zodiacal del horóscopo.
	 *
	 * @return signo zodiacal (solo aplica para horóscopos)
	 */
	public String getSigno() {
		return signo;
	}

	/**
	 * Establece el signo zodiacal del horóscopo.
	 *
	 * @param signo signo zodiacal (solo aplica para horóscopos)
	 */
	public void setSigno(String signo) {
		this.signo = signo;
	}

	/**
	 * Obtiene el elemento zodiacal del horóscopo.
	 *
	 * @return elemento zodiacal (solo aplica para horóscopos)
	 */
	public String getElemento() {
		return elemento;
	}

	/**
	 * Establece el elemento zodiacal del horóscopo.
	 *
	 * @param elemento elemento zodiacal (solo aplica para horóscopos)
	 */
	public void setElemento(String elemento) {
		this.elemento = elemento;
	}

	/**
	 * Retorna una representación en cadena del objeto {@code PublicacionDTO}.
	 *
	 * @return cadena con todos los campos del DTO
	 */
	@Override
	public String toString() {
		return "PublicacionDTO [id=" + id + ", tipo=" + tipo + ", titulo=" + titulo + ", contenido=" + contenido
				+ ", autor=" + autor + ", editorial=" + editorial + ", fecha=" + fecha + ", categoria=" + categoria
				+ ", signo=" + signo + ", elemento=" + elemento + "]";
	}

	/**
	 * Calcula el código hash del objeto basándose en todos sus campos.
	 *
	 * @return código hash del objeto
	 */
	@Override
	public int hashCode() {
		return Objects.hash(autor, categoria, contenido, editorial, elemento, fecha, id, signo, tipo, titulo);
	}

	/**
	 * Compara este objeto con otro para determinar si son iguales,
	 * basándose en todos sus campos.
	 *
	 * @param obj objeto con el que se compara
	 * @return {@code true} si todos los campos son iguales, {@code false} en caso contrario
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PublicacionDTO other = (PublicacionDTO) obj;
		return Objects.equals(autor, other.autor) && categoria == other.categoria
				&& Objects.equals(contenido, other.contenido) && Objects.equals(editorial, other.editorial)
				&& Objects.equals(elemento, other.elemento) && Objects.equals(fecha, other.fecha)
				&& Objects.equals(id, other.id) && Objects.equals(signo, other.signo) && tipo == other.tipo
				&& Objects.equals(titulo, other.titulo);
	}
}