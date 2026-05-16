package co.edu.unbosque.periodicazo.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import co.edu.unbosque.periodicazo.dto.UsuarioDTO;
import co.edu.unbosque.periodicazo.service.UsuarioService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

/**
 * Controlador REST para operaciones exclusivas del administrador.
 * <p>
 * Expone endpoints bajo la ruta {@code /admin} para la gestión completa
 * de usuarios del sistema. Todos los endpoints requieren autenticación
 * mediante token JWT con rol {@code ADMIN}.
 * </p>
 */
@RestController
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/admin")
@CrossOrigin(origins = { "http://localhost:8080", "*" })
public class AdminController {

	/**
	 * Servicio de usuarios inyectado para ejecutar la lógica de negocio.
	 */
	@Autowired
	private UsuarioService userSer;

	/**
	 * Crea un nuevo usuario en el sistema.
	 *
	 * @param dto objeto {@link UsuarioDTO} con los datos del usuario a crear,
	 *            incluyendo username, password y rol
	 * @return {@code 201 Created} si el usuario fue creado exitosamente,
	 *         {@code 409 Conflict} si ya existe un usuario con ese username,
	 *         {@code 500 Internal Server Error} si ocurrió un error inesperado
	 */
	@PostMapping("/crearusuario")
	public ResponseEntity<String> crearUsuario(@RequestBody UsuarioDTO dto) {
		int status = userSer.create(dto);
		if (status == 0) {
			return new ResponseEntity<>("Usuario creado con éxito", HttpStatus.CREATED);
		} else if (status == 1) {
			return new ResponseEntity<>("Ya existe un usuario con ese username", HttpStatus.CONFLICT);
		} else {
			return new ResponseEntity<>("Error inesperado al crear el usuario", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * Obtiene la lista de todos los usuarios registrados en el sistema.
	 *
	 * @return {@code 202 Accepted} con la lista de {@link UsuarioDTO} si hay usuarios,
	 *         {@code 204 No Content} si no existe ningún usuario registrado
	 */
	@GetMapping("/mostrarusuario")
	public ResponseEntity<List<UsuarioDTO>> mostrarUsuario() {
		List<UsuarioDTO> usuarios = userSer.getAll();
		if (usuarios.isEmpty()) {
			return new ResponseEntity<List<UsuarioDTO>>(usuarios, HttpStatus.NO_CONTENT);
		} else {
			return new ResponseEntity<List<UsuarioDTO>>(usuarios, HttpStatus.ACCEPTED);
		}
	}

	/**
	 * Actualiza los datos de un usuario existente identificado por su ID.
	 *
	 * @param id  identificador único del usuario a actualizar
	 * @param dto objeto {@link UsuarioDTO} con los nuevos datos del usuario
	 * @return {@code 200 OK} si el usuario fue actualizado exitosamente,
	 *         {@code 204 No Content} si no existe un usuario con el ID proporcionado
	 */
	@PutMapping("actualizarusuario")
	public ResponseEntity<String> actualizarUsuario(@RequestParam Long id, @RequestBody UsuarioDTO dto) {
		int status = userSer.updateByID(id, dto);
		if (status == 0) {
			return new ResponseEntity<>("Usuario actualizado con éxito", HttpStatus.OK);
		} else {
			return new ResponseEntity<>("Usuario no éxiste", HttpStatus.NO_CONTENT);
		}
	}

	/**
	 * Elimina un usuario del sistema identificado por su ID.
	 *
	 * @param id identificador único del usuario a eliminar
	 * @return {@code 200 OK} si el usuario fue eliminado exitosamente,
	 *         {@code 204 No Content} si no existe un usuario con el ID proporcionado
	 */
	@DeleteMapping("/eliminarusuario")
	public ResponseEntity<String> eliminarUsuario(@RequestParam Long id) {
		int status = userSer.deleteByID(id);
		if (status == 0) {
			return new ResponseEntity<>("Usuario eliminado con éxito", HttpStatus.OK);
		} else {
			return new ResponseEntity<>("Usuario no éxiste", HttpStatus.NO_CONTENT);
		}
	}
}