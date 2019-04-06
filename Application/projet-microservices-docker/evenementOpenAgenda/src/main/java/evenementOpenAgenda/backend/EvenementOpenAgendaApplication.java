package evenementOpenAgenda.backend;

import evenementOpenAgenda.backend.facade.EvenementOpenAgendaService;
import evenementOpenAgenda.backend.modele.entities.EvenementOpenAgenda;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableDiscoveryClient
public class EvenementOpenAgendaApplication {

	public static void main(String[] args) {
		SpringApplication.run(EvenementOpenAgendaApplication.class, args);
	}

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}

}
