package co.edu.unbosque.periodicazo.security;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import co.edu.unbosque.periodicazo.entity.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.security.Key;

/**
 * Componente utilitario para la generación, validación y extracción de datos
 * de tokens JWT (JSON Web Token).
 * <p>
 * Se encarga de todo el ciclo de vida de los tokens JWT en la aplicación:
 * generarlos al autenticar un usuario, validarlos en cada solicitud entrante
 * y extraer información como el username, el rol y la fecha de expiración.
 * </p>
 * <p>
 * Los tokens generados tienen una validez de 24 horas y están firmados con
 * el algoritmo {@code HS256} usando la clave secreta definida en
 * {@code application.properties} bajo la propiedad {@code jwt.secret}.
 * </p>
 */
@Component
public class JwtUtil {

	/** Tiempo de validez del token JWT en milisegundos (24 horas). */
	private static final long JWT_TOKEN_VALIDITY = 24 * 60 * 60 * 1000;

	/**
	 * Clave secreta utilizada para firmar los tokens JWT.
	 * Se configura mediante la propiedad {@code jwt.secret} en
	 * {@code application.properties}. Si no se define, usa un valor por defecto.
	 */
	@Value("${jwt.secret:defaultSecretKeyWhichShouldBeAtLeast32CharactersLong}")
	private String secret;

	/**
	 * Convierte la clave secreta en un objeto {@link Key} compatible con HMAC-SHA
	 * para firmar y verificar los tokens JWT.
	 *
	 * @return clave de firma generada a partir del secreto configurado
	 */
	private Key getSigningKey() {
		byte[] keyBytes = secret.getBytes();
		return Keys.hmacShaKeyFor(keyBytes);
	}

	/**
	 * Extrae el nombre de usuario (subject) del token JWT.
	 *
	 * @param token token JWT del cual extraer el username
	 * @return nombre de usuario contenido en el token
	 */
	public String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	/**
	 * Extrae la fecha de expiración del token JWT.
	 *
	 * @param token token JWT del cual extraer la fecha de expiración
	 * @return fecha y hora en que el token expira
	 */
	public Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}

	/**
	 * Extrae el rol del usuario contenido en los claims del token JWT.
	 *
	 * @param token token JWT del cual extraer el rol
	 * @return nombre del rol del usuario (ej. ADMIN, EDITOR, USUARIO)
	 */
	public String extractRole(String token) {
		return extractClaim(token, claims -> claims.get("role", String.class));
	}

	/**
	 * Método genérico para extraer cualquier claim del token JWT
	 * mediante una función resolutora.
	 *
	 * @param token          token JWT del cual extraer el claim
	 * @param claimsResolver función que define qué claim extraer del objeto {@link Claims}
	 * @param <T>            tipo de dato del claim a extraer
	 * @return valor del claim extraído del token
	 */
	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}

	/**
	 * Extrae todos los claims del token JWT parseando y verificando su firma.
	 *
	 * @param token token JWT del cual extraer todos los claims
	 * @return objeto {@link Claims} con todos los claims contenidos en el token
	 */
	private Claims extractAllClaims(String token) {
		return Jwts.parserBuilder()
				.setSigningKey(getSigningKey())
				.build()
				.parseClaimsJws(token)
				.getBody();
	}

	/**
	 * Verifica si el token JWT ha expirado comparando su fecha de expiración
	 * con la fecha y hora actuales.
	 *
	 * @param token token JWT a verificar
	 * @return {@code true} si el token ya expiró, {@code false} si aún es válido
	 */
	private Boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}

	/**
	 * Genera un token JWT para un usuario autenticado.
	 * <p>
	 * Incluye en los claims las autoridades del usuario y, si es una instancia
	 * de {@link Usuario}, también incluye su rol explícitamente para facilitar
	 * su uso en el frontend sin necesidad de decodificar el token.
	 * </p>
	 *
	 * @param userDetails detalles del usuario autenticado para el cual generar el token
	 * @return token JWT firmado y listo para ser enviado al cliente
	 */
	public String generateToken(UserDetails userDetails) {
		Map<String, Object> claims = new HashMap<>();
		claims.put("authorities", userDetails.getAuthorities());

		if (userDetails instanceof Usuario) {
			Usuario user = (Usuario) userDetails;
			claims.put("role", user.getRole().name());
		}

		return createToken(claims, userDetails.getUsername());
	}

	/**
	 * Construye y firma el token JWT con los claims, el subject y las fechas
	 * de emisión y expiración.
	 *
	 * @param claims  mapa de claims adicionales a incluir en el token
	 * @param subject nombre de usuario que será el subject del token
	 * @return token JWT compacto y firmado como cadena de texto
	 */
	private String createToken(Map<String, Object> claims, String subject) {
		return Jwts.builder()
				.setClaims(claims)
				.setSubject(subject)
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY))
				.signWith(getSigningKey(), SignatureAlgorithm.HS256)
				.compact();
	}

	/**
	 * Valida un token JWT verificando que el username coincida con el del usuario
	 * y que el token no haya expirado.
	 *
	 * @param token       token JWT a validar
	 * @param userDetails detalles del usuario contra los cuales validar el token
	 * @return {@code true} si el token es válido para el usuario indicado,
	 *         {@code false} si el username no coincide o el token ha expirado
	 */
	public Boolean validateToken(String token, UserDetails userDetails) {
		final String username = extractUsername(token);
		return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}
}