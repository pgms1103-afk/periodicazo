package co.edu.unbosque.periodicazo.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import co.edu.unbosque.periodicazo.entity.Usuario;

/**
 * Repositorio JPA para la entidad {@link Usuario}.
 * <p>
 * Extiende {@link JpaRepository} para heredar las operaciones CRUD básicas
 * (guardar, buscar, actualizar, eliminar) sobre la tabla {@code usuarios}
 * en la base de datos. Incluye una consulta personalizada para buscar
 * usuarios por nombre de usuario, utilizada principalmente durante
 * el proceso de autenticación con JWT.
 * </p>
 */
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

	/**
	 * Busca un usuario por su nombre de usuario único.
	 * <p>
	 * Es utilizado por {@code UserDetailsServiceImpl} durante la autenticación
	 * para cargar los datos del usuario desde la base de datos a partir
	 * del username proporcionado en el login.
	 * </p>
	 *
	 * @param username nombre de usuario a buscar
	 * @return {@link Optional} con el {@link Usuario} encontrado,
	 *         o un {@link Optional} vacío si no existe un usuario con ese username
	 */
	public Optional<Usuario> findByUsername(String username);
}