package co.edu.unbosque.periodicazo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.unbosque.periodicazo.entity.Publicacion;
import co.edu.unbosque.periodicazo.entity.Publicacion.Tipo;

public interface PublicacionRepository extends JpaRepository<Publicacion, Long>{
	
	public Optional<List<Publicacion>> findByTipo(Tipo tipo);

}
