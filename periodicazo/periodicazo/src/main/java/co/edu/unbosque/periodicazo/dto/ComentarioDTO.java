package co.edu.unbosque.periodicazo.dto;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Objeto de transferencia de datos (DTO) para la entidad Comentario.
 * <p>
 * Se utiliza para transportar la información de los comentarios entre
 * las capas de la aplicación (controller, service) sin exponer directamente
 * la entidad JPA. Incluye información resumida de la publicación asociada
 * para facilitar la presentación en el frontend.
 * </p>
 */
public class ComentarioDTO {

	/** Identificador único del comentario. */
	private Long id;

	/** Contenido textual del comentario. */
	private String contenido;

	/** Nombre del usuario que realizó el comentario. */
	private String nombreComentador;

	/** Fecha y hora en que fue creado el comentario. */
	private LocalDateTime fecha;

	/** Identificador de la publicación a la que pertenece el comentario. */
	private Long publicacionId;

	/** Título de la publicación a la que pertenece el comentario. */
	private String nombrePublicacion;

	/**
	 * Constructor vacío requerido para la deserialización JSON y
	 * el uso con frameworks como ModelMapper.
	 */
	public ComentarioDTO() {
	}

	/**
	 * Constructor completo con todos los campos del DTO.
	 *
	 * @param id               identificador único del comentario
	 * @param contenido        contenido textual del comentario
	 * @param nombreComentador nombre del usuario que realizó el comentario
	 * @param fecha            fecha y hora de creación del comentario
	 * @param publicacionId    identificador de la publicación asociada
	 * @param nombrePublicacion título de la publicación asociada
	 */
	public ComentarioDTO(Long id, String contenido, String nombreComentador, LocalDateTime fecha, Long publicacionId,
			String nombrePublicacion) {
		super();
		this.id = id;
		this.contenido = contenido;
		this.nombreComentador = nombreComentador;
		this.fecha = fecha;
		this.publicacionId = publicacionId;
		this.nombrePublicacion = nombrePublicacion;
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
	public void setNombreComentador(String nombreComentador) {
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
	 * Obtiene el identificador de la publicación asociada al comentario.
	 *
	 * @return identificador de la publicación
	 */
	public Long getPublicacionId() {
		return publicacionId;
	}

	/**
	 * Establece el identificador de la publicación asociada al comentario.
	 *
	 * @param publicacionId identificador de la publicación
	 */
	public void setPublicacionId(Long publicacionId) {
		this.publicacionId = publicacionId;
	}

	/**
	 * Obtiene el título de la publicación asociada al comentario.
	 *
	 * @return título de la publicación
	 */
	public String getNombrePublicacion() {
		return nombrePublicacion;
	}

	/**
	 * Establece el título de la publicación asociada al comentario.
	 *
	 * @param nombrePublicacion título de la publicación
	 */
	public void setNombrePublicacion(String nombrePublicacion) {
		this.nombrePublicacion = nombrePublicacion;
	}

	/**
	 * Retorna una representación en cadena del objeto {@code ComentarioDTO}.
	 *
	 * @return cadena con todos los campos del DTO
	 */
	@Override
	public String toString() {
		return "ComentarioDTO [id=" + id + ", contenido=" + contenido + ", nombreComentador=" + nombreComentador
				+ ", fecha=" + fecha + ", publicacionId=" + publicacionId + ", nombrePublicacion=" + nombrePublicacion
				+ "]";
	}

	/**
	 * Calcula el código hash del objeto basándose en todos sus campos.
	 *
	 * @return código hash del objeto
	 */
	@Override
	public int hashCode() {
		return Objects.hash(contenido, fecha, id, nombreComentador, nombrePublicacion, publicacionId);
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
		ComentarioDTO other = (ComentarioDTO) obj;
		return Objects.equals(contenido, other.contenido) && Objects.equals(fecha, other.fecha)
				&& Objects.equals(id, other.id) && Objects.equals(nombreComentador, other.nombreComentador)
				&& Objects.equals(nombrePublicacion, other.nombrePublicacion)
				&& Objects.equals(publicacionId, other.publicacionId);
	}
}