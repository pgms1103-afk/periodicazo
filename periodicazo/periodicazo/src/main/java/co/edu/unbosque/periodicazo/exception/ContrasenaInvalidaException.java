package co.edu.unbosque.periodicazo.exception;

/**
 * Excepción lanzada cuando la contraseña proporcionada no cumple con los requisitos
 * mínimos de seguridad definidos en la capa de negocio o servicio.
 * <p>
 * Esta excepción es de tipo {@link RuntimeException} (unchecked), por lo que no obliga
 * al llamador a capturarla explícitamente, aunque se recomienda manejarla en la capa
 * de controlador para devolver respuestas HTTP apropiadas (ej. 400 Bad Request).
 * </p>
 * <p>
 * Casos típicos que la provocan (según la lógica en {@code UsuarioService}):
 * <ul>
 *   <li>Contraseña nula o vacía</li>
 *   <li>Menos de 8 caracteres</li>
 *   <li>No contiene al menos una letra mayúscula</li>
 *   <li>No contiene al menos un número</li>
 * </ul>
 * </p>
 * <p>
 * Estas reglas son requisitos mínimos de seguridad básicos. En entornos de producción
 * se recomienda agregar más restricciones (caracteres especiales, longitud máxima,
 * no usar contraseñas comunes, etc.) y siempre almacenar contraseñas hasheadas.
 * </p>
 *
 * @author David Alejandro Velasquez Salamanca
 * @since 1.0
 * @see ApellidoInvalidoException
 * @see CedulaInvalidaException
 * @see NombreInvalidoException
 * @see CorreoInvalidoException
 * @see TipoUsuarioInvalidoException
 * @see IdExistException
 * @see UsuarioService
 */
public class ContrasenaInvalidaException extends RuntimeException {

    /**
     * Construye una nueva excepción con el mensaje de error detallado.
     *
     * @param mensaje descripción específica del problema con la contraseña  
     *                (ej. "La contrasena no puede estar vacia",  
     *                "La contrasena debe tener minimo 8 caracteres",  
     *                "La contrasena debe tener al menos una letra mayuscula",  
     *                "La contrasena debe tener al menos un numero")
     */
    public ContrasenaInvalidaException(String mensaje) {
        super(mensaje);
    }
}