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

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final JwtUtil jwtUtil;
	private final UserDetailsService userDetailsService;

	public JwtAuthenticationFilter(JwtUtil jwtUtil,
			@Qualifier("userDetailsServiceImpl") UserDetailsService userDetailsService) {
		this.jwtUtil = jwtUtil;
		this.userDetailsService = userDetailsService;
	}

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
				// NUEVO: Intentamos cargar el usuario de la BD de forma segura
				UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
				
				if (jwtUtil.validateToken(jwt, userDetails)) { 
					UsernamePasswordAuthenticationToken authenticationToken =
							new UsernamePasswordAuthenticationToken(
									userDetails, null, userDetails.getAuthorities()); 
					authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					SecurityContextHolder.getContext().setAuthentication(authenticationToken);
				}
			} catch (UsernameNotFoundException ex) {
				// SOLUCIÓN: Si el usuario del token ya fue borrado de la BD, 
				// capturamos el error para que el servidor no se estrelle.
				// El sistema simplemente ignora el token viejo y trata la petición como "pública".
				logger.warn("Se ignoró un token porque el usuario ya no existe en los registros.");
			}
		}
		filterChain.doFilter(request, response);
	}
}