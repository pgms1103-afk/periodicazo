package co.edu.unbosque.periodicazo.configuration;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

/**
 * Clase de configuración para la documentación de la API con Swagger/OpenAPI.
 * <p>
 * Define el esquema de seguridad global de la documentación, indicando que
 * los endpoints protegidos requieren autenticación mediante un token JWT
 * enviado en el header {@code Authorization} con el esquema {@code Bearer}.
 * </p>
 * <p>
 * Esta configuración permite que el botón "Authorize" aparezca en la interfaz
 * de Swagger UI ({@code /swagger-ui.html}), donde el usuario puede ingresar
 * su token JWT para probar los endpoints protegidos directamente desde la documentación.
 * </p>
 */
@Configuration
@SecurityScheme(
    name = "bearerAuth",
    type = SecuritySchemeType.HTTP,
    scheme = "bearer",
    bearerFormat = "JWT"
)
public class SwaggerConfig {
}