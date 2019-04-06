package evenement;

import evenement.backend.facade.EvenementService;
import evenement.backend.modele.entities.Evenement;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;

@SpringBootApplication
public class EvenementApplication {

	public static void main(String[] args) {
		SpringApplication.run(EvenementApplication.class, args);
	}

}