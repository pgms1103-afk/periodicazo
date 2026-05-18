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

/**
 * Servicio que implementa la lógica de negocio para la gestión de usuarios.
 * <p>
 * Provee operaciones CRUD sobre la entidad {@link Usuario}, incluyendo
 * validaciones de contraseña, encriptación con BCrypt y verificación de
 * disponibilidad de nombre de usuario. Es utilizado tanto por el
 * {@code PublicController} para el registro, como por el {@code AdminController}
 * para la administración completa de usuarios.
 * </p>
 */
@Service
public class UsuarioService implements CRUDoperation<UsuarioDTO> {

    /** Mapeador para convertir entre entidades {@link Usuario} y objetos {@link UsuarioDTO}. */
    @Autowired
    private ModelMapper mapper;

    /** Repositorio de usuarios para operaciones de persistencia en la base de datos. */
    @Autowired
    private UsuarioRepository usuarioRepo;

    /** Codificador de contraseñas BCrypt para encriptar antes de persistir. */
    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Crea un nuevo usuario en el sistema tras validar su contraseña.
     * <p>
     * Aplica las siguientes validaciones antes de persistir:
     * </p>
     * <ul>
     *   <li>La contraseña no puede ser nula ni estar vacía.</li>
     *   <li>La contraseña debe tener mínimo 8 caracteres.</li>
     *   <li>La contraseña debe contener al menos una letra mayúscula.</li>
     * </ul>
     * <p>
     * Si el nombre de usuario ya está en uso, retorna {@code 1} sin persistir.
     * Si se proporciona un rol en el DTO, se asigna al usuario; de lo contrario
     * se usa el rol por defecto de la entidad.
     * </p>
     *
     * @param data objeto {@link UsuarioDTO} con el username, password y rol opcional
     *             del usuario a crear
     * @return {@code 0} si el usuario fue creado exitosamente,
     *         {@code 1} si el nombre de usuario ya está en uso
     * @throws ContrasenaInvalidaException si la contraseña no cumple los requisitos
     *                                     de seguridad establecidos
     */
    @Override
    public int create(UsuarioDTO data) {
        if (data.getPassword() == null || data.getPassword().isBlank()) {
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

    /**
     * Obtiene la lista de todos los usuarios registrados en el sistema.
     * <p>
     * Convierte cada entidad {@link Usuario} a su representación {@link UsuarioDTO}
     * usando {@link ModelMapper}.
     * </p>
     *
     * @return lista de {@link UsuarioDTO} con todos los usuarios registrados;
     *         lista vacía si no hay ninguno
     */
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

    /**
     * Actualiza los datos de un usuario existente identificado por su ID.
     * <p>
     * Siempre actualiza el username. La contraseña solo se actualiza si se
     * proporciona un valor no nulo y no vacío, permitiendo modificar otros
     * campos sin necesidad de re-ingresar la contraseña. El rol también se
     * actualiza únicamente si se proporciona en el DTO.
     * </p>
     *
     * @param id   identificador único del usuario a actualizar
     * @param data objeto {@link UsuarioDTO} con los nuevos datos del usuario
     * @return {@code 0} si el usuario fue actualizado exitosamente,
     *         {@code 1} si no existe un usuario con el ID proporcionado
     */
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

    /**
     * Elimina un usuario del sistema identificado por su ID.
     *
     * @param id identificador único del usuario a eliminar
     * @return {@code 0} si el usuario fue eliminado exitosamente,
     *         {@code 1} si no existe un usuario con el ID proporcionado
     */
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

    /**
     * Verifica si un nombre de usuario ya está registrado en el sistema.
     * <p>
     * Usado como validación previa antes de crear un nuevo usuario para
     * evitar duplicados. Si el valor proporcionado es {@code null}, retorna
     * {@code false} sin consultar la base de datos.
     * </p>
     *
     * @param newUser nombre de usuario a verificar
     * @return {@code true} si el nombre de usuario ya está en uso,
     *         {@code false} si está disponible o si el valor es {@code null}
     */
    public boolean findUsernameAlreadyTaken(String newUser) {
        if (newUser == null) {
            return false;
        }
        Optional<Usuario> found = usuarioRepo.findByUsername(newUser);
        return found.isPresent();
    }

    /**
     * Obtiene el DTO del usuario autenticado a partir de su nombre de usuario.
     * <p>
     * Utilizado para recuperar los datos del usuario activo durante una sesión.
     * </p>
     *
     * @param username nombre de usuario a buscar
     * @return objeto {@link UsuarioDTO} con los datos del usuario encontrado
     * @throws UsernameNotFoundException si no existe ningún usuario registrado
     *                                   con el nombre de usuario proporcionado
     */
    public UsuarioDTO getLoginUser(String username) {
        Optional<Usuario> entity = usuarioRepo.findByUsername(username);
        if (entity.isEmpty()) {
            throw new UsernameNotFoundException("Usuario no encontrado");
        }
        return mapper.map(entity, UsuarioDTO.class);
    }
}