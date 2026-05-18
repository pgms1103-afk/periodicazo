package co.edu.unbosque.periodicazo.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import co.edu.unbosque.periodicazo.dto.ComentarioDTO;
import co.edu.unbosque.periodicazo.service.ComentarioService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.web.bind.annotation.PutMapping;

/**
 * Controlador REST para la gestión de comentarios del periódico.
 * <p>
 * Expone endpoints bajo la ruta {@code /private/comentario} para crear,
 * listar, buscar, actualizar y eliminar comentarios asociados a publicaciones.
 * Todos los endpoints requieren autenticación mediante token JWT.
 * </p>
 */
@RestController
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/private/comentario")
@CrossOrigin(origins = { "http://localhost:8080", "*" })
public class ComentarioController {

	/**
	 * Servicio de comentarios inyectado para ejecutar la lógica de negocio.
	 */
	@Autowired
	private ComentarioService comentarioSer;

	/**
	 * Crea un nuevo comentario asociado a una publicación existente.
	 * <p>
	 * El comentario queda vinculado a la publicación indicada en el DTO.
	 * Si la publicación no existe, la operación es rechazada.
	 * </p>
	 *
	 * @param dto objeto {@link ComentarioDTO} con los datos del comentario a crear,
	 *            incluyendo el contenido y el ID de la publicación asociada
	 * @return {@code 200 OK} si el comentario fue creado exitosamente,
	 *         {@code 404 Not Found} si la publicación asociada no existe
	 */
	@PostMapping("/crearcomentario")
	public ResponseEntity<String> crearComentario(@RequestBody ComentarioDTO dto) {
		int status = comentarioSer.create(dto);
		if (status == 0) {
			return new ResponseEntity<>("Comentario creado con éxito", HttpStatus.OK);
		} else {
			return new ResponseEntity<>("Error, publicacion no encontrada", HttpStatus.NOT_FOUND);
		}
	}

	/**
	 * Obtiene la lista de todos los comentarios registrados en el sistema.
	 *
	 * @return {@code 200 OK} con la lista de {@link ComentarioDTO} si hay
	 *         comentarios registrados,
	 *         {@code 204 No Content} si no existe ningún comentario en el sistema
	 */
	@GetMapping("/mostrarcomentarios")
	public ResponseEntity<List<ComentarioDTO>> mostrarComentarios() {
		List<ComentarioDTO> lista = comentarioSer.getAll();
		if (lista.isEmpty()) {
			return new ResponseEntity<>(lista, HttpStatus.NO_CONTENT);
		} else {
			return new ResponseEntity<>(lista, HttpStatus.OK);
		}
	}

	/**
	 * Obtiene los comentarios asociados a una publicación filtrados por su título.
	 * <p>
	 * Permite consultar todos los comentarios que pertenecen a una publicación
	 * específica sin necesidad de conocer su ID.
	 * </p>
	 *
	 * @param titulo título exacto de la publicación cuyos comentarios se desean consultar
	 * @return {@code 200 OK} con la lista de {@link ComentarioDTO} que coinciden
	 *         con el título proporcionado,
	 *         {@code 204 No Content} si no se encontraron comentarios para ese título
	 */
	@GetMapping("mostrarportitulo")
	public ResponseEntity<List<ComentarioDTO>> mostrarPorTitulo(@RequestParam String titulo) {
		List<ComentarioDTO> encontrados = comentarioSer.findByTitulo(titulo);
		if (encontrados.isEmpty()) {
			return new ResponseEntity<>(encontrados, HttpStatus.NO_CONTENT);
		} else {
			return new ResponseEntity<>(encontrados, HttpStatus.OK);
		}
	}

	/**
	 * Actualiza el contenido de un comentario existente identificado por su ID.
	 * <p>
	 * Solo se actualizan los campos proporcionados en el DTO. Si el comentario
	 * no existe, la operación no tiene efecto.
	 * </p>
	 *
	 * @param id  identificador único del comentario a actualizar
	 * @param dto objeto {@link ComentarioDTO} con los nuevos datos del comentario
	 * @return {@code 200 OK} si el comentario fue actualizado exitosamente,
	 *         {@code 204 No Content} si no existe un comentario con el ID proporcionado
	 */
	@PutMapping("/actualizarcomentario")
	public ResponseEntity<String> putMethodName(@RequestParam Long id, @RequestBody ComentarioDTO dto) {
		int status = comentarioSer.updateByID(id, dto);
		if (status == 0) {
			return new ResponseEntity<>("Comentario actualizado con éxito", HttpStatus.OK);
		} else {
			return new ResponseEntity<>("Comentario no existe", HttpStatus.NO_CONTENT);
		}
	}

	/**
	 * Elimina permanentemente un comentario del sistema identificado por su ID.
	 * <p>
	 * Esta operación es irreversible. Una vez eliminado, el comentario no puede
	 * ser recuperado.
	 * </p>
	 *
	 * @param id identificador único del comentario a eliminar
	 * @return {@code 200 OK} si el comentario fue eliminado exitosamente,
	 *         {@code 204 No Content} si no existe un comentario con el ID proporcionado
	 */
	@DeleteMapping("/eliminarcomentario")
	public ResponseEntity<String> eliminarComentario(@RequestParam Long id) {
		int status = comentarioSer.deleteByID(id);
		if (status == 0) {
			return new ResponseEntity<>("Comentario eliminado con éxito", HttpStatus.OK);
		} else {
			return new ResponseEntity<>("Comentario no existe", HttpStatus.NO_CONTENT);
		}
	}
}