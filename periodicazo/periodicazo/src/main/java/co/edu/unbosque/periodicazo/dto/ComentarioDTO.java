package co.edu.unbosque.periodicazo.dto;

import java.time.LocalDateTime;
import java.util.Objects;

public class ComentarioDTO {
	
	private Long id;
	private String contenido;
	private String nombre;
	private LocalDateTime fecha;
	private Long publicacionId;
	
	public ComentarioDTO() {
		// TODO Auto-generated constructor stub
	}

	public ComentarioDTO(Long id, String contenido, String nombre, LocalDateTime fecha, Long publicacionId) {
		super();
		this.id = id;
		this.contenido = contenido;
		this.nombre = nombre;
		this.fecha = fecha;
		this.publicacionId = publicacionId;
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

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
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

	@Override
	public String toString() {
		return "ComentarioDTO [id=" + id + ", contenido=" + contenido + ", nombre=" + nombre + ", fecha=" + fecha
				+ ", publicacionId=" + publicacionId + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(contenido, fecha, id, nombre, publicacionId);
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
				&& Objects.equals(id, other.id) && Objects.equals(nombre, other.nombre)
				&& Objects.equals(publicacionId, other.publicacionId);
	}

	

}
