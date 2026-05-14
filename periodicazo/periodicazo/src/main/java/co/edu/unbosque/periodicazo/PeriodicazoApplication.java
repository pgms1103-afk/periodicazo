package co.edu.unbosque.periodicazo;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class PeriodicazoApplication {

	public static void main(String[] args) {
		SpringApplication.run(PeriodicazoApplication.class, args);
	}
	
	  @Bean
	  public ModelMapper getModelMapper() {
	    return new ModelMapper();
	  }

}
