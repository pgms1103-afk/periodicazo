package co.edu.unbosque.periodicazo.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Column;

/**
 * Entidad JPA que representa una publicación del periódico.
 * <p>
 * Soporta dos tipos de publicaciones definidos por el enum {@link Tipo}:
 * </p>
 * <ul>
 *   <li><b>NOTICIA:</b> usa los campos título, contenido, autor, editorial y categoría.</li>
 *   <li><b>HOROSCOPO:</b> usa los campos título, contenido, signo y elemento.</li>
 * </ul>
 * <p>
 * Una publicación puede tener múltiples {@link Comentario}s asociados.
 * Al eliminar una publicación, sus comentarios se eliminan en cascada.
 * </p>
 */
@Entity
public class Publicacion {

	/**
	 * Identificador único de la publicación, generado automáticamente por la base de datos.
	 */
	private @Id @GeneratedValue(strategy = GenerationType.IDENTITY) Long id;

	/**
	 * Tipo de publicación: {@link Tipo#NOTICIA} o {@link Tipo#HOROSCOPO}.
	 * Se almacena como texto en la base de datos.
	 */
	@Enumerated(EnumType.STRING)
	private Tipo tipo;

	/** Título de la publicación. */
	private String titulo;

	/** Contenido o cuerpo de la publicación. */
	@Column(length = 65535)
	private String contenido;

	/** Nombre del autor de la publicación. Solo aplica para noticias. */
	private String autor;

	/** Nombre de la editorial de la publicación. Solo aplica para noticias. */
	private String editorial;

	/** Fecha y hora de creación de la publicación. Se inicializa con el momento actual. */
	private LocalDateTime fecha;

	/**
	 * Categoría temática de la publicación.
	 * Solo aplica para noticias ({@link Tipo#NOTICIA}).
	 * Se almacena como texto en la base de datos.
	 */
	@Enumerated(EnumType.STRING)
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
	 * Lista de comentarios asociados a esta publicación.
	 * Relación uno-a-muchos con la entidad {@link Comentario}.
	 * Los comentarios se eliminan en cascada al eliminar la publicación.
	 */
	@OneToMany(mappedBy = "publicacion", cascade = CascadeType.ALL)
	private List<Comentario> comentarios;

	/**
	 * Enumeración de las categorías temáticas disponibles para las noticias.
	 */
	public enum Categoria {
		/** Noticias relacionadas con política. */
		Politica,
		/** Noticias relacionadas con economía. */
		Economia,
		/** Noticias relacionadas con cultura. */
		Cultura,
		/** Noticias relacionadas con deportes. */
		Deportes,
		/** Noticias relacionadas con tecnología. */
		Tecnologia
	}

	/**
	 * Enumeración de los tipos de publicación disponibles en el periódico.
	 */
	public enum Tipo {
		/** Publicación de tipo noticia con categoría, autor y editorial. */
		NOTICIA,
		/** Publicación de tipo horóscopo con signo y elemento zodiacal. */
		HOROSCOPO
	}

	/**
	 * Constructor vacío que inicializa la fecha con el momento actual.
	 * Requerido por JPA para la instanciación de entidades.
	 */
	public Publicacion() {
		this.fecha = LocalDateTime.now();
	}

	/**
	 * Constructor completo con todos los campos de la entidad.
	 *
	 * @param tipo        tipo de publicación (NOTICIA o HOROSCOPO)
	 * @param titulo      título de la publicación
	 * @param contenido   contenido o cuerpo de la publicación
	 * @param autor       nombre del autor (solo para noticias)
	 * @param editorial   nombre de la editorial (solo para noticias)
	 * @param fecha       fecha y hora de creación
	 * @param categoria   categoría temática (solo para noticias)
	 * @param signo       signo zodiacal (solo para horóscopos)
	 * @param elemento    elemento zodiacal (solo para horóscopos)
	 * @param comentarios lista de comentarios asociados a la publicación
	 */
	public Publicacion(Tipo tipo, String titulo, String contenido, String autor, String editorial, LocalDateTime fecha,
			Categoria categoria, String signo, String elemento, List<Comentario> comentarios) {
		super();
		this.tipo = tipo;
		this.titulo = titulo;
		this.contenido = contenido;
		this.autor = autor;
		this.editorial = editorial;
		this.fecha = fecha;
		this.categoria = categoria;
		this.signo = signo;
		this.elemento = elemento;
		this.comentarios = new ArrayList<>(comentarios);
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
	 * Obtiene una copia defensiva de la lista de comentarios de la publicación.
	 * <p>
	 * Se devuelve una copia para evitar modificaciones externas directas
	 * sobre la lista interna de la entidad.
	 * </p>
	 *
	 * @return nueva lista con los comentarios asociados a la publicación
	 */
	public List<Comentario> getComentarios() {
		return new ArrayList<>(this.comentarios);
	}

	/**
	 * Establece la lista de comentarios de la publicación a partir de una copia defensiva.
	 * <p>
	 * Se almacena una copia de la lista recibida para evitar que modificaciones
	 * externas afecten la lista interna de la entidad.
	 * </p>
	 *
	 * @param comentarios lista de comentarios a asociar con la publicación
	 */
	public void setComentarios(List<Comentario> comentarios) {
		this.comentarios = new ArrayList<>(comentarios);
	}

	/**
	 * Retorna una representación en cadena del objeto {@code Publicacion}.
	 *
	 * @return cadena con todos los campos de la entidad
	 */
	@Override
	public String toString() {
		return "Publicacion [id=" + id + ", tipo=" + tipo + ", titulo=" + titulo + ", contenido=" + contenido
				+ ", autor=" + autor + ", editorial=" + editorial + ", fecha=" + fecha + ", categoria=" + categoria
				+ ", signo=" + signo + ", elemento=" + elemento + ", comentarios=" + comentarios + "]";
	}

	/**
	 * Calcula el código hash del objeto basándose en todos sus campos.
	 *
	 * @return código hash del objeto
	 */
	@Override
	public int hashCode() {
		return Objects.hash(autor, categoria, comentarios, contenido, editorial, elemento, fecha, id, signo, tipo,
				titulo);
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
		Publicacion other = (Publicacion) obj;
		return Objects.equals(autor, other.autor) && categoria == other.categoria
				&& Objects.equals(comentarios, other.comentarios) && Objects.equals(contenido, other.contenido)
				&& Objects.equals(editorial, other.editorial) && Objects.equals(elemento, other.elemento)
				&& Objects.equals(fecha, other.fecha) && Objects.equals(id, other.id)
				&& Objects.equals(signo, other.signo) && tipo == other.tipo && Objects.equals(titulo, other.titulo);
	}
}