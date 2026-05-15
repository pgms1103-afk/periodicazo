package co.edu.unbosque.periodicazo.repository;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.unbosque.periodicazo.entity.Usuario;


public interface UsuarioRepository extends JpaRepository<Usuario, Long>{
	
	public Optional<Usuario> findByUsername(String username);
	
}
