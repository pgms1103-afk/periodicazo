package co.edu.unbosque.periodicazo.service;

import java.util.List;

/**
 * Interfaz genérica que define las operaciones CRUD estándar para los servicios del sistema.
 * <p>
 * Cualquier servicio que maneje entidades con operaciones básicas de creación,
 * consulta, actualización y eliminación debe implementar esta interfaz,
 * parametrizándola con el DTO correspondiente.
 * </p>
 *
 * @param <T> tipo del DTO sobre el cual se aplican las operaciones CRUD.
 *
 * @author Periodicazo
 * @version 1.0
 */
public interface CRUDoperation<T> {

    /**
     * Crea y persiste un nuevo registro a partir de los datos proporcionados.
     *
     * @param data DTO con la información necesaria para crear el registro.
     * @return {@code 0} si la operación fue exitosa,
     *         {@code 1} si ocurrió algún error o no se cumplió una condición requerida.
     */
    public int create(T data);

    /**
     * Retorna todos los registros existentes del tipo {@code T}.
     *
     * @return lista con todos los registros encontrados,
     *         o una lista vacía si no existe ninguno.
     */
    public List<T> getAll();

    /**
     * Actualiza un registro existente identificado por su {@code id}
     * con los datos proporcionados en {@code data}.
     *
     * @param id   identificador único del registro a actualizar.
     * @param data DTO con los nuevos valores a aplicar.
     * @return {@code 0} si la actualización fue exitosa,
     *         {@code 1} si no se encontró un registro con el {@code id} indicado.
     */
    public int updateByID(Long id, T data);

    /**
     * Elimina el registro cuyo identificador coincide con el {@code id} proporcionado.
     *
     * @param id identificador único del registro a eliminar.
     * @return {@code 0} si la eliminación fue exitosa,
     *         {@code 1} si no se encontró un registro con el {@code id} indicado.
     */
    public int deleteByID(Long id);
}