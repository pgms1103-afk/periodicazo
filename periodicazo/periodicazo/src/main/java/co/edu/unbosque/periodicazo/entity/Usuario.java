package co.edu.unbosque.periodicazo.entity;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Entidad JPA que representa un usuario del sistema del periódico.
 * <p>
 * Implementa {@link UserDetails} de Spring Security para integrarse con el
 * mecanismo de autenticación y autorización basado en JWT. Cada usuario tiene
 * un rol que determina los endpoints a los que puede acceder.
 * </p>
 * <p>
 * Los roles disponibles son:
 * </p>
 * <ul>
 *   <li>{@link Role#ADMIN} — acceso total al sistema.</li>
 *   <li>{@link Role#EDITOR} — puede crear y editar publicaciones.</li>
 *   <li>{@link Role#COMENTADOR} — puede crear y ver comentarios.</li>
 *   <li>{@link Role#USUARIO} — solo puede leer y listar publicaciones.</li>
 * </ul>
 */
@Entity
@Table(name = "usuarios")
public class Usuario implements UserDetails {

	/** Identificador de versión para la serialización de la clase. */
	private static final long serialVersionUID = 1L;

	/**
	 * Identificador único del usuario, generado automáticamente por la base de datos.
	 */
	private @Id @GeneratedValue(strategy = GenerationType.IDENTITY) Long id;

	/** Nombre de usuario único en el sistema. */
	@Column(unique = true)
	private String username;

	/** Contraseña del usuario almacenada de forma encriptada. */
	private String password;

	/**
	 * Rol del usuario que determina sus permisos en el sistema.
	 * Se almacena como texto en la base de datos.
	 */
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Role role;

	/** Indica si la cuenta del usuario no ha expirado. */
	private boolean accountNonExpired;

	/** Indica si la cuenta del usuario no está bloqueada. */
	private boolean accountNonLocked;

	/** Indica si las credenciales del usuario no han expirado. */
	private boolean credentialsNonExpired;

	/** Indica si la cuenta del usuario está habilitada. */
	private boolean enabled;

	/**
	 * Enumeración de los roles disponibles en el sistema del periódico.
	 */
	public enum Role {
		/** Rol con acceso total: puede gestionar usuarios, publicaciones y comentarios. */
		ADMIN,
		/** Rol con acceso a creación y edición de publicaciones. */
		EDITOR,
		/** Rol con acceso a creación y visualización de comentarios. */
		COMENTADOR,
		/** Rol con acceso de solo lectura a publicaciones. */
		USUARIO
	}

	/**
	 * Constructor vacío que inicializa la cuenta como activa y con rol {@link Role#ADMIN}.
	 * Requerido por JPA para la instanciación de entidades.
	 */
	public Usuario() {
		this.accountNonExpired = true;
		this.accountNonLocked = true;
		this.credentialsNonExpired = true;
		this.enabled = true;
		this.role = Role.USUARIO;
	}

	/**
	 * Constructor con credenciales y rol específico.
	 * <p>
	 * Llama al constructor vacío mediante {@code this()} para garantizar que
	 * los campos de estado de la cuenta se inicialicen correctamente.
	 * </p>
	 *
	 * @param username nombre de usuario único en el sistema
	 * @param password contraseña del usuario (debe enviarse ya encriptada)
	 * @param role     rol del usuario que determina sus permisos
	 */
	public Usuario(String username, String password, Role role) {
		this();
		this.username = username;
		this.password = password;
		this.role = role;
	}

	/**
	 * Retorna la lista de autoridades (roles) del usuario para Spring Security.
	 * <p>
	 * El rol se formatea con el prefijo {@code ROLE_} requerido por Spring Security,
	 * por ejemplo {@code ROLE_ADMIN} o {@code ROLE_EDITOR}.
	 * </p>
	 *
	 * @return colección con la autoridad del usuario
	 */
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
	}

	/**
	 * Indica si la cuenta del usuario no ha expirado.
	 *
	 * @return {@code true} siempre, la cuenta nunca expira
	 */
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	/**
	 * Indica si la cuenta del usuario no está bloqueada.
	 *
	 * @return {@code true} siempre, la cuenta nunca se bloquea
	 */
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	/**
	 * Indica si las credenciales del usuario no han expirado.
	 *
	 * @return {@code true} siempre, las credenciales nunca expiran
	 */
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	/**
	 * Indica si la cuenta del usuario está habilitada.
	 *
	 * @return {@code true} siempre, la cuenta siempre está habilitada
	 */
	@Override
	public boolean isEnabled() {
		return true;
	}

	/**
	 * Obtiene la contraseña encriptada del usuario.
	 *
	 * @return contraseña encriptada del usuario
	 */
	@Override
	public @Nullable String getPassword() {
		return this.password;
	}

	/**
	 * Obtiene el nombre de usuario.
	 *
	 * @return nombre de usuario
	 */
	@Override
	public String getUsername() {
		return this.username;
	}

	/**
	 * Calcula el código hash del objeto basándose en todos sus campos.
	 *
	 * @return código hash del objeto
	 */
	@Override
	public int hashCode() {
		return Objects.hash(accountNonExpired, accountNonLocked, credentialsNonExpired, enabled, id, password, role,
				username);
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
		Usuario other = (Usuario) obj;
		return accountNonExpired == other.accountNonExpired && accountNonLocked == other.accountNonLocked
				&& credentialsNonExpired == other.credentialsNonExpired && enabled == other.enabled
				&& Objects.equals(id, other.id) && Objects.equals(password, other.password) && role == other.role
				&& Objects.equals(username, other.username);
	}

	/**
	 * Retorna una representación en cadena del objeto {@code Usuario}.
	 *
	 * @return cadena con todos los campos de la entidad
	 */
	@Override
	public String toString() {
		return "Usuario [id=" + id + ", username=" + username + ", password=" + password + ", role=" + role
				+ ", accountNonExpired=" + accountNonExpired + ", accountNonLocked=" + accountNonLocked
				+ ", credentialsNonExpired=" + credentialsNonExpired + ", enabled=" + enabled + "]";
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
	 * Obtiene el identificador de versión de serialización de la clase.
	 *
	 * @return valor del {@code serialVersionUID}
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
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
	 * Establece la contraseña del usuario.
	 *
	 * @param password contraseña encriptada del usuario
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * Establece si la cuenta del usuario ha expirado.
	 *
	 * @param accountNonExpired {@code true} si la cuenta no ha expirado
	 */
	public void setAccountNonExpired(boolean accountNonExpired) {
		this.accountNonExpired = accountNonExpired;
	}

	/**
	 * Establece si la cuenta del usuario está bloqueada.
	 *
	 * @param accountNonLocked {@code true} si la cuenta no está bloqueada
	 */
	public void setAccountNonLocked(boolean accountNonLocked) {
		this.accountNonLocked = accountNonLocked;
	}

	/**
	 * Establece si las credenciales del usuario han expirado.
	 *
	 * @param credentialsNonExpired {@code true} si las credenciales no han expirado
	 */
	public void setCredentialsNonExpired(boolean credentialsNonExpired) {
		this.credentialsNonExpired = credentialsNonExpired;
	}

	/**
	 * Establece si la cuenta del usuario está habilitada.
	 *
	 * @param enabled {@code true} si la cuenta está habilitada
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
}