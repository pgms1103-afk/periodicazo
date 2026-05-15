package co.edu.unbosque.periodicazo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.unbosque.periodicazo.entity.Comentario;

public interface ComentarioRepository extends JpaRepository<Comentario, Long>{
	
}
