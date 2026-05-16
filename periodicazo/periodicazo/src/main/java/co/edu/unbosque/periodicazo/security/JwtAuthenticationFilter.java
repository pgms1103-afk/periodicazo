package co.edu.unbosque.periodicazo.security;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import io.jsonwebtoken.io.IOException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Filtro de autenticación JWT que se ejecuta una vez por cada solicitud HTTP.
 * <p>
 * Intercepta todas las peticiones entrantes, extrae el token JWT del header
 * {@code Authorization}, lo valida y, si es válido, establece la autenticación
 * del usuario en el {@link SecurityContextHolder} para que Spring Security
 * pueda aplicar las reglas de autorización correspondientes.
 * </p>
 * <p>
 * El flujo de autenticación es:
 * </p>
 * <ol>
 *   <li>Extrae el token JWT del header {@code Authorization: Bearer <token>}.</li>
 *   <li>Extrae el username del token.</li>
 *   <li>Carga los detalles del usuario desde la base de datos.</li>
 *   <li>Valida el token contra los detalles del usuario.</li>
 *   <li>Si es válido, establece la autenticación en el contexto de seguridad.</li>
 * </ol>
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	/** Utilidad para operaciones con tokens JWT. */
	private final JwtUtil jwtUtil;

	/** Servicio para cargar los detalles del usuario desde la base de datos. */
	private final UserDetailsService userDetailsService;

	/**
	 * Constructor que inyecta las dependencias necesarias para el filtro.
	 *
	 * @param jwtUtil            utilidad para operaciones con tokens JWT
	 * @param userDetailsService servicio para cargar los detalles del usuario,
	 *                           calificado con {@code userDetailsServiceImpl}
	 */
	public JwtAuthenticationFilter(JwtUtil jwtUtil,
			@Qualifier("userDetailsServiceImpl") UserDetailsService userDetailsService) {
		this.jwtUtil = jwtUtil;
		this.userDetailsService = userDetailsService;
	}

	/**
	 * Método principal del filtro que se ejecuta una vez por cada solicitud HTTP.
	 * <p>
	 * Extrae y valida el token JWT del header {@code Authorization}. Si el token
	 * es válido, carga los detalles del usuario y establece la autenticación en
	 * el contexto de seguridad de Spring para que los endpoints protegidos puedan
	 * verificar los permisos del usuario autenticado.
	 * </p>
	 *
	 * @param request     solicitud HTTP entrante
	 * @param response    respuesta HTTP saliente
	 * @param filterChain cadena de filtros para continuar el procesamiento de la solicitud
	 * @throws ServletException      si ocurre un error durante el procesamiento del servlet
	 * @throws IOException           si ocurre un error de entrada/salida de jsonwebtoken
	 * @throws java.io.IOException   si ocurre un error de entrada/salida estándar de Java
	 */
	@Override
	protected void doFilterInternal(
			HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException, java.io.IOException {

		final String authorizationHeader = request.getHeader("Authorization");
		String username = null;
		String jwt = null;

		if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
			jwt = authorizationHeader.substring(7); // verifica si es real
			try {
				username = jwtUtil.extractUsername(jwt); // Extraiga el username, verifica que sea valido y lo guarda
			} catch (Exception e) {
				logger.error("Error extracting username from token", e);
			}
		}

		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) { // Traiga todos los detalles de esa persona
			UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
			if (jwtUtil.validateToken(jwt, userDetails)) { // Coje todo el JWT y verifica contraseña, rol, y userName.
				UsernamePasswordAuthenticationToken authenticationToken =
						new UsernamePasswordAuthenticationToken(
								userDetails, null, userDetails.getAuthorities()); // authorities verifica que puede hacer el usuario verificado.
				authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authenticationToken);
			}
		}
		filterChain.doFilter(request, response);
	}
}