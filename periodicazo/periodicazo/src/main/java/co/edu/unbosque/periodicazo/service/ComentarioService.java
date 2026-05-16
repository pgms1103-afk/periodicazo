package co.edu.unbosque.periodicazo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import co.edu.unbosque.periodicazo.dto.ComentarioDTO;
import co.edu.unbosque.periodicazo.entity.Comentario;
import co.edu.unbosque.periodicazo.entity.Publicacion;
import co.edu.unbosque.periodicazo.repository.ComentarioRepository;
import co.edu.unbosque.periodicazo.repository.PublicacionRepository;

/**
 * Servicio que gestiona las operaciones CRUD para los comentarios del sistema.
 * <p>
 * Implementa {@link CRUDoperation} con {@link ComentarioDTO} y se encarga de
 * la lógica de negocio relacionada con la creación, consulta, actualización
 * y eliminación de comentarios, así como su asociación con publicaciones.
 * </p>
 *
 * @author Periodicazo
 * @version 1.0
 */
@Service
public class ComentarioService implements CRUDoperation<ComentarioDTO> {

    /**
     * Repositorio para operaciones de persistencia sobre {@link Comentario}.
     */
    @Autowired
    private ComentarioRepository comentarioRepo;

    /**
     * Repositorio para consultas sobre {@link Publicacion},
     * utilizado para validar la existencia de una publicación antes de crear un comentario.
     */
    @Autowired
    private PublicacionRepository publicacionRepo;

    /**
     * Crea un nuevo comentario y lo asocia a una publicación existente.
     * <p>
     * Busca la publicación referenciada por {@code data.getPublicacionId()}.
     * Si no existe, retorna {@code 1}. Si existe, persiste el comentario y retorna {@code 0}.
     * </p>
     *
     * @param data DTO con los datos del comentario a crear, incluyendo el ID de la publicación asociada.
     * @return {@code 0} si el comentario fue creado exitosamente,
     *         {@code 1} si la publicación referenciada no existe.
     */
    @Override
    public int create(ComentarioDTO data) {
        Optional<Publicacion> encontrado = publicacionRepo.findById(data.getPublicacionId());
        if (encontrado.isEmpty()) {
            return 1;
        } else {
            Comentario comentario = new Comentario();
            comentario.setnombreComentador(data.getNombreComentador());
            comentario.setContenido(data.getContenido());
            comentario.setFecha(data.getFecha());
            comentario.setPublicacion(encontrado.get());
            comentarioRepo.save(comentario);
            return 0;
        }
    }

    /**
     * Retorna todos los comentarios registrados en el sistema.
     * <p>
     * Convierte cada entidad {@link Comentario} en un {@link ComentarioDTO},
     * incluyendo el título y el ID de la publicación asociada.
     * </p>
     *
     * @return lista de {@link ComentarioDTO} con todos los comentarios existentes.
     *         Retorna una lista vacía si no hay comentarios.
     */
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

    /**
     * Actualiza el nombre del comentador y el contenido de un comentario existente.
     * <p>
     * Busca el comentario por su {@code id}. Si no existe, retorna {@code 1}.
     * Si existe, aplica los cambios de {@code data} y persiste la entidad actualizada.
     * </p>
     *
     * @param id   identificador único del comentario a actualizar.
     * @param data DTO con los nuevos valores de {@code nombreComentador} y {@code contenido}.
     * @return {@code 0} si la actualización fue exitosa,
     *         {@code 1} si no se encontró el comentario con el {@code id} indicado.
     */
    @Override
    public int updateByID(Long id, ComentarioDTO data) {
        Optional<Comentario> encontrado = comentarioRepo.findById(id);
        if (encontrado.isEmpty()) {
            return 1;
        } else {
            Comentario temp = encontrado.get();
            temp.setnombreComentador(data.getNombreComentador());
            temp.setContenido(data.getContenido());
            comentarioRepo.save(temp);
            return 0;
        }
    }

    /**
     * Elimina el comentario cuyo identificador coincide con el {@code id} proporcionado.
     *
     * @param id identificador único del comentario a eliminar.
     * @return {@code 0} si el comentario fue eliminado exitosamente,
     *         {@code 1} si no se encontró un comentario con el {@code id} indicado.
     */
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

    /**
     * Retorna todos los comentarios asociados a la primera publicación que coincida con el título dado.
     * <p>
     * Busca publicaciones cuyo título sea igual a {@code titulo}. Si se encuentra al menos una,
     * toma la primera y retorna sus comentarios como lista de {@link ComentarioDTO}.
     * Si no se encuentra ninguna publicación con ese título, retorna una lista vacía.
     * </p>
     *
     * @param titulo título de la publicación cuyos comentarios se desean consultar.
     * @return lista de {@link ComentarioDTO} de los comentarios encontrados,
     *         o una lista vacía si no existe ninguna publicación con ese título.
     */
    public List<ComentarioDTO> findByTitulo(String titulo) {
        Optional<List<Publicacion>> encontrado = publicacionRepo.findByTitulo(titulo);
        if (encontrado.isPresent()) {
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
        } else {
            return new ArrayList<ComentarioDTO>();
        }
    }
}