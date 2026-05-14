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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import co.edu.unbosque.periodicazo.dto.UsuarioDTO;
import co.edu.unbosque.periodicazo.entity.Usuario;
import co.edu.unbosque.periodicazo.security.JwtUtil;
import co.edu.unbosque.periodicazo.service.UsuarioService;


@RestController
@RequestMapping("/public")
@CrossOrigin(origins = { "http://localhost:8080", "*" })
public class PublicController {
	
	@Autowired
	private UsuarioService usuarioSer;
	
	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private AuthenticationManager authenticationManager;
	
	@PostMapping("/registrarusuario")
	public ResponseEntity<String> registrarUsuario(@RequestParam String nombreUsuario, @RequestParam String contrasena) {
		 if (usuarioSer.findUsernameAlreadyTaken(nombreUsuario)) {
		      return ResponseEntity.status(HttpStatus.CONFLICT).body("El nombre de usuario ya existe");
		    }
		 
		UsuarioDTO nuevo = new UsuarioDTO ();
		nuevo.setUsername(nombreUsuario);
		nuevo.setPassword(contrasena);
		int status = usuarioSer.create(nuevo);
		if (status == 0) {
			return new ResponseEntity<>("Usuario registrado con éxito", HttpStatus.CREATED);
		}else {
			return new ResponseEntity<>("Error al registrarse", HttpStatus.BAD_REQUEST);
		}	
	}
	
	@PostMapping("/login")
	public ResponseEntity<Object> login(@RequestParam String usuario, @RequestParam String contrasena) {
		 try {
		      Authentication authentication =
		          authenticationManager.authenticate(
		              new UsernamePasswordAuthenticationToken(
		            		  usuario, contrasena));

		      UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		      String jwt = jwtUtil.generateToken(userDetails);

		      String role = null;
		      if (userDetails instanceof Usuario) {
		    	  Usuario user = (Usuario) userDetails;
		        role = user.getRole().name();
		      }

		      return ResponseEntity.ok(new AuthResponse(jwt, role));
		    } catch (AuthenticationException e) {
		      e.printStackTrace();
		      return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
		          .body("Nombre de usuario o contraseña inválidos o usuario no encontrado");
		    }
	}
	
	 private static class AuthResponse {
		    /** Token JWT generado para el usuario autenticado. */
		    private final String token;

		    /** Rol del usuario autenticado. */
		    private final String role;

		    /**
		     * Constructor con solo token.
		     *
		     * @param token Token JWT generado
		     */
		    public AuthResponse(String token) {
		      this.token = token;
		      // Extraer rol del token
		      this.role = null; // Se establecerá en el constructor con el parámetro de rol
		    }

		    /**
		     * Constructor con token y rol.
		     *
		     * @param token Token JWT generado
		     * @param role Rol del usuario
		     */
		    public AuthResponse(String token, String role) {
		      this.token = token;
		      this.role = role;
		    }

		    /**
		     * Obtiene el token JWT.
		     *
		     * @return Token JWT
		     */
		    public String getToken() {
		      return token;
		    }

		    /**
		     * Obtiene el rol del usuario.
		     *
		     * @return Rol del usuario
		     */
		    public String getRole() {
		      return role;
		    }
		  }
	 
}
