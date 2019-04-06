package soiree.backend.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import soiree.backend.modele.entities.Soiree;
import soiree.backend.facade.SoireeService;
import soiree.backend.modele.entities.Soiree;
import soiree.backend.modele.exceptions.SoireeNotFoundException;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

/**
 * Controleur du web service Soiree
 * @version 1.0
 */

@RestController
@RequestMapping(path = "/", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_XML_VALUE})
public class SoireeController {

    private static Logger logger = LoggerFactory.getLogger(SoireeController.class);

    private final SoireeService facade;

    @Autowired
    public SoireeController(SoireeService facade) {
        this.facade = facade;
    }

    /**
     * Permet de recuperer toutes les soirees
     * @return les soirees
     */
    @GetMapping(value = "/", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity getAllSoirees() {
        logger.info("Recuperation de toutes les soirees");
        return ResponseEntity.ok(this.facade.getAllSoirees());
    }

    /**
     * Permet de creer une soiree
     * @param soiree Soiree a creer
     * @return resultat de la requete
     */
    @PostMapping(value = "/", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity creerSoiree(@RequestBody Soiree soiree) {
        Soiree soireeCree = this.facade.creerSoiree(soiree);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("{id}")
                .buildAndExpand(soireeCree.getId()).toUri();
        return ResponseEntity.created(location).body(soireeCree);
    }

    /**
     * Permet de rechercher une soirée par son id
     *
     * @param idSoiree id de la soirée
     * @return resultat de la requete
     */
    @GetMapping(value = "/{idSoiree}", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Soiree> rechercherById(@PathVariable("idSoiree") Long idSoiree) {
        logger.info("Tentative de recuperation de la soirée : " + idSoiree);
        try{
            Soiree soiree = this.facade.getSoireeById(idSoiree);
            logger.info("Recuperation de la soirée : " + idSoiree + " reussie");
            return ResponseEntity.ok(soiree);
        }catch (SoireeNotFoundException e){
            logger.error("Recuperation de la soirée : " + idSoiree + " echouee -> " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * Permet de modifier une soirée
     *
     * @param soiree avec modification
     * @return resultat de la requete
     */
    @PutMapping(value = "/{idSoiree}", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Soiree> modificationSoiree(@RequestBody Soiree soiree) {
        Soiree uneSoiree = this.facade.updateSoiree(soiree);
        return ResponseEntity.ok(uneSoiree);
    }

    /**
     * Permet de rechercher les évenements par son idSoiree
     *
     * @param idSoiree id de la soirée
     * @return resultat de la requete
     */
    @GetMapping(value = "/{idSoiree}/evenements", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<List<String>> rechercherEvenements(@PathVariable("idSoiree") Long idSoiree) {
        logger.info("Tentative de recuperation de la soirée : " + idSoiree);
        try{
            List<String> lesEvenements = this.facade.getSoireeById(idSoiree).getEvenements();
            logger.info("Recuperation des évenements de la soirée : " + lesEvenements.toString() + " reussie");
            return ResponseEntity.ok(lesEvenements);
        }catch (SoireeNotFoundException e){
            logger.error("Recuperation des évenements de la soirée : " + idSoiree + " echouee -> " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * Permet d'ajouter une soirée
     *
     * @param idSoiree id de la soirée
     * @param idEvenement id de l'evenement
     * @return resultat de la requete
     */
    @PostMapping(value = "/{idSoiree}/evenements", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Soiree> creationEvenement(
            @PathVariable("idSoiree") Long idSoiree,
            @Valid @RequestBody String idEvenement) {
        logger.info("Tentative de creation d'ajout de l'évenement à la soirée : " + idSoiree);
        this.facade.ajouterEvenement(idSoiree, idEvenement);
        logger.info("Ajout d'evenement reussie");
        return ResponseEntity.ok().build();
    }


    /**
     * Permet de rechercher les évenements open agenda par son idSoiree
     *
     * @param idSoiree id de la soirée
     * @return resultat de la requete
     */
    @GetMapping(value = "/{idSoiree}/evenementsOpenAgenda", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<List<String>> rechercherEvenementsOpenAgenda(@PathVariable("idSoiree") Long idSoiree) {
        logger.info("Tentative de recuperation de la soirée : " + idSoiree);
        try{
            List<String> lesEvenementsOpenAgenda = this.facade.getSoireeById(idSoiree).getEvenementsOpenAgenda();
            logger.info("Recuperation des évenements open agenda de la soirée : " + lesEvenementsOpenAgenda.toString() + " reussie");
            return ResponseEntity.ok(lesEvenementsOpenAgenda);
        }catch (SoireeNotFoundException e){
            logger.error("Recuperation des évenements de la soirée : " + idSoiree + " echouee -> " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * Permet d'ajouter une soirée
     *
     * @param idSoiree id de la soirée
     * @param uid id de l'evenementOpenAgenda
     * @return resultat de la requete
     */
    @PostMapping(value = "/{idSoiree}/evenementopenAgenda", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Soiree> creationEvenementopenAgenda(
            @PathVariable("idSoiree") Long idSoiree,
            @Valid @RequestBody String uid) {
        logger.info("Tentative de creation d'ajout de l'evenementOpenAgenda à la soirée : " + idSoiree);
        this.facade.ajouterEvenementOpenAgenda(idSoiree, uid);
        logger.info("Ajout d'evenementOpenAgenda reussie");
        return ResponseEntity.ok().build();
    }

    /**
     * Permet d'ajouter un participant à une soirée
     *
     * @param idSoiree Soiree
     * @param idUtilisateur participant
     */
    @PutMapping(value = "/{idSoiree}/utilisateur/{idUtilisateur}")
    public ResponseEntity<Soiree> addParticipant(
            @PathVariable("idSoiree") Long idSoiree,
            @PathVariable("idUtilisateur") Long idUtilisateur){
        logger.info("Tentative d'ajout du participant : " + idUtilisateur);
        Soiree laSoiree = this.facade.getSoireeById(idSoiree);
        laSoiree.getParticipants().add(idUtilisateur);
        this.facade.updateSoiree(laSoiree);
        return ResponseEntity.ok().build();
    }

}
