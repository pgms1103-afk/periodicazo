package co.edu.unbosque.periodicazo.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import co.edu.unbosque.periodicazo.entity.Comentario;
import co.edu.unbosque.periodicazo.entity.Publicacion;

/**
 * Repositorio JPA para la entidad {@link Comentario}.
 * <p>
 * Extiende {@link JpaRepository} para heredar las operaciones CRUD básicas
 * (guardar, buscar, actualizar, eliminar) sobre la tabla {@code comentario}
 * en la base de datos. Incluye además una consulta personalizada para
 * filtrar comentarios por publicación.
 * </p>
 */
public interface ComentarioRepository extends JpaRepository<Comentario, Long> {

	/**
	 * Busca todos los comentarios asociados a una publicación específica.
	 *
	 * @param publicacion entidad {@link Publicacion} por la cual filtrar los comentarios
	 * @return lista de {@link Comentario} que pertenecen a la publicación indicada,
	 *         o una lista vacía si no existen comentarios para esa publicación
	 */
	List<Comentario> findByPublicacion(Publicacion publicacion);
}