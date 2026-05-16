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

/**
 * Servicio que gestiona las operaciones CRUD para los usuarios del sistema.
 * <p>
 * Implementa {@link CRUDoperation} con {@link UsuarioDTO} y maneja la lógica
 * de negocio relacionada con la creación, consulta, actualización y eliminación
 * de usuarios, incluyendo el cifrado de contraseñas y la validación de
 * disponibilidad de nombres de usuario.
 * </p>
 *
 * @author Periodicazo
 * @version 1.0
 */
@Service
public class UsuarioService implements CRUDoperation<UsuarioDTO> {

    /**
     * Mapper utilizado para convertir entidades {@link Usuario}
     * a objetos {@link UsuarioDTO} de forma automática.
     */
    @Autowired
    private ModelMapper mapper;

    /**
     * Repositorio para operaciones de persistencia sobre {@link Usuario}.
     */
    @Autowired
    private UsuarioRepository usuarioRepo;

    /**
     * Codificador de contraseñas utilizado para cifrar las passwords
     * antes de persistirlas en la base de datos.
     */
    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Crea y persiste un nuevo usuario a partir de los datos proporcionados.
     * <p>
     * Verifica primero que el nombre de usuario no esté ya en uso.
     * Si está disponible, cifra la contraseña antes de persistir la entidad.
     * El rol se asigna únicamente si viene informado en el DTO.
     * </p>
     *
     * @param data DTO con la información del usuario a crear,
     *             incluyendo username, password y opcionalmente el rol.
     * @return {@code 0} si el usuario fue creado exitosamente,
     *         {@code 1} si el nombre de usuario ya está en uso.
     */
    @Override
    public int create(UsuarioDTO data) {
        Usuario entity = new Usuario();
        if (findUsernameAlreadyTaken(entity.getUsername())) {
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
     * Retorna todos los usuarios registrados en el sistema.
     * <p>
     * Cada entidad {@link Usuario} es convertida a {@link UsuarioDTO}
     * mediante {@link ModelMapper}.
     * </p>
     *
     * @return lista de {@link UsuarioDTO} con todos los usuarios existentes,
     *         o una lista vacía si no hay ninguno.
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
     * Actualiza el username, la contraseña y opcionalmente el rol
     * de un usuario existente identificado por su {@code id}.
     * <p>
     * La nueva contraseña es cifrada antes de ser persistida.
     * El rol solo se actualiza si viene informado en el DTO.
     * </p>
     *
     * @param id   identificador único del usuario a actualizar.
     * @param data DTO con los nuevos valores de username, password y rol.
     * @return {@code 0} si la actualización fue exitosa,
     *         {@code 1} si no se encontró un usuario con el {@code id} indicado.
     */
    @Override
    public int updateByID(Long id, UsuarioDTO data) {
        Optional<Usuario> found = usuarioRepo.findById(id);
        if (found.isPresent()) {
            Usuario temp = found.get();
            temp.setUsername(data.getUsername());
            temp.setPassword(passwordEncoder.encode(data.getPassword()));
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
     * Elimina el usuario cuyo identificador coincide con el {@code id} proporcionado.
     *
     * @param id identificador único del usuario a eliminar.
     * @return {@code 0} si la eliminación fue exitosa,
     *         {@code 1} si no se encontró un usuario con el {@code id} indicado.
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
     *
     * @param newUser nombre de usuario a verificar.
     * @return {@code true} si el username ya existe en la base de datos,
     *         {@code false} si está disponible.
     */
    public boolean findUsernameAlreadyTaken(String newUser) {
        Optional<Usuario> found = usuarioRepo.findByUsername(newUser);
        if (found.isPresent()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Busca y retorna el DTO del usuario correspondiente al username proporcionado.
     * <p>
     * Este método está pensado para ser utilizado durante el proceso de autenticación.
     * Si no se encuentra ningún usuario con ese username, lanza una excepción
     * de Spring Security.
     * </p>
     *
     * @param username nombre de usuario a buscar.
     * @return {@link UsuarioDTO} con la información del usuario encontrado.
     * @throws UsernameNotFoundException si no existe ningún usuario
     *                                   con el {@code username} indicado.
     */
    public UsuarioDTO getLoginUser(String username) {
        Optional<Usuario> entity = usuarioRepo.findByUsername(username);
        if (entity.isEmpty()) {
            throw new UsernameNotFoundException("Usuario no encontrado");
        }
        return mapper.map(entity, UsuarioDTO.class);
    }
}