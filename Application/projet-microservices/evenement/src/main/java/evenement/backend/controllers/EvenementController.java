package evenement.backend.controllers;

import evenement.backend.modele.entities.Evenement;
import evenement.backend.facade.EvenementService;
import evenement.backend.modele.exception.EvenementIntrouvableException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.Collection;

/**
 * Controleur du web service Evenement
 * @version 1.0
 */

@RestController
@RequestMapping(path = "/", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_XML_VALUE})
public class EvenementController {

    private static Logger logger = LoggerFactory.getLogger(EvenementController.class);

    @Autowired
    private EvenementService facade;

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
     * Permet d'ajouter un evenement
     *
     * @param evenement
     * @return resultat de la requete
     */
    @PostMapping(value = "/", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Evenement> creation(@Valid @RequestBody(required = true) Evenement evenement) {
        logger.info("Tentative de creation d'evenement");
        Evenement evenement1 = this.facade.creerEvenement(evenement.getNom(), evenement.getIdCreateur(), evenement.getDateEvenement(), evenement.getLieu());
        logger.info("Creation d'evenement reussie");
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("{id}")
                .buildAndExpand(evenement1.getId()).toUri();
        return ResponseEntity.created(location).body(evenement1);
    }

    /**
     * Permet de rechercher un evenement par son id
     *
     * @param idEvenement id de l'evenement
     * @return resultat de la requete
     */
    @GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Evenement> rechercherById(@PathVariable("id") Long idEvenement) {
        logger.info("Tentative de recuperation de l'evenement : " + idEvenement);
        try{
            Evenement evenement = this.facade.getEvenementById(idEvenement);
            logger.info("Recuperation de l'evenement : " + idEvenement + " reussie");
            return ResponseEntity.ok(evenement);
        }catch (EvenementIntrouvableException e){
            logger.error("Recuperation de l'evenement : " + idEvenement + " echouee -> " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * Permet de modifier un evenement
     *
     * @param evenement evenement avec modification
     * @return resultat de la requete
     */
    @PutMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Evenement> modificationEvenement(@RequestBody Evenement evenement) {
        Evenement event = this.facade.updateEvenement(evenement);
        return ResponseEntity.ok(event);
    }

    /**
     * Permet de supprimer un evenement
     *
     * @param idEvenement numéro de l'évenement
     * @return resultat de la requete
     */
    @DeleteMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Evenement> suppressionById(@PathVariable("id") Long idEvenement) {
        this.facade.deleteEvenement(idEvenement);
        return ResponseEntity.ok().build();
    }

    /**
     * Récupère tous les utilisateurs d'un evenement
     *
     * @param idEvenement numéro de l'évenement
     * @return resultat de la requete
     */
    @GetMapping(value = "/{id}/utilisateur", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity rechercherUtilisateurByEvenement(@PathVariable("id") Long idEvenement){
        Collection<Long> participants = this.facade.getUtilisateurByEvenement(idEvenement);
        return ResponseEntity.ok(participants);
    }

    /**
     * Ajouter un utilisateur a un evenement
     *
     * @param idEvenement id de l'evenement
     * @return resultat de la requete
     */
    @PostMapping(value = "/{id}/utilisateur", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity ajoutUtilisateur(
            @PathVariable("id") Long idEvenement,
            @Valid @RequestBody Long idUtilisateur){
        logger.info("Tentative d'inscription de l'utilisateur : " + idUtilisateur + " a l'evenement : " + idEvenement);
        try {
            Evenement evenement = this.facade.getEvenementById(idEvenement);
            evenement.ajouterParticipant(idUtilisateur);
            this.facade.updateEvenement(evenement);
            logger.info("Ajout de l'utilisateur : "+idUtilisateur+" a l'evenement : " + idEvenement + " reussie");
            return ResponseEntity.ok(evenement);
        }catch (EvenementIntrouvableException e){
            logger.info("Inscription a l'evenement : " + idEvenement + " echouee -> "+e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
