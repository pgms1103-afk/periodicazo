package co.edu.unbosque.periodicazo.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import co.edu.unbosque.periodicazo.repository.UsuarioRepository;

/**
 * Implementación de {@link UserDetailsService} para la autenticación con Spring Security.
 * <p>
 * Proporciona la funcionalidad necesaria para cargar los datos del usuario
 * desde la base de datos durante el proceso de autenticación JWT. Es utilizada
 * por {@link SecurityConfig} y {@link JwtAuthenticationFilter} para verificar
 * las credenciales del usuario en cada solicitud autenticada.
 * </p>
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	/**
	 * Repositorio de usuarios utilizado para buscar información de usuarios
	 * en la base de datos durante la autenticación.
	 */
	private final UsuarioRepository userRepository;

	/**
	 * Constructor que inyecta el repositorio de usuarios.
	 *
	 * @param userRepository repositorio de usuarios utilizado para las consultas
	 *                       de autenticación
	 */
	public UserDetailsServiceImpl(UsuarioRepository userRepository) {
		this.userRepository = userRepository;
	}

	/**
	 * Carga los detalles del usuario a partir de su nombre de usuario.
	 * <p>
	 * Es invocado automáticamente por Spring Security durante el proceso
	 * de autenticación para obtener los datos del usuario y verificar
	 * sus credenciales y permisos.
	 * </p>
	 *
	 * @param username nombre de usuario a buscar en la base de datos
	 * @return objeto {@link UserDetails} con los datos del usuario encontrado
	 * @throws UsernameNotFoundException si no existe ningún usuario registrado
	 *                                   con el nombre de usuario proporcionado
	 */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return userRepository
				.findByUsername(username)
				.orElseThrow(
						() -> new UsernameNotFoundException("User not found with username: " + username));
	}
}