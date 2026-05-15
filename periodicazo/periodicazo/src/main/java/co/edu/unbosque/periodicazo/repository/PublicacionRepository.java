package co.edu.unbosque.periodicazo.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import co.edu.unbosque.periodicazo.entity.Publicacion;
import co.edu.unbosque.periodicazo.entity.Publicacion.Tipo;

/**
 * Repositorio JPA para la entidad {@link Publicacion}.
 * <p>
 * Extiende {@link JpaRepository} para heredar las operaciones CRUD básicas
 * (guardar, buscar, actualizar, eliminar) sobre la tabla {@code publicacion}
 * en la base de datos. Incluye consultas personalizadas para filtrar
 * publicaciones por tipo y por título.
 * </p>
 */
public interface PublicacionRepository extends JpaRepository<Publicacion, Long> {

	/**
	 * Busca todas las publicaciones que corresponden a un tipo específico.
	 *
	 * @param tipo tipo de publicación a buscar ({@link Tipo#NOTICIA} o {@link Tipo#HOROSCOPO})
	 * @return {@link Optional} con la lista de {@link Publicacion} del tipo indicado,
	 *         o un {@link Optional} vacío si no existen publicaciones de ese tipo
	 */
	public Optional<List<Publicacion>> findByTipo(Tipo tipo);

	/**
	 * Busca todas las publicaciones que coinciden exactamente con el título indicado.
	 *
	 * @param titulo título de la publicación a buscar
	 * @return {@link Optional} con la lista de {@link Publicacion} que coinciden con el título,
	 *         o un {@link Optional} vacío si no existen publicaciones con ese título
	 */
	public Optional<List<Publicacion>> findByTitulo(String titulo);
}