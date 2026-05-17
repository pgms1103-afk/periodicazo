package co.edu.unbosque.periodicazo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import co.edu.unbosque.periodicazo.dto.UsuarioDTO;
import co.edu.unbosque.periodicazo.entity.Usuario;
import co.edu.unbosque.periodicazo.exception.ContrasenaInvalidaException;
import co.edu.unbosque.periodicazo.security.JwtUtil;
import co.edu.unbosque.periodicazo.service.UsuarioService;

/**
 * Controlador REST para endpoints públicos de la aplicación.
 * <p>
 * Expone endpoints bajo la ruta {@code /public} que no requieren autenticación,
 * permitiendo a cualquier usuario registrarse en el sistema e iniciar sesión
 * para obtener un token JWT con el que acceder a los recursos protegidos.
 * </p>
 */
@RestController
@RequestMapping("/public")
@CrossOrigin(origins = { "http://localhost:8080", "*" })
public class PublicController {

	/**
	 * Servicio de usuarios inyectado para ejecutar la lógica de negocio.
	 */
	@Autowired
	private UsuarioService usuarioSer;

	/**
	 * Utilidad JWT inyectada para la generación de tokens de autenticación.
	 */
	@Autowired
	private JwtUtil jwtUtil;

	/**
	 * Gestor de autenticación inyectado para validar las credenciales del usuario.
	 */
	@Autowired
	private AuthenticationManager authenticationManager;

	/**
	 * Registra un nuevo usuario en el sistema con rol {@code USUARIO} por defecto.
	 * <p>
	 * Verifica primero si el nombre de usuario ya está en uso antes de proceder con
	 * el registro.
	 * </p>
	 *
	 * @param nombreUsuario nombre de usuario deseado para el nuevo registro
	 * @param contrasena    contraseña del nuevo usuario en texto plano, será
	 *                      encriptada antes de almacenarse
	 * @return {@code 201 Created} si el usuario fue registrado exitosamente,
	 *         {@code 409 Conflict} si el nombre de usuario ya está en uso,
	 *         {@code 400 Bad Request} si ocurrió un error durante el registro
	 */
	@PostMapping("/registrarusuario")
	public ResponseEntity<String> registrarUsuario(@RequestBody UsuarioDTO dto) {
		try {
		if (usuarioSer.findUsernameAlreadyTaken(dto.getUsername())) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("El nombre de usuario ya existe");
		}
		
			UsuarioDTO nuevo = new UsuarioDTO();
			nuevo.setUsername(dto.getUsername());
			nuevo.setPassword(dto.getPassword());
			int status = usuarioSer.create(nuevo);
			if (status == 0) {
				return new ResponseEntity<>("Usuario registrado con éxito", HttpStatus.CREATED);
			} else {
				return new ResponseEntity<>("Error al registrarse", HttpStatus.BAD_REQUEST);
			}
		} catch (ContrasenaInvalidaException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * Autentica un usuario y devuelve un token JWT junto con su rol.
	 * <p>
	 * Valida las credenciales del usuario contra la base de datos. Si son
	 * correctas, genera un token JWT que deberá enviarse en el header
	 * {@code Authorization} como {@code Bearer <token>} para acceder a los
	 * endpoints protegidos.
	 * </p>
	 *
	 * @param usuario    nombre de usuario registrado en el sistema
	 * @param contrasena contraseña del usuario en texto plano
	 * @return {@code 200 OK} con un objeto {@link AuthResponse} que contiene el
	 *         token JWT y el rol del usuario si las credenciales son válidas,
	 *         {@code 401 Unauthorized} si las credenciales son incorrectas o el
	 *         usuario no existe
	 */
	@PostMapping("/login")
	public ResponseEntity<Object> login(@RequestBody UsuarioDTO dto) {
		try {
			Authentication authentication = authenticationManager
					.authenticate(new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword()));

			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			String jwt = jwtUtil.generateToken(userDetails);

			String role = null;
			if (userDetails instanceof Usuario) {
				Usuario user = (Usuario) userDetails;
				role = user.getRole().name();
			}

			return ResponseEntity.ok(new AuthResponse(jwt, role));
		} catch (AuthenticationException e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body("Nombre de usuario o contraseña inválidos o usuario no encontrado");
		}
	}

	/**
	 * Clase interna que representa la respuesta de autenticación enviada al
	 * cliente.
	 * <p>
	 * Contiene el token JWT generado y el rol del usuario autenticado, permitiendo
	 * al frontend saber qué permisos tiene el usuario sin necesidad de decodificar
	 * el token.
	 * </p>
	 */
	private static class AuthResponse {

		/** Token JWT generado para el usuario autenticado. */
		private final String token;

		/** Rol del usuario autenticado. */
		private final String role;

		/**
		 * Constructor con solo token.
		 *
		 * @param token token JWT generado
		 */
		public AuthResponse(String token) {
			this.token = token;
			this.role = null;
		}

		/**
		 * Constructor con token y rol.
		 *
		 * @param token token JWT generado para el usuario autenticado
		 * @param role  rol del usuario autenticado (ej. ADMIN, EDITOR, USUARIO)
		 */
		public AuthResponse(String token, String role) {
			this.token = token;
			this.role = role;
		}

		/**
		 * Obtiene el token JWT generado durante la autenticación.
		 *
		 * @return token JWT como cadena de texto
		 */
		public String getToken() {
			return token;
		}

		/**
		 * Obtiene el rol del usuario autenticado.
		 *
		 * @return nombre del rol del usuario (ej. ADMIN, EDITOR, USUARIO)
		 */
		public String getRole() {
			return role;
		}
	}
}