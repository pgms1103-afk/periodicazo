package co.edu.unbosque.periodicazo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import co.edu.unbosque.periodicazo.dto.UsuarioDTO;
import co.edu.unbosque.periodicazo.entity.Usuario;
import co.edu.unbosque.periodicazo.repository.UsuarioRepository;

@Service
public class UsuarioService implements CRUDoperation<UsuarioDTO>{
	
	@Autowired
	private ModelMapper mapper;
	@Autowired
	private UsuarioRepository usuarioRepo;
	@Autowired 
	private PasswordEncoder passwordEncoder;

	@Override
	public int create(UsuarioDTO data) {
		Usuario entity = new Usuario();
		if(findUsernameAlreadyTaken(entity.getUsername())) {
			return 1;
		} else{ 
			entity.setUsername(data.getUsername());
			entity.setPassword(passwordEncoder.encode(data.getPassword())); }
		 if (data.getRole() != null) {
		        entity.setRole(data.getRole());
		      }
		 usuarioRepo.save(entity);
		return 0;
	}

	@Override
	public List<UsuarioDTO> getAll() {
		List<Usuario> entityList = usuarioRepo.findAll();
		List<UsuarioDTO> dtoList = new ArrayList<>();
		entityList.forEach(
				(entity) -> {
					UsuarioDTO dto = mapper.map(entity, UsuarioDTO.class);
					dtoList.add(dto);
				});
		return dtoList;
	}

	@Override
	public int updateByID(Long id, UsuarioDTO data) {
	    Optional<Usuario> found = usuarioRepo.findById(id);
	    if(found.isPresent()) {
	    	Usuario temp = found.get();
		      temp.setUsername(data.getUsername());
		      temp.setPassword(passwordEncoder.encode(data.getPassword()));
		      if (data.getRole() != null) {
		        temp.setRole(data.getRole());
		      }
		      usuarioRepo.save(temp);
			return 0;
	    }else {
	    	return 1;
	    }
	    
	}

	@Override
	public int deleteByID(Long id) {
		 Optional<Usuario> found = usuarioRepo.findById(id);
		    if (found.isPresent()) {
		    	usuarioRepo.delete(found.get());
		      return 0;
		    } else {
		      return 1;
		    }
	}

	
	
	public boolean findUsernameAlreadyTaken(String newUser) {
	    Optional<Usuario> found = usuarioRepo.findByUsername(newUser);
	    if (found.isPresent()) {
	      return true;
	    } else {
	      return false;
	    }
	  }
	
	public UsuarioDTO getLoginUser(String username) {
		Optional<Usuario> entity = usuarioRepo.findByUsername(username);
				if(entity.isEmpty()) {
					throw new UsernameNotFoundException("Usuario no encontrado");
				}
	    return mapper.map(entity, UsuarioDTO.class);
	}
	

}
