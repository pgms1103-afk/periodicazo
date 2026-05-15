package co.edu.unbosque.periodicazo.dto;

import java.time.LocalDateTime;
import java.util.Objects;

public class ComentarioDTO {
	
	private Long id;
	private String contenido;
	private String nombreComentador;
	private LocalDateTime fecha;
	private Long publicacionId;
	private String nombrePublicacion;
	
	public ComentarioDTO() {
		// TODO Auto-generated constructor stub
	}

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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getContenido() {
		return contenido;
	}

	public void setContenido(String contenido) {
		this.contenido = contenido;
	}

	public String getNombreComentador() {
		return nombreComentador;
	}

	public void setNombreComentador(String nombreComentador) {
		this.nombreComentador = nombreComentador;
	}

	public LocalDateTime getFecha() {
		return fecha;
	}

	public void setFecha(LocalDateTime fecha) {
		this.fecha = fecha;
	}

	public Long getPublicacionId() {
		return publicacionId;
	}

	public void setPublicacionId(Long publicacionId) {
		this.publicacionId = publicacionId;
	}

	public String getNombrePublicacion() {
		return nombrePublicacion;
	}

	public void setNombrePublicacion(String nombrePublicacion) {
		this.nombrePublicacion = nombrePublicacion;
	}

	@Override
	public String toString() {
		return "ComentarioDTO [id=" + id + ", contenido=" + contenido + ", nombreComentador=" + nombreComentador
				+ ", fecha=" + fecha + ", publicacionId=" + publicacionId + ", nombrePublicacion=" + nombrePublicacion
				+ "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(contenido, fecha, id, nombreComentador, nombrePublicacion, publicacionId);
	}

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
