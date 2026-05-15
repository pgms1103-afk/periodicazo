package co.edu.unbosque.periodicazo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.unbosque.periodicazo.dto.PublicacionDTO;
import co.edu.unbosque.periodicazo.dto.UsuarioDTO;
import co.edu.unbosque.periodicazo.entity.Publicacion;
import co.edu.unbosque.periodicazo.entity.Usuario;
import co.edu.unbosque.periodicazo.entity.Publicacion.Tipo;
import co.edu.unbosque.periodicazo.repository.PublicacionRepository;

@Service
public class PublicacionService implements CRUDoperation<PublicacionDTO>{
	
	@Autowired
	private ModelMapper mapper;
	
	@Autowired
	private PublicacionRepository publiRepo;

	@Override
	public int create(PublicacionDTO data) {
		Publicacion entity = new Publicacion();
		entity.setTipo(data.getTipo());
		entity.setTitulo(data.getTitulo());
		entity.setContenido(data.getContenido());
		entity.setAutor(data.getAutor());
		entity.setEditorial(data.getEditorial());
		entity.setFecha(data.getFecha());
		if(entity.getTipo() == Tipo.NOTICIA) {
			entity.setCategoria(data.getCategoria());
			
		}else {
			entity.setSigno(data.getSigno());
			entity.setElemento(data.getElemento());
		}
		publiRepo.save(entity);
		return 0;
	}

	@Override
	public List<PublicacionDTO> getAll() {
		List<Publicacion> entityList = publiRepo.findAll();
		List<PublicacionDTO> dtoList = new ArrayList<>();
		entityList.forEach(
				(entity) -> {
					PublicacionDTO dto = mapper.map(entity, PublicacionDTO.class);
					dtoList.add(dto);
				});
		return dtoList;
	}

	@Override
	public int updateByID(Long id, PublicacionDTO data) {
		Optional<Publicacion> found = publiRepo.findById(id);
		 if(found.isPresent()) {
			 Publicacion temp = found.get();
			 if(temp.getTipo() == Tipo.NOTICIA) {
				 temp.setTitulo(data.getTitulo());
				 temp.setContenido(data.getContenido());
				 temp.setAutor(data.getAutor());
				 temp.setEditorial(data.getEditorial());
				 temp.setFecha(data.getFecha());
				 temp.setCategoria(data.getCategoria());
					publiRepo.save(temp);
				}else {
					temp.setTitulo(data.getTitulo());
					temp.setContenido(data.getContenido());
					temp.setAutor(data.getAutor());
					temp.setEditorial(data.getEditorial());
					temp.setFecha(data.getFecha());
					temp.setSigno(data.getSigno());
					temp.setElemento(data.getElemento());
					publiRepo.save(temp);
				}
				return 0;
		    }else {
		    	return 1;
		    }
	}

	@Override
	public int deleteByID(Long id) {
		Optional<Publicacion> found = publiRepo.findById(id);
	    if (found.isPresent()) {
	    	publiRepo.delete(found.get());
	      return 0;
	    } else {
	      return 1;
	    }
	}
	
	public List<PublicacionDTO> findByTipo (Tipo tipo){
		Optional<List<Publicacion>> encontrados = publiRepo.findByTipo(tipo);
		if(encontrados.isPresent()) {
			List<Publicacion> entityList = encontrados.get();
			List<PublicacionDTO> dtoList = new ArrayList<>();
			entityList.forEach((entity) -> {
				PublicacionDTO dto = mapper.map(entity, PublicacionDTO.class);
				dtoList.add(dto);
			});
			return dtoList;
		}else {
			return new ArrayList<PublicacionDTO>();
		}
	}
}
