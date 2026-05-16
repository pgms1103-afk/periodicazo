package co.edu.unbosque.periodicazo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import co.edu.unbosque.periodicazo.dto.PublicacionDTO;
import co.edu.unbosque.periodicazo.entity.Publicacion;
import co.edu.unbosque.periodicazo.entity.Publicacion.Tipo;
import co.edu.unbosque.periodicazo.repository.PublicacionRepository;

/**
 * Servicio que gestiona las operaciones CRUD para las publicaciones del sistema.
 * <p>
 * Implementa {@link CRUDoperation} con {@link PublicacionDTO} y maneja la lógica
 * de negocio para publicaciones de tipo {@link Tipo#NOTICIA} y de tipo horóscopo,
 * cuyos campos específicos difieren entre sí.
 * </p>
 *
 * @author Periodicazo
 * @version 1.0
 */
@Service
public class PublicacionService implements CRUDoperation<PublicacionDTO> {

    /**
     * Mapper utilizado para convertir entidades {@link Publicacion}
     * a objetos {@link PublicacionDTO} de forma automática.
     */
    @Autowired
    private ModelMapper mapper;

    /**
     * Repositorio para operaciones de persistencia sobre {@link Publicacion}.
     */
    @Autowired
    private PublicacionRepository publiRepo;

    /**
     * Crea y persiste una nueva publicación a partir de los datos proporcionados.
     * <p>
     * Si el tipo de publicación es {@link Tipo#NOTICIA}, se asigna la categoría.
     * En caso contrario (horóscopo), se asignan el signo y el elemento.
     * </p>
     *
     * @param data DTO con la información de la publicación a crear.
     * @return {@code 0} siempre, indicando que la operación fue exitosa.
     */
    @Override
    public int create(PublicacionDTO data) {
        Publicacion entity = new Publicacion();
        entity.setTipo(data.getTipo());
        entity.setTitulo(data.getTitulo());
        entity.setContenido(data.getContenido());
        entity.setAutor(data.getAutor());
        entity.setEditorial(data.getEditorial());
        entity.setFecha(data.getFecha());
        if (entity.getTipo() == Tipo.NOTICIA) {
            entity.setCategoria(data.getCategoria());
        } else {
            entity.setSigno(data.getSigno());
            entity.setElemento(data.getElemento());
        }
        publiRepo.save(entity);
        return 0;
    }

    /**
     * Retorna todas las publicaciones registradas en el sistema.
     * <p>
     * Cada entidad {@link Publicacion} es convertida a {@link PublicacionDTO}
     * mediante {@link ModelMapper}.
     * </p>
     *
     * @return lista de {@link PublicacionDTO} con todas las publicaciones existentes,
     *         o una lista vacía si no hay ninguna.
     */
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

    /**
     * Actualiza una publicación existente identificada por su {@code id}.
     * <p>
     * Los campos actualizados dependen del tipo de publicación:
     * <ul>
     *   <li>Si es {@link Tipo#NOTICIA}: actualiza título, contenido, autor,
     *       editorial, fecha y categoría.</li>
     *   <li>En caso contrario (horóscopo): actualiza título, contenido, autor,
     *       editorial, fecha, signo y elemento.</li>
     * </ul>
     * </p>
     *
     * @param id   identificador único de la publicación a actualizar.
     * @param data DTO con los nuevos valores a aplicar.
     * @return {@code 0} si la actualización fue exitosa,
     *         {@code 1} si no se encontró una publicación con el {@code id} indicado.
     */
    @Override
    public int updateByID(Long id, PublicacionDTO data) {
        Optional<Publicacion> found = publiRepo.findById(id);
        if (found.isPresent()) {
            Publicacion temp = found.get();
            if (temp.getTipo() == Tipo.NOTICIA) {
                temp.setTitulo(data.getTitulo());
                temp.setContenido(data.getContenido());
                temp.setAutor(data.getAutor());
                temp.setEditorial(data.getEditorial());
                temp.setFecha(data.getFecha());
                temp.setCategoria(data.getCategoria());
                publiRepo.save(temp);
            } else {
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
        } else {
            return 1;
        }
    }

    /**
     * Elimina la publicación cuyo identificador coincide con el {@code id} proporcionado.
     *
     * @param id identificador único de la publicación a eliminar.
     * @return {@code 0} si la eliminación fue exitosa,
     *         {@code 1} si no se encontró una publicación con el {@code id} indicado.
     */
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

    /**
     * Retorna todas las publicaciones que coincidan con el tipo especificado.
     * <p>
     * Consulta el repositorio filtrando por {@link Tipo}. Si se encuentran resultados,
     * los convierte a {@link PublicacionDTO} mediante {@link ModelMapper}.
     * Si no hay publicaciones del tipo indicado, retorna una lista vacía.
     * </p>
     *
     * @param tipo tipo de publicación a buscar ({@link Tipo#NOTICIA} u horóscopo).
     * @return lista de {@link PublicacionDTO} que coinciden con el tipo dado,
     *         o una lista vacía si no existe ninguna publicación de ese tipo.
     */
    public List<PublicacionDTO> findByTipo(Tipo tipo) {
        Optional<List<Publicacion>> encontrados = publiRepo.findByTipo(tipo);
        if (encontrados.isPresent()) {
            List<Publicacion> entityList = encontrados.get();
            List<PublicacionDTO> dtoList = new ArrayList<>();
            entityList.forEach((entity) -> {
                PublicacionDTO dto = mapper.map(entity, PublicacionDTO.class);
                dtoList.add(dto);
            });
            return dtoList;
        } else {
            return new ArrayList<PublicacionDTO>();
        }
    }
}