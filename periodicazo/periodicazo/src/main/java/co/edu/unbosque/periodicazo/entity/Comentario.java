package co.edu.unbosque.periodicazo.entity;

import java.time.LocalDateTime;
import java.util.Objects;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

/**
 * Entidad JPA que representa un comentario realizado sobre una publicación del periódico.
 * <p>
 * Un comentario está siempre asociado a una {@link Publicacion} existente mediante
 * una relación muchos-a-uno. Cada comentario registra su contenido, el nombre del
 * comentador y la fecha y hora en que fue creado.
 * </p>
 */
@Entity
public class Comentario {

	/**
	 * Identificador único del comentario, generado automáticamente por la base de datos.
	 */
	private @Id @GeneratedValue(strategy = GenerationType.IDENTITY) Long id;

	/** Contenido textual del comentario. */
	private String contenido;

	/** Nombre del usuario que realizó el comentario. */
	private String nombreComentador;

	/** Fecha y hora en que fue creado el comentario. Se inicializa con el momento actual. */
	private LocalDateTime fecha;

	/**
	 * Publicación a la que pertenece el comentario.
	 * Relación muchos-a-uno con la entidad {@link Publicacion}.
	 */
	@ManyToOne
	@JoinColumn(name = "publicacion_id")
	private Publicacion publicacion;

	/**
	 * Constructor vacío que inicializa la fecha con el momento actual.
	 * Requerido por JPA para la instanciación de entidades.
	 */
	public Comentario() {
		this.fecha = LocalDateTime.now();
	}

	/**
	 * Constructor completo con todos los campos de la entidad.
	 *
	 * @param id               identificador único del comentario
	 * @param contenido        contenido textual del comentario
	 * @param nombreComentador nombre del usuario que realizó el comentario
	 * @param fecha            fecha y hora de creación del comentario
	 * @param publicacion      publicación a la que pertenece el comentario
	 */
	public Comentario(Long id, String contenido, String nombreComentador, LocalDateTime fecha,
			Publicacion publicacion) {
		super();
		this.id = id;
		this.contenido = contenido;
		this.nombreComentador = nombreComentador;
		this.fecha = fecha;
		this.publicacion = publicacion;
	}

	/**
	 * Obtiene el identificador único del comentario.
	 *
	 * @return identificador del comentario
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Establece el identificador único del comentario.
	 *
	 * @param id identificador del comentario
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Obtiene el contenido textual del comentario.
	 *
	 * @return contenido del comentario
	 */
	public String getContenido() {
		return contenido;
	}

	/**
	 * Establece el contenido textual del comentario.
	 *
	 * @param contenido contenido del comentario
	 */
	public void setContenido(String contenido) {
		this.contenido = contenido;
	}

	/**
	 * Obtiene el nombre del usuario que realizó el comentario.
	 *
	 * @return nombre del comentador
	 */
	public String getNombreComentador() {
		return nombreComentador;
	}

	/**
	 * Establece el nombre del usuario que realizó el comentario.
	 *
	 * @param nombreComentador nombre del comentador
	 */
	public void setnombreComentador(String nombreComentador) {
		this.nombreComentador = nombreComentador;
	}

	/**
	 * Obtiene la fecha y hora de creación del comentario.
	 *
	 * @return fecha y hora del comentario
	 */
	public LocalDateTime getFecha() {
		return fecha;
	}

	/**
	 * Establece la fecha y hora de creación del comentario.
	 *
	 * @param fecha fecha y hora del comentario
	 */
	public void setFecha(LocalDateTime fecha) {
		this.fecha = fecha;
	}

	/**
	 * Obtiene la publicación a la que pertenece el comentario.
	 *
	 * @return publicación asociada al comentario
	 */
	public Publicacion getPublicacion() {
		return publicacion;
	}

	/**
	 * Establece la publicación a la que pertenece el comentario.
	 *
	 * @param publicacion publicación asociada al comentario
	 */
	public void setPublicacion(Publicacion publicacion) {
		this.publicacion = publicacion;
	}

	/**
	 * Retorna una representación en cadena del objeto {@code Comentario}.
	 *
	 * @return cadena con todos los campos de la entidad
	 */
	@Override
	public String toString() {
		return "Comentario [id=" + id + ", contenido=" + contenido + ", nombre=" + nombreComentador + ", fecha=" + fecha
				+ ", publicacion=" + publicacion + "]";
	}

	/**
	 * Calcula el código hash del objeto basándose en todos sus campos.
	 *
	 * @return código hash del objeto
	 */
	@Override
	public int hashCode() {
		return Objects.hash(contenido, fecha, id, nombreComentador, publicacion);
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
		Comentario other = (Comentario) obj;
		return Objects.equals(contenido, other.contenido) && Objects.equals(fecha, other.fecha)
				&& Objects.equals(id, other.id) && Objects.equals(nombreComentador, other.nombreComentador)
				&& Objects.equals(publicacion, other.publicacion);
	}
}