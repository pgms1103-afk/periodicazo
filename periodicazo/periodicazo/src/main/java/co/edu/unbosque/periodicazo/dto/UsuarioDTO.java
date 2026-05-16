package co.edu.unbosque.periodicazo.dto;

import java.util.Objects;
import co.edu.unbosque.periodicazo.entity.Usuario.Role;

/**
 * Objeto de transferencia de datos (DTO) para la entidad Usuario.
 * <p>
 * Se utiliza para transportar la información de los usuarios entre
 * las capas de la aplicación (controller, service) sin exponer directamente
 * la entidad JPA ni los detalles internos de Spring Security.
 * </p>
 */
public class UsuarioDTO {

	/** Identificador único del usuario. */
	private Long id;

	/** Nombre de usuario único en el sistema. */
	private String username;

	/** Contraseña del usuario. Se almacena encriptada en la base de datos. */
	private String password;

	/** Rol del usuario que determina sus permisos en el sistema. */
	private Role role;

	/**
	 * Constructor vacío requerido para la deserialización JSON y
	 * el uso con frameworks como ModelMapper.
	 */
	public UsuarioDTO() {
	}

	/**
	 * Constructor completo con todos los campos del DTO.
	 *
	 * @param id       identificador único del usuario
	 * @param username nombre de usuario único en el sistema
	 * @param password contraseña del usuario
	 * @param role     rol del usuario (ADMIN, EDITOR, COMENTADOR o USUARIO)
	 */
	public UsuarioDTO(Long id, String username, String password, Role role) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
		this.role = role;
	}

	/**
	 * Obtiene el identificador único del usuario.
	 *
	 * @return identificador del usuario
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Establece el identificador único del usuario.
	 *
	 * @param id identificador del usuario
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Obtiene el nombre de usuario.
	 *
	 * @return nombre de usuario
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Establece el nombre de usuario.
	 *
	 * @param username nombre de usuario único en el sistema
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * Obtiene la contraseña del usuario.
	 *
	 * @return contraseña del usuario
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Establece la contraseña del usuario.
	 *
	 * @param password contraseña del usuario en texto plano antes de encriptar
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * Obtiene el rol del usuario.
	 *
	 * @return rol del usuario (ADMIN, EDITOR, COMENTADOR o USUARIO)
	 */
	public Role getRole() {
		return role;
	}

	/**
	 * Establece el rol del usuario.
	 *
	 * @param role rol del usuario que determina sus permisos en el sistema
	 */
	public void setRole(Role role) {
		this.role = role;
	}

	/**
	 * Retorna una representación en cadena del objeto {@code UsuarioDTO}.
	 *
	 * @return cadena con todos los campos del DTO
	 */
	@Override
	public String toString() {
		return "UsuarioDTO [id=" + id + ", username=" + username + ", password=" + password + ", role=" + role + "]";
	}

	/**
	 * Calcula el código hash del objeto basándose en todos sus campos.
	 *
	 * @return código hash del objeto
	 */
	@Override
	public int hashCode() {
		return Objects.hash(id, password, role, username);
	}

	/**
	 * Compara este objeto con otro para determinar si son iguales,
	 * basándose en todos sus campos.
	 *
	 * @param obj objeto con el que se compara
	 * @return {@code true} si todos los campos son iguales, {@code false} en caso contrario
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UsuarioDTO other = (UsuarioDTO) obj;
		return Objects.equals(id, other.id) && Objects.equals(password, other.password) && role == other.role
				&& Objects.equals(username, other.username);
	}
}