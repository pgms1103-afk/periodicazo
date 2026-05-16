package co.edu.unbosque.periodicazo;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * Inicializador del servlet para despliegue en servidor de aplicaciones externo.
 * <p>
 * Extiende {@link SpringBootServletInitializer} para permitir que la aplicación
 * Spring Boot pueda desplegarse como un archivo WAR en servidores como Tomcat,
 * WildFly o cualquier contenedor de servlets compatible con Jakarta EE,
 * en lugar de usar el servidor embebido por defecto.
 * </p>
 */
public class ServletInitializer extends SpringBootServletInitializer {

	/**
	 * Configura la aplicación Spring Boot para su despliegue externo.
	 * <p>
	 * Indica a Spring cuál es la clase principal de la aplicación
	 * ({@link PeriodicazoApplication}) para que pueda inicializarse
	 * correctamente al desplegarse en un servidor externo.
	 * </p>
	 *
	 * @param application constructor de la aplicación Spring proporcionado por el contenedor
	 * @return el {@link SpringApplicationBuilder} configurado con la clase fuente principal
	 */
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(PeriodicazoApplication.class);
	}
}