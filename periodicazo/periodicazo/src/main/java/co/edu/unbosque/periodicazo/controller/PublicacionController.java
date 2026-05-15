package co.edu.unbosque.periodicazo.controller;

import java.util.List;

import org.apache.catalina.connector.Response;
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


@RestController
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/private/publicacion")
@CrossOrigin(origins = { "http://localhost:8080", "*" })
public class PublicacionController {
	
	@Autowired
	private PublicacionService publiSer;
	
	@PostMapping("/crearpublicacion")
	public ResponseEntity<String> crearPublicacion(@RequestBody PublicacionDTO dto) {
		int status = publiSer.create(dto);
		
		if(status == 0) {
			return new ResponseEntity<>("Publicacion creado con éxito", HttpStatus.CREATED);
		}else {
			return new ResponseEntity<>("Error inesperado al crear el usuario", HttpStatus.INTERNAL_SERVER_ERROR);
		}	
	}
	
	@GetMapping("/mostrarportipo")
	public ResponseEntity<List<PublicacionDTO>> mostrarPorTipo(@RequestParam Tipo tipo) {
		List<PublicacionDTO> encontrados = publiSer.findByTipo(tipo);
		if(encontrados.isEmpty()) {
			return new ResponseEntity<List<PublicacionDTO>>(encontrados, HttpStatus.NO_CONTENT);
		}else {
			return new ResponseEntity<List<PublicacionDTO>>(encontrados, HttpStatus.OK);
		}
	}
	
	@GetMapping("/mostrartodo")
	public ResponseEntity<List<PublicacionDTO>> mostrarTodo() {
		List<PublicacionDTO> encontrados = publiSer.getAll();
		if(encontrados.isEmpty()) {
			return new ResponseEntity<List<PublicacionDTO>>(encontrados, HttpStatus.NO_CONTENT);
		}else {
			return new ResponseEntity<List<PublicacionDTO>>(encontrados, HttpStatus.OK);
		}
	}
	
	@PutMapping("/editarPublicacion")
	public ResponseEntity<String> editarPublicacion (@RequestParam Long id, @RequestBody PublicacionDTO dto){
		int status = publiSer.updateByID(id, dto);
		if(status == 0) {
			return new ResponseEntity<>("Publicacion actualizado con éxito", HttpStatus.OK);
		}else {
			return new ResponseEntity<>("Publicacion no éxiste", HttpStatus.NO_CONTENT);
		}
	}
	
	@DeleteMapping("/eliminarpublicacion")
	public ResponseEntity<String> eliminarPublicacion (@RequestParam Long id){
		int status = publiSer.deleteByID(id);
		if(status == 0) {
			return new ResponseEntity<>("Usuario eliminado con éxito", HttpStatus.OK);
		}else {
			return new ResponseEntity<>("Usuario no éxiste", HttpStatus.NO_CONTENT);
		}
	}
	
	
	
	
	
	

}
