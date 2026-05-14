package co.edu.unbosque.periodicazo.dto;

import java.util.Objects;

import co.edu.unbosque.periodicazo.entity.Usuario.Role;

public class UsuarioDTO {
	
	private Long id;
	private String username;
	private String password;
	private Role role;
	
	public UsuarioDTO() {
		// TODO Auto-generated constructor stub
	}

	public UsuarioDTO(Long id, String username, String password, Role role) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
		this.role = role;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	@Override
	public String toString() {
		return "UsuarioDTO [id=" + id + ", username=" + username + ", password=" + password + ", role=" + role + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, password, role, username);
	}

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
