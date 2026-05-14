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
import org.springframework.web.bind.annotation.PathVariable;



@RestController
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/admin")
@CrossOrigin(origins = { "http://localhost:8080", "*" })
public class AdminController {
	
	@Autowired
	private UsuarioService userSer;
	
	@PostMapping("/crearusuario")
	public ResponseEntity<String> crearUsuario(@RequestBody UsuarioDTO dto) {
		int status = userSer.create(dto);
		
		if(status == 0) {
			return new ResponseEntity<>("Usuario creado con éxito", HttpStatus.CREATED);
		}else if(status == 1) {
			return new ResponseEntity<>("Ya existe un usuario con ese username", HttpStatus.CONFLICT);
		}else {
			return new ResponseEntity<>("Error inesperado al crear el usuario", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/mostrarusuario")
	public ResponseEntity<List<UsuarioDTO>> mostrarUsuario() {
		List<UsuarioDTO> usuarios = userSer.getAll();
		if (usuarios.isEmpty()) {
			return new ResponseEntity<List<UsuarioDTO>>(usuarios, HttpStatus.NO_CONTENT);
		} else {
			return new ResponseEntity<List<UsuarioDTO>>(usuarios, HttpStatus.ACCEPTED);
		}
	}
	
	@PutMapping("actualizarusuario")
	public ResponseEntity<String> actualizarUsuario(@RequestParam Long id, @RequestBody UsuarioDTO dto) {
		int status = userSer.updateByID(id, dto);
		if(status == 0) {
			return new ResponseEntity<>("Usuario actualizado con éxito", HttpStatus.OK);
		}else {
			return new ResponseEntity<>("Usuario no éxiste", HttpStatus.NO_CONTENT);
		}
	}
	
	@DeleteMapping("/eliminarusuario")
	public ResponseEntity<String> eliminarUsuario (@RequestParam Long id){
		int status = userSer.deleteByID(id);
		if(status == 0) {
			return new ResponseEntity<>("Usuario eliminado con éxito", HttpStatus.OK);
		}else {
			return new ResponseEntity<>("Usuario no éxiste", HttpStatus.NO_CONTENT);
		}
	}
	
	

}
