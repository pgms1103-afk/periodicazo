package co.edu.unbosque.periodicazo;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * Clase principal de la aplicación Periodicazo.
 * <p>
 * Punto de entrada de la aplicación Spring Boot. Se encarga de iniciar
 * el contexto de la aplicación y registrar los beans globales necesarios.
 * </p>
 */
@SpringBootApplication
public class PeriodicazoApplication {

	/**
	 * Método principal que inicia la aplicación Spring Boot.
	 *
	 * @param args argumentos de línea de comandos pasados al iniciar la aplicación
	 */
	public static void main(String[] args) {
		SpringApplication.run(PeriodicazoApplication.class, args);
	}

	/**
	 * Registra un bean de {@link ModelMapper} en el contexto de Spring.
	 * <p>
	 * ModelMapper se utiliza para convertir objetos entre entidades y DTOs
	 * a lo largo de toda la aplicación.
	 * </p>
	 *
	 * @return una instancia de {@link ModelMapper}
	 */
	@Bean
	public ModelMapper getModelMapper() {
		return new ModelMapper();
	}
}