package co.edu.unbosque.periodicazo.entity;

import java.time.LocalDateTime;
import java.util.Objects;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Comentario {
	
	private @Id @GeneratedValue(strategy = GenerationType.IDENTITY) Long id;
	private String contenido;
	private String nombreComentador;
	private LocalDateTime fecha;
	@ManyToOne
	@JoinColumn(name = "publicacion_id")
	private Publicacion publicacion;
	
	public Comentario() {
		this.fecha = LocalDateTime.now();
	}

	public Comentario(Long id, String contenido, String nombreComentador, LocalDateTime fecha, Publicacion publicacion) {
		super();
		this.id = id;
		this.contenido = contenido;
		this.nombreComentador = nombreComentador;
		this.fecha = fecha;
		this.publicacion = publicacion;
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

	public void setnombreComentador(String nombreComentador) {
		this.nombreComentador = nombreComentador;
	}

	public LocalDateTime getFecha() {
		return fecha;
	}

	public void setFecha(LocalDateTime fecha) {
		this.fecha = fecha;
	}

	public Publicacion getPublicacion() {
		return publicacion;
	}

	public void setPublicacion(Publicacion publicacion) {
		this.publicacion = publicacion;
	}

	@Override
	public String toString() {
		return "Comentario [id=" + id + ", contenido=" + contenido + ", nombre=" + nombreComentador + ", fecha=" + fecha
				+ ", publicacion=" + publicacion + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(contenido, fecha, id, nombreComentador, publicacion);
	}

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
