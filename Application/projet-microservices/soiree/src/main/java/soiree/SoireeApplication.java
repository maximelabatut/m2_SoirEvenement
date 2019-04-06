package soiree;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import soiree.backend.facade.SoireeService;
import soiree.backend.modele.entities.Soiree;

import java.time.LocalDate;

@SpringBootApplication
public class SoireeApplication {

	public static void main(String[] args) {
		SpringApplication.run(SoireeApplication.class, args);
	}

}
