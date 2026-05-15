package co.edu.unbosque.periodicazo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import co.edu.unbosque.periodicazo.dto.ComentarioDTO;
import co.edu.unbosque.periodicazo.entity.Comentario;
import co.edu.unbosque.periodicazo.entity.Publicacion;
import co.edu.unbosque.periodicazo.entity.Usuario;
import co.edu.unbosque.periodicazo.repository.ComentarioRepository;
import co.edu.unbosque.periodicazo.repository.PublicacionRepository;

public class ComentarioService implements CRUDoperation<ComentarioDTO> {

	@Autowired
	private ModelMapper mapper;

	@Autowired
	private ComentarioRepository comentarioRepo;

	@Autowired
	private PublicacionRepository publicacionRepo;

	@Override
	public int create(ComentarioDTO data) {

		Optional<Publicacion> encontrado = publicacionRepo.findById(data.getPublicacionId());

		if (encontrado.isEmpty()) {
			return 1;
		} else {
			Comentario comentario = new Comentario();
			comentario.setNombre(data.getNombre());
			comentario.setContenido(data.getContenido());
			comentario.setFecha(data.getFecha());
			comentario.setPublicacion(encontrado.get());
			comentarioRepo.save(comentario);
			return 0;
		}
	}

	@Override
	public List<ComentarioDTO> getAll() {
		List<Comentario> entityList = comentarioRepo.findAll();
		List<ComentarioDTO> dtoList = new ArrayList<>();
		entityList.forEach((entity) -> {
			ComentarioDTO dto = mapper.map(entity, ComentarioDTO.class);
			dtoList.add(dto);
		});
		return dtoList;
	}

	@Override
	public int updateByID(Long id, ComentarioDTO data) {
		Optional<Comentario> encontrado = comentarioRepo.findById(id);
		if(encontrado.isEmpty()) {
			return 1;
		}else {
			Comentario temp = encontrado.get();
			temp.setNombre(data.getNombre());
			temp.setContenido(data.getContenido());
			comentarioRepo.save(temp);
			return 0;
		}
	}
	

	@Override
	public int deleteByID(Long id) {
		 Optional<Comentario> encontrado = comentarioRepo.findById(id);
		    if (encontrado.isPresent()) {
		    	comentarioRepo.delete(encontrado.get());
		      return 0;
		    } else {
		      return 1;
		    }
	}

}
