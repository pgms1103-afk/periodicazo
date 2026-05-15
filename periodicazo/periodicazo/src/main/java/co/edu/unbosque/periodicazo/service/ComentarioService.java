package co.edu.unbosque.periodicazo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.unbosque.periodicazo.dto.ComentarioDTO;
import co.edu.unbosque.periodicazo.entity.Comentario;
import co.edu.unbosque.periodicazo.entity.Publicacion;
import co.edu.unbosque.periodicazo.entity.Usuario;
import co.edu.unbosque.periodicazo.repository.ComentarioRepository;
import co.edu.unbosque.periodicazo.repository.PublicacionRepository;

@Service
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
			comentario.setnombreComentador(data.getNombreComentador());;
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
      entityList.forEach(entity -> {
          ComentarioDTO dto = new ComentarioDTO();
          dto.setId(entity.getId());
          dto.setContenido(entity.getContenido());
          dto.setNombreComentador(entity.getNombreComentador());
          dto.setFecha(entity.getFecha());
          dto.setNombrePublicacion(entity.getPublicacion().getTitulo());
          dto.setPublicacionId(entity.getPublicacion() != null ? entity.getPublicacion().getId() : null);
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
			temp.setnombreComentador(data.getNombreComentador());;
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
	
	public List<ComentarioDTO> findByTitulo(String titulo) {
		Optional<List<Publicacion>> encontrado = publicacionRepo.findByTitulo(titulo);
		if(encontrado.isPresent()) {
			Publicacion publi = encontrado.get().get(0);
			List<Comentario> entityList = comentarioRepo.findByPublicacion(publi);
		      List<ComentarioDTO> dtoList = new ArrayList<>();
		      entityList.forEach(entity -> {
		          ComentarioDTO dto = new ComentarioDTO();
		          dto.setId(entity.getId());
		          dto.setContenido(entity.getContenido());
		          dto.setNombreComentador(entity.getNombreComentador());
		          dto.setFecha(entity.getFecha());
		          dto.setNombrePublicacion(entity.getPublicacion().getTitulo());
		          dto.setPublicacionId(entity.getPublicacion() != null ? entity.getPublicacion().getId() : null);
		          dtoList.add(dto);
		      });
		      return dtoList;
		}else {
			return new ArrayList<ComentarioDTO>();
		}
	}

}
