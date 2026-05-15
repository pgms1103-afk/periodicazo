package co.edu.unbosque.periodicazo.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import co.edu.unbosque.periodicazo.entity.Comentario;
import co.edu.unbosque.periodicazo.entity.Publicacion;

public interface ComentarioRepository extends JpaRepository<Comentario, Long>{
	
	List<Comentario> findByPublicacion(Publicacion publicacion);
	
}
