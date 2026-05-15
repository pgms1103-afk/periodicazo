package co.edu.unbosque.periodicazo.dto;

import java.time.LocalDateTime;
import java.util.Objects;

import co.edu.unbosque.periodicazo.entity.Publicacion.Categoria;
import co.edu.unbosque.periodicazo.entity.Publicacion.Tipo;


public class PublicacionDTO {
	
private Long id;
	
	private Tipo tipo;
	private String titulo;
	private String contenido;
	private String autor;
	private String editorial;
	private LocalDateTime fecha;
	private Categoria categoria;
	private String signo;
	private String elemento;

	public PublicacionDTO() {
		this.fecha = LocalDateTime.now();
	}

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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Tipo getTipo() {
		return tipo;
	}

	public void setTipo(Tipo tipo) {
		this.tipo = tipo;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getContenido() {
		return contenido;
	}

	public void setContenido(String contenido) {
		this.contenido = contenido;
	}

	public String getAutor() {
		return autor;
	}

	public void setAutor(String autor) {
		this.autor = autor;
	}

	public String getEditorial() {
		return editorial;
	}

	public void setEditorial(String editorial) {
		this.editorial = editorial;
	}

	public LocalDateTime getFecha() {
		return fecha;
	}

	public void setFecha(LocalDateTime fecha) {
		this.fecha = fecha;
	}

	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}

	public String getSigno() {
		return signo;
	}

	public void setSigno(String signo) {
		this.signo = signo;
	}

	public String getElemento() {
		return elemento;
	}

	public void setElemento(String elemento) {
		this.elemento = elemento;
	}

	@Override
	public String toString() {
		return "PublicacionDTO [id=" + id + ", tipo=" + tipo + ", titulo=" + titulo + ", contenido=" + contenido
				+ ", autor=" + autor + ", editorial=" + editorial + ", fecha=" + fecha + ", categoria=" + categoria
				+ ", signo=" + signo + ", elemento=" + elemento + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(autor, categoria, contenido, editorial, elemento, fecha, id, signo, tipo, titulo);
	}

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
