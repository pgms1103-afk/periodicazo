package co.edu.unbosque.periodicazo.security;

//revisar
import org.springframework.security.config.Customizer;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.Arrays;
//revisar
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Clase de configuración principal de seguridad de la aplicación.
 * <p>
 * Define las reglas de autorización por rol para cada endpoint, configura
 * la autenticación sin estado (stateless) mediante JWT, y registra los
 * beans necesarios para el funcionamiento de Spring Security.
 * </p>
 * <p>
 * Las reglas de acceso configuradas son:
 * </p>
 * <ul>
 *   <li>{@code /public/**} — acceso libre sin autenticación (login y registro).</li>
 *   <li>{@code /swagger-ui/**}, {@code /v3/api-docs/**} — documentación accesible sin autenticación.</li>
 *   <li>{@code /private/publicacion/mostrarportipo} y endpoints de comentarios — accesible por {@code COMENTADOR} y {@code USUARIO}.</li>
 *   <li>{@code /private/publicacion/editarpublicacion} y listados — accesible por {@code EDITOR} y {@code ADMIN}.</li>
 *   <li>{@code /private/comentario/crearcomentario} y endpoints de comentarios — accesible por {@code COMENTADOR} y {@code ADMIN}.</li>
 *   <li>{@code /admin/**} y demás endpoints privados — accesible solo por {@code ADMIN}.</li>
 * </ul>
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

	/** Filtro de autenticación JWT que procesa los tokens en las solicitudes entrantes. */
	private final JwtAuthenticationFilter jwtAuthFilter;

	/** Servicio que carga los detalles del usuario desde la base de datos para la autenticación. */
	private final UserDetailsService userDetailsService;

	/**
	 * Constructor que inyecta los componentes necesarios para la configuración de seguridad.
	 *
	 * @param jwtAuthFilter      filtro para interceptar y procesar tokens JWT en cada solicitud
	 * @param userDetailsService servicio para cargar los detalles del usuario durante la autenticación,
	 *                           calificado con {@code userDetailsServiceImpl}
	 */
	public SecurityConfig(JwtAuthenticationFilter jwtAuthFilter,
			@Qualifier("userDetailsServiceImpl") UserDetailsService userDetailsService) {
		this.jwtAuthFilter = jwtAuthFilter;
		this.userDetailsService = userDetailsService;
	}

	/**
	 * Configura la cadena de filtros de seguridad HTTP.
	 * <p>
	 * Deshabilita CSRF (no necesario en APIs REST stateless), define las reglas
	 * de autorización por rol para cada grupo de endpoints, configura la política
	 * de sesión como {@link SessionCreationPolicy#STATELESS} (sin sesión del lado
	 * del servidor) y agrega el filtro JWT antes del filtro de autenticación
	 * estándar de Spring Security.
	 * </p>
	 *
	 * @param http objeto de configuración de seguridad HTTP proporcionado por Spring
	 * @return cadena de filtros de seguridad construida y configurada
	 * @throws Exception si ocurre un error durante la configuración de seguridad
	 */
	//revisar
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
			.cors(Customizer.withDefaults()) // Habilitamos CORS en la barrera
			.csrf(csrf -> csrf.disable())
			.authorizeHttpRequests(auth -> auth

				// 1. Endpoints totalmente abiertos al público
				.requestMatchers("/public/**", "/error") 
				.permitAll()
				.requestMatchers("/swagger-ui/**", "/v3/api-docs/**")
				.permitAll()

				// 2. Visualización de Publicaciones (La ven todos los roles del diario)
				.requestMatchers("/private/publicacion/mostrarportipo")
				.hasAnyRole("USUARIO", "COMENTADOR", "EDITOR", "ADMIN")

				// 3. Visualización de Comentarios (Lectores, comentadores y administrador)
				.requestMatchers("/private/comentario/mostrarportitulo",
						"/private/comentario/mostrarcomentarios")
				.hasAnyRole("USUARIO", "COMENTADOR", "ADMIN")

				// 4. Edición y creación de Columnas (Editores y Administrador)
				// NOTA: Se corrige a /editarPublicacion con P mayúscula para coincidir con tu Controller
				.requestMatchers("/private/publicacion/editarPublicacion",
						"/private/publicacion/mostrartodo")
				.hasAnyRole("EDITOR", "ADMIN")

				// 5. Escritura y Modificación de Comentarios (Comentadores y Administrador)
				// NOTA: Se corrige el prefijo de /actualizarcomentario a la sección de comentario
				.requestMatchers("/private/comentario/crearcomentario",
						"/private/comentario/actualizarcomentario")
				.hasAnyRole("COMENTADOR", "ADMIN")

				// 6. Protección global para el resto de la administración
				.requestMatchers("/admin/**", "/private/publicacion/**", "/private/comentario/**")
				.hasRole("ADMIN")

				.anyRequest().authenticated()
			)
			.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.authenticationProvider(authenticationProvider())
			.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

	//revisar
	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		// Permitir explícitamente el puerto de Angular
		configuration.setAllowedOrigins(Arrays.asList("http://localhost:4200"));
		// Permitir todos los métodos, incluido OPTIONS (El explorador invisible)
		configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
		// Permitir las cabeceras personalizadas como Authorization
		configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
		// Permitir credenciales de sesión/tokens
		configuration.setAllowCredentials(true);
		
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		// Aplicar esta regla a absolutamente todas las rutas de la API
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}

	/**
	 * Configura el proveedor de autenticación basado en DAO.
	 * <p>
	 * Utiliza {@link DaoAuthenticationProvider} que consulta la base de datos
	 * a través del {@link UserDetailsService} y verifica las contraseñas
	 * usando el {@link PasswordEncoder} configurado con BCrypt.
	 * </p>
	 *
	 * @return proveedor de autenticación configurado con el servicio de usuarios
	 *         y el codificador de contraseñas
	 */
	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userDetailsService);
		authProvider.setPasswordEncoder(passwordEncoder());
		return authProvider;
	}

	/**
	 * Expone el {@link AuthenticationManager} como bean de Spring para ser
	 * inyectado en los controllers que necesiten autenticar usuarios manualmente,
	 * como el {@code PublicController} en el endpoint de login.
	 *
	 * @param config configuración de autenticación proporcionada por Spring
	 * @return gestor de autenticación obtenido de la configuración
	 * @throws Exception si ocurre un error al obtener el gestor de autenticación
	 */
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}

	/**
	 * Configura el codificador de contraseñas de la aplicación.
	 * <p>
	 * Utiliza {@link BCryptPasswordEncoder} para generar hashes seguros de las
	 * contraseñas antes de almacenarlas en la base de datos y para verificarlas
	 * durante el proceso de autenticación.
	 * </p>
	 *
	 * @return instancia de {@link BCryptPasswordEncoder} como codificador de contraseñas
	 */
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}