package authentification.backend.controllers;

import authentification.backend.domain.Login;
import authentification.backend.facade.AuthentificationService;
import authentification.backend.modele.entities.User;
import authentification.backend.modele.exception.IdentifiantsIncorrectsException;
import authentification.backend.modele.exception.PseudoDejaPrisException;
import authentification.backend.modele.exception.UserIntrouvableException;
import io.jsonwebtoken.*;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import javax.websocket.server.PathParam;
import java.net.URI;
import java.util.Date;

/**
 * Controleur du web service Authentification
 * @version 1.0
 */

@RestController
@RequestMapping(value="/", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_XML_VALUE})
public class AuthentificationController {

    private static Logger logger = LoggerFactory.getLogger(AuthentificationController.class);

    private static final long EXPIRATION_TIME = 1000 * 120L;
    private static final String SECRET_KEY = "secret" ;
    private static final String TOKEN_PREFIX = "Bearer ";
    private static final String AUTHORIZATION = "Authorization";

    private final AuthentificationService service;

    @Autowired
    public AuthentificationController(AuthentificationService service) {
        this.service = service;
    }

    @PostMapping(value = "/connexion", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity connexion(@Valid @RequestBody Login login){
        logger.info("Tentative de connexion de : " + login.getPseudo());
        try {
            // Connexion de l'utilisateur sur la facade
            User user = this.service.connexion(login.getPseudo(), login.getPassword());

            // Ajout d'un message OK dans les logs
            logger.info("Connexion de : " + login.getPseudo() + " reussie");

            // Generation du token
            Claims claims = Jwts.claims().setSubject(login.getPseudo());
            claims.put("pseudo", login.getPseudo());
            String token = Jwts.builder()
                    .setClaims(claims)
                    .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                    .signWith(SignatureAlgorithm.HS512, SECRET_KEY.getBytes())
                    .compact();

            // Renvoi d'un code HTTP 200 avec le token dans l'entete de la requete
            return ResponseEntity.ok().header(AUTHORIZATION, TOKEN_PREFIX + token).body(user.getId());
        }catch (UserIntrouvableException e){
            // Ajout d'un message KO dans les logs
            logger.error("Connexion de : " + login.getPseudo() + " echouee -> " + e.getMessage());
            // Renvoi d'un code HTTP 404
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }catch (IdentifiantsIncorrectsException e){
            // Ajout d'un message KO dans les logs
            logger.error("Connexion de : " + login.getPseudo() + " echouee -> " + e.getMessage());
            // Renvoi d'un code HTTP 409
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @PostMapping(value = "/inscription", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity inscription(@Valid @RequestBody(required = true) Login login) throws Exception{
        logger.info("Tentative d'inscription de : " + login.getPseudo());
        try {
            // Inscription de l'utilisateur sur la facade
            User user = this.service.inscription(login.getPseudo(), login.getPassword());
            // Ajout d'un message OK dans les logs
            logger.info("Inscription de : " + login.getPseudo() + " reussie");
            ResponseEntity responseEntity = this.connexion(login);
            String token = responseEntity.getHeaders().get(AUTHORIZATION).toString();
            // Renvoi d'un code HTTP 201
            return ResponseEntity.status(HttpStatus.CREATED).header(AUTHORIZATION, TOKEN_PREFIX + token).body(user.getId());
        }catch (PseudoDejaPrisException e){
            // Ajout d'un message KO dans les logs
            logger.error("Inscription de : " + login.getPseudo() + " echouee -> " + e.getMessage());
            // Renvoi d'un code HTTP 409
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @DeleteMapping(value = "/connexion", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity deconnexion(@PathParam("pseudo") String pseudo){
        logger.info("Tentative de déconnexion de : " + pseudo);
        try {
            // Déconnexion de l'utilisateur sur la facade
            this.service.deconnexion(pseudo);
            // Ajout d'un message OK dans les logs
            logger.info("Déconnexion de : " + pseudo + " reussite");
            // Renvoi d'un code HTTP 200
            return ResponseEntity.ok().build();
        }catch (UserIntrouvableException e){
            // Ajout d'un message KO dans les logs
            logger.error("Utilisateur : " + pseudo + " introuvable -> " + e.getMessage());
            // Renvoi d'un code HTTP 404
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
