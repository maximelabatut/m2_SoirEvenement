package evenementOpenAgenda.backend.controllers;

import evenementOpenAgenda.backend.facade.EvenementOpenAgendaService;
import evenementOpenAgenda.backend.modele.entities.EvenementOpenAgenda;
import evenementOpenAgenda.backend.modele.exception.EvenementOpenAgendaIntrouvableException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@RequestMapping(path = "/", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_XML_VALUE})
public class EvenementOpenAgendaController {

    private static Logger logger = LoggerFactory.getLogger(EvenementOpenAgendaController.class);

    private final EvenementOpenAgendaService facade;

    @Autowired
    public EvenementOpenAgendaController(EvenementOpenAgendaService facade) {
        this.facade = facade;
    }

    /**
     * Récupère tous les évenements
     *
     * @return tous les évenements
     */
    @GetMapping(value = "/", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity getAllEvenement() {
        logger.info("Recuperation de tous les evenements");
        return ResponseEntity.status(HttpStatus.OK).body(this.facade.getAllEvenement());
    }

    /**
     * Permet de rechercher un evenement par son id
     *
     * @param idEvenement id de l'evenement
     * @return resultat de la requete
     */
    @GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<EvenementOpenAgenda> rechercherById(@PathVariable("id") String idEvenement) {
        logger.info("Tentative de recuperation de l'evenement : " + idEvenement);
        try{
            EvenementOpenAgenda evenementOpenAgenda = this.facade.getEvenementById(idEvenement);
            logger.info("Recuperation de l'evenement : " + idEvenement + " reussie");
            return ResponseEntity.ok(evenementOpenAgenda);
        }catch (EvenementOpenAgendaIntrouvableException e){
            logger.error("Recuperation de l'evenement : " + idEvenement + " echouee -> " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * Récupère tous les utilisateurs d'un evenement
     *
     * @param idEvenement numéro de l'évenement
     * @return resultat de la requete
     */
    @GetMapping(value = "/{id}/utilisateur", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity rechercherUtilisateurByEvenement(@PathVariable("id") String idEvenement){
        logger.info("Tentative de recuperation des utilisateurs de l'evenement : " + idEvenement);
        try{
            Collection<Long> openAgendaCollection = this.facade.getUtilisateurByEvenement(idEvenement);
            logger.info("Recuperation des utilisateurs de l'evenement : " + idEvenement + " reussie");
            return ResponseEntity.ok(openAgendaCollection);
        }catch (EvenementOpenAgendaIntrouvableException e){
            logger.error("Recuperation des utilisateurs de l'evenement : " + idEvenement + " echouee -> "+e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * Ajouter un utilisateur a un evenement
     *
     * @param idEvenement id de l'evenement
     * @param idUtilisateur id de l'utilisateur
     * @return resultat de la requete
     */
    @PostMapping(value = "/{id}/utilisateur", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity ajoutUtilisateur(
            @PathVariable("id") String idEvenement,
            @Valid @RequestBody Long idUtilisateur){
        logger.info("Tentative d'inscription de l'utilisateur : " +  idUtilisateur + " a l'evenement : " + idEvenement);
        try {
            EvenementOpenAgenda evenement = this.facade.getEvenementById(idEvenement);
            evenement.ajouterParticipant(idUtilisateur);
            this.facade.updateEvenement(evenement);
            logger.info("Inscription a l'evenement : " + idEvenement + " reussie");
            return ResponseEntity.ok(evenement);
        }catch (EvenementOpenAgendaIntrouvableException e){
            logger.info("Inscription a l'evenement : " + idEvenement + " echouee -> "+e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
