package co.edu.unbosque.periodicazo.entity;

import java.time.LocalDateTime;
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

@Entity
public class Publicacion {
	
	private @Id @GeneratedValue(strategy = GenerationType.IDENTITY) Long id;
	
	@Enumerated(EnumType.STRING)
	private Tipo tipo;
	private String titulo;
	private String contenido;
	private String autor;
	private String editorial;
	private LocalDateTime fecha;
	@Enumerated(EnumType.STRING)
	private Categoria categoria;
	private String signo;
	private String elemento;
	@OneToMany(mappedBy = "publicacion", cascade = CascadeType.ALL)
	private List<Comentario> comentarios;
	public enum Categoria{
		 Política,
		    Economía,
		    Cultura,
		    Deportes,
		    Tecnología
	}
	
	public enum Tipo{
		NOTICIA,
		HOROSCOPO
	}
	
	public Publicacion() {
	   this.fecha = LocalDateTime.now();
	}

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
		this.comentarios = comentarios;
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

	public List<Comentario> getComentarios() {
		return comentarios;
	}

	public void setComentarios(List<Comentario> comentarios) {
		this.comentarios = comentarios;
	}

	@Override
	public String toString() {
		return "Publicacion [id=" + id + ", tipo=" + tipo + ", titulo=" + titulo + ", contenido=" + contenido
				+ ", autor=" + autor + ", editorial=" + editorial + ", fecha=" + fecha + ", categoria=" + categoria
				+ ", signo=" + signo + ", elemento=" + elemento + ", comentarios=" + comentarios + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(autor, categoria, comentarios, contenido, editorial, elemento, fecha, id, signo, tipo,
				titulo);
	}

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
