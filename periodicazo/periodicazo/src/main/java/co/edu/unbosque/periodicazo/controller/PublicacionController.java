package co.edu.unbosque.periodicazo.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import co.edu.unbosque.periodicazo.dto.PublicacionDTO;
import co.edu.unbosque.periodicazo.entity.Publicacion.Tipo;
import co.edu.unbosque.periodicazo.service.PublicacionService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Controlador REST para la gestión de publicaciones del periódico.
 * <p>
 * Expone endpoints bajo la ruta {@code /private/publicacion} para crear,
 * listar, filtrar, actualizar y eliminar publicaciones. Soporta dos tipos
 * de publicaciones: {@link Tipo#NOTICIA} y {@link Tipo#HOROSCOPO}.
 * Todos los endpoints requieren autenticación mediante token JWT.
 * </p>
 */
@RestController
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/private/publicacion")
@CrossOrigin(origins = { "http://localhost:8080", "*" })
public class PublicacionController {

	/**
	 * Servicio de publicaciones inyectado para ejecutar la lógica de negocio.
	 */
	@Autowired
	private PublicacionService publiSer;

	/**
	 * Crea una nueva publicación en el sistema.
	 * <p>
	 * Según el valor del campo {@code tipo} en el DTO, se procesarán los campos
	 * correspondientes a una noticia (título, contenido, autor, editorial, categoría)
	 * o a un horóscopo (título, contenido, signo, elemento).
	 * </p>
	 *
	 * @param dto objeto {@link PublicacionDTO} con los datos de la publicación a crear
	 * @return {@code 201 Created} si la publicación fue creada exitosamente,
	 *         {@code 500 Internal Server Error} si ocurrió un error inesperado
	 *         durante la creación
	 */
	@PostMapping("/crearpublicacion")
	public ResponseEntity<String> crearPublicacion(@RequestBody PublicacionDTO dto) {
		int status = publiSer.create(dto);
		if (status == 0) {
			return new ResponseEntity<>("Publicacion creado con éxito", HttpStatus.CREATED);
		} else {
			return new ResponseEntity<>("Error inesperado al crear el publicación", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * Obtiene la lista de publicaciones filtradas por tipo.
	 * <p>
	 * Permite consultar únicamente las noticias o únicamente los horóscopos
	 * según el tipo indicado.
	 * </p>
	 *
	 * @param tipo tipo de publicación a filtrar, puede ser {@link Tipo#NOTICIA}
	 *             o {@link Tipo#HOROSCOPO}
	 * @return {@code 200 OK} con la lista de {@link PublicacionDTO} que coinciden
	 *         con el tipo indicado,
	 *         {@code 204 No Content} si no existen publicaciones del tipo indicado
	 */
	@GetMapping("/mostrarportipo")
	public ResponseEntity<List<PublicacionDTO>> mostrarPorTipo(@RequestParam Tipo tipo) {
		List<PublicacionDTO> encontrados = publiSer.findByTipo(tipo);
		if (encontrados.isEmpty()) {
			return new ResponseEntity<List<PublicacionDTO>>(encontrados, HttpStatus.NO_CONTENT);
		} else {
			return new ResponseEntity<List<PublicacionDTO>>(encontrados, HttpStatus.OK);
		}
	}

	/**
	 * Obtiene la lista de todas las publicaciones registradas en el sistema,
	 * sin importar su tipo.
	 *
	 * @return {@code 200 OK} con la lista completa de {@link PublicacionDTO},
	 *         {@code 204 No Content} si no existe ninguna publicación registrada
	 */
	@GetMapping("/mostrartodo")
	public ResponseEntity<List<PublicacionDTO>> mostrarTodo() {
		List<PublicacionDTO> encontrados = publiSer.getAll();
		if (encontrados.isEmpty()) {
			return new ResponseEntity<List<PublicacionDTO>>(encontrados, HttpStatus.NO_CONTENT);
		} else {
			return new ResponseEntity<List<PublicacionDTO>>(encontrados, HttpStatus.OK);
		}
	}

	/**
	 * Actualiza los datos de una publicación existente identificada por su ID.
	 * <p>
	 * Solo se actualizan los campos que lleguen con valor; los campos nulos
	 * conservan su valor original en la base de datos.
	 * </p>
	 *
	 * @param id  identificador único de la publicación a actualizar
	 * @param dto objeto {@link PublicacionDTO} con los nuevos datos de la publicación
	 * @return {@code 200 OK} si la publicación fue actualizada exitosamente,
	 *         {@code 204 No Content} si no existe una publicación con el ID
	 *         proporcionado
	 */
	@PutMapping("/editarPublicacion")
	public ResponseEntity<String> editarPublicacion(@RequestParam Long id, @RequestBody PublicacionDTO dto) {
		int status = publiSer.updateByID(id, dto);
		if (status == 0) {
			return new ResponseEntity<>("Publicacion actualizado con éxito", HttpStatus.OK);
		} else {
			return new ResponseEntity<>("Publicacion no éxiste", HttpStatus.NO_CONTENT);
		}
	}

	/**
	 * Elimina permanentemente una publicación del sistema identificada por su ID.
	 * <p>
	 * Al eliminar una publicación, sus comentarios asociados también serán
	 * eliminados automáticamente por la configuración de cascada.
	 * Esta operación es irreversible.
	 * </p>
	 *
	 * @param id identificador único de la publicación a eliminar
	 * @return {@code 200 OK} si la publicación fue eliminada exitosamente,
	 *         {@code 204 No Content} si no existe una publicación con el ID
	 *         proporcionado
	 */
	@DeleteMapping("/eliminarpublicacion")
	public ResponseEntity<String> eliminarPublicacion(@RequestParam Long id) {
		int status = publiSer.deleteByID(id);
		if (status == 0) {
			return new ResponseEntity<>("Publicación eliminado con éxito", HttpStatus.OK);
		} else {
			return new ResponseEntity<>("Publicación no éxiste", HttpStatus.NO_CONTENT);
		}
	}
}