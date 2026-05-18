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
import co.edu.unbosque.periodicazo.exception.ContrasenaInvalidaException;
import co.edu.unbosque.periodicazo.repository.UsuarioRepository;

@Service
public class UsuarioService implements CRUDoperation<UsuarioDTO> {

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private UsuarioRepository usuarioRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public int create(UsuarioDTO data) {
    	if (data.getPassword() == null || data.getPassword() .isBlank()) {
            throw new ContrasenaInvalidaException("La contrasena no puede estar vacia");
        }
        if (data.getPassword().length() < 8) {
            throw new ContrasenaInvalidaException("La contrasena debe tener minimo 8 caracteres");
        }
        if (!data.getPassword().matches(".*[A-Z].*")) {
            throw new ContrasenaInvalidaException("La contrasena debe tener al menos una letra mayuscula");
        }
        Usuario entity = new Usuario();
        
        if (findUsernameAlreadyTaken(data.getUsername())) {
            return 1;
        } else {
            entity.setUsername(data.getUsername());
            entity.setPassword(passwordEncoder.encode(data.getPassword()));
        }
        
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
        if (found.isPresent()) {
            Usuario temp = found.get();
            temp.setUsername(data.getUsername());
            
            if (data.getPassword() != null && !data.getPassword().trim().isEmpty()) {
                temp.setPassword(passwordEncoder.encode(data.getPassword()));
            }
            
            if (data.getRole() != null) {
                temp.setRole(data.getRole());
            }
            usuarioRepo.save(temp);
            return 0;
        } else {
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
        if (newUser == null) {
            return false;
        }
        Optional<Usuario> found = usuarioRepo.findByUsername(newUser);
        return found.isPresent();
    }

    public UsuarioDTO getLoginUser(String username) {
        Optional<Usuario> entity = usuarioRepo.findByUsername(username);
        if (entity.isEmpty()) {
            throw new UsernameNotFoundException("Usuario no encontrado");
        }
        return mapper.map(entity, UsuarioDTO.class);
    }
}