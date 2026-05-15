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


@RestController
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/private/comentario")
@CrossOrigin(origins = { "http://localhost:8080", "*" })
public class ComentarioController {
	
	@Autowired
	private ComentarioService comentarioSer;
	
	@PostMapping("/crearcomentario")
	public ResponseEntity<String> crearComentario(@RequestBody ComentarioDTO dto) {
		
		int status = comentarioSer.create(dto);
		
		if(status == 0) {
			return new ResponseEntity<>("Comentario creado con éxito", HttpStatus.OK);
		}else {
			return new ResponseEntity<>("Error, publicacion no encontrada", HttpStatus.NOT_FOUND);
		}
	}
	
	@GetMapping("/mostrarcomentarios")
	public ResponseEntity<List<ComentarioDTO>> mostrarComentarios() {
		List<ComentarioDTO> lista = comentarioSer.getAll();
		if(lista.isEmpty()) {
			return new ResponseEntity<>(lista, HttpStatus.NO_CONTENT);
		}else {
			return new ResponseEntity<>(lista, HttpStatus.OK);
		}
	}
	
	@GetMapping("mostrarportitulo")
	public ResponseEntity<List<ComentarioDTO>> mostrarPorTitulo (@RequestParam String titulo){
		List<ComentarioDTO> encontrados = comentarioSer.findByTitulo(titulo);
		if(encontrados.isEmpty()) {
			return new ResponseEntity<>(encontrados, HttpStatus.NO_CONTENT);
		}else {
			return new ResponseEntity<>(encontrados, HttpStatus.OK);
		}
	}
	
	@PutMapping("/actualizarcomentario")
	public ResponseEntity<String> putMethodName(@RequestParam Long id, @RequestBody ComentarioDTO dto) {
		int status = comentarioSer.updateByID(id, dto);
		if(status == 0) {
			return new ResponseEntity<>("Comentario actualizado con éxito", HttpStatus.OK);
		}else {
			return new ResponseEntity<>("Comentario no existe", HttpStatus.NO_CONTENT);
		}
	}
	
	@DeleteMapping("/eliminarcomentario")
	public ResponseEntity<String> eliminarComentario (@RequestParam Long id){
		int status = comentarioSer.deleteByID(id);
		if(status == 0) {
			return new ResponseEntity<>("Comentario eliminado con éxito", HttpStatus.OK);
		}else {
			return new ResponseEntity<>("Comentario no existe", HttpStatus.NO_CONTENT);
		}
	}
	
	
	
	
	

}
