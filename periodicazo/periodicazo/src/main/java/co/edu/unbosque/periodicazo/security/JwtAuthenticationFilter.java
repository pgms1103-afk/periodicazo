package co.edu.unbosque.periodicazo.security;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import io.jsonwebtoken.io.IOException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Filtro de autenticación JWT que intercepta cada petición HTTP entrante.
 * <p>
 * Extiende {@link OncePerRequestFilter} para garantizar que la lógica de
 * autenticación se ejecute una sola vez por petición. Extrae el token JWT
 * del header {@code Authorization}, lo valida y, si es correcto, establece
 * la autenticación en el {@link SecurityContextHolder} para que Spring Security
 * reconozca al usuario durante el procesamiento de la petición.
 * </p>
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	/** Utilidad JWT para extracción y validación de tokens. */
	private final JwtUtil jwtUtil;

	/** Servicio para cargar los detalles del usuario desde la base de datos. */
	private final UserDetailsService userDetailsService;

	/**
	 * Constructor que inyecta las dependencias necesarias para el filtro.
	 *
	 * @param jwtUtil            utilidad JWT para operaciones sobre el token
	 * @param userDetailsService implementación de {@link UserDetailsService}
	 *                           para cargar usuarios desde la base de datos
	 */
	public JwtAuthenticationFilter(JwtUtil jwtUtil,
			@Qualifier("userDetailsServiceImpl") UserDetailsService userDetailsService) {
		this.jwtUtil = jwtUtil;
		this.userDetailsService = userDetailsService;
	}

	/**
	 * Lógica principal del filtro que se ejecuta en cada petición HTTP.
	 * <p>
	 * Sigue el siguiente flujo:
	 * <ol>
	 *   <li>Extrae el token JWT del header {@code Authorization} si comienza con {@code Bearer }.</li>
	 *   <li>Intenta extraer el nombre de usuario del token.</li>
	 *   <li>Si el usuario no está autenticado aún, carga sus detalles desde la base de datos.</li>
	 *   <li>Valida el token contra los detalles del usuario.</li>
	 *   <li>Si el token es válido, establece la autenticación en el contexto de seguridad.</li>
	 *   <li>Si el usuario ya no existe en la base de datos, ignora el token y continúa
	 *       la cadena de filtros tratando la petición como no autenticada.</li>
	 * </ol>
	 * </p>
	 *
	 * @param request     objeto {@link HttpServletRequest} con los datos de la petición entrante
	 * @param response    objeto {@link HttpServletResponse} con los datos de la respuesta saliente
	 * @param filterChain cadena de filtros que continúa el procesamiento de la petición
	 * @throws ServletException    si ocurre un error en el procesamiento del servlet
	 * @throws IOException         si ocurre un error de entrada/salida durante el filtrado
	 * @throws java.io.IOException si ocurre un error de entrada/salida estándar de Java
	 */
	@Override
	protected void doFilterInternal(
			HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException, java.io.IOException {

		final String authorizationHeader = request.getHeader("Authorization");
		String username = null;
		String jwt = null;

		if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
			jwt = authorizationHeader.substring(7);
			try {
				username = jwtUtil.extractUsername(jwt);
			} catch (Exception e) {
				logger.error("Error extrayendo el usuario del token", e);
			}
		}

		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			try {
				UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

				if (jwtUtil.validateToken(jwt, userDetails)) {
					UsernamePasswordAuthenticationToken authenticationToken =
							new UsernamePasswordAuthenticationToken(
									userDetails, null, userDetails.getAuthorities());
					authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					SecurityContextHolder.getContext().setAuthentication(authenticationToken);
				}
			} catch (UsernameNotFoundException ex) {
				
				logger.warn("Se ignoró un token porque el usuario ya no existe en los registros.");
			}
		}

		filterChain.doFilter(request, response);
	}
}