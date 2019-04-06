package utilisateur.backend.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import utilisateur.backend.domain.Invitation;
import utilisateur.backend.facade.UtilisateurService;
import utilisateur.backend.modele.entities.InvitationAmi;
import utilisateur.backend.modele.entities.InvitationSoiree;
import utilisateur.backend.modele.entities.Utilisateur;
import utilisateur.backend.modele.exceptions.PseudoDejaPrisException;
import utilisateur.backend.modele.exceptions.UtilisateurNotFoundException;

import javax.validation.Valid;
import java.net.URI;
import java.util.Collection;

/**
 * Controleur du web service Utilisateur
 * @version 1.0
 */

@RestController
@RequestMapping(path = "/", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_XML_VALUE})
public class UtilisateurController {

    private static Logger logger = LoggerFactory.getLogger(UtilisateurController.class);

    private final UtilisateurService facade;

    @Autowired
    public UtilisateurController(UtilisateurService facade) {
        this.facade = facade;
    }

    /**
     * Permet de recuperer tous les utilisateurs
     *
     * @return les utilisateurs
     */
    @GetMapping(value = "/", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity getAllUsers() {
        logger.info("Recuperation de tous les utilisateurs");
        return ResponseEntity.ok(this.facade.getAllUtilisateurs());
    }

    /**
     * Permet d'inscrire un utilisateur
     *
     * @param utilisateur Utilisateur a inscrire
     * @return resultat de la requete
     */
    @PostMapping(value = "/", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity inscription(
            @Valid @RequestBody Utilisateur utilisateur) {
        logger.info("Tentative d'inscription de :" + utilisateur.getPseudo());
        try {
            Utilisateur utilisateur1 = this.facade.inscription(utilisateur.getPseudo());
            logger.info("Inscription de : " + utilisateur.getPseudo() + " reussie");
            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest().path("{idUtilisateur}")
                    .buildAndExpand(utilisateur1.getId()).toUri();
            return ResponseEntity.created(location).body(utilisateur1);
        }catch (PseudoDejaPrisException e){
            logger.error("Inscription de : " + utilisateur.getPseudo() + " echouee -> "+e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    /**
     * Permet de rechercher un utilisateur par son pseudo
     *
     * @param idUtilisateur id de l'utilisateur
     * @return resultat de la requete
     */
    @GetMapping(value = "/{idUtilisateur}", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity findById(
            @PathVariable("idUtilisateur") Long idUtilisateur) {
        logger.info("Tentative de recuperation de l'utilisateur : " + idUtilisateur);
        try{
            Utilisateur utilisateur = this.facade.getUtilisateurById(idUtilisateur);
            logger.info("Recuperation de : " + idUtilisateur + " reussie");
            return ResponseEntity.ok(utilisateur);
        }catch (UtilisateurNotFoundException e){
            logger.error("Recuperation de : " + idUtilisateur + " echouee -> " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    /**
     * Permet de modifier un utilisateur
     *
     * @param idUtilisateur id de l'utilisateur
     * @param utilisateur utilisateur modifie
     * @return resultat de la requete
     */
    @PutMapping(value = "/{idUtilisateur}", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Utilisateur> editUser(
            @PathVariable("idUtilisateur") Long idUtilisateur,
            @Valid @RequestBody Utilisateur utilisateur) {
        logger.info("Tentative de modification de l'utilisateur : " + utilisateur.getPseudo());
        utilisateur.setId(idUtilisateur);
        try {
            this.facade.getUtilisateurById(idUtilisateur);
            this.facade.updateUtilisateur(utilisateur);
            logger.info("Modification de l'utilisateur : " + utilisateur.getPseudo() + " reussie");
            return ResponseEntity.ok().build();
        }catch (UtilisateurNotFoundException e){
            logger.info("Modification de l'utilisateur : " + utilisateur.getPseudo() + " echouee -> "+e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * Permet de rechercher les amis d'un utilisateur
     *
     * @param idUtilisateur id de l'utilisateur
     * @return resultat de la requete
     */
    @GetMapping(value = "/{idUtilisateur}/amis", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity findAmisById(
            @PathVariable("idUtilisateur") Long idUtilisateur) {
        logger.info("Tentative de recuperation des amis de l'utilisateur : " + idUtilisateur);
        try{
            Collection<Utilisateur> utilisateurs = this.facade.getUtilisateurById(idUtilisateur).getAmis();
            logger.info("Recuperation des amis de l'utilisateur : " + idUtilisateur + " réussie");
            return ResponseEntity.ok(utilisateurs);
        }catch (UtilisateurNotFoundException e){
            logger.error("Recuperation des amis de l'utilisateur : " + idUtilisateur + " echouee -> " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * Permet de recuperer les invitations d'un utilisateur
     *
     * @param idUtilisateur id de l'utilisateur
     * @return resultat de la requete
     */
    @GetMapping(value = "/{idUtilisateur}/invitationsAmis", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity getInvitationAmi(@PathVariable("idUtilisateur") Long idUtilisateur) {
        logger.info("Tentative de recuperation des invitations d'amis  de : " + idUtilisateur);
        try {
            Utilisateur utilisateur = this.facade.getUtilisateurById(idUtilisateur);
            logger.info("Recuperation des invitations d'amis de l'utilisateur : " + idUtilisateur + " réussie");
            return ResponseEntity.ok(utilisateur.getInvitationsAmis());
        }catch (UtilisateurNotFoundException e){
            logger.info("Recuperation des invitations d'amis de l'utilisateur : " + idUtilisateur + " echouee -> "+e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * Permet d'ajouter une invitation d'ami
     *
     * @param invitation Invitation
     */
    @PostMapping(value = "/{idUtilisateur}/invitationsAmis", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity addInvitationAmi(
            @PathVariable("idUtilisateur") Long idUtilisateur,
            @Valid @RequestBody InvitationAmi invitation) {
        logger.info("Tentative d'ajout d'invitation d'amis : " + invitation.getIdUtilisateurInvitant() + " et " + invitation.getIdUtilisateurInvite());
        InvitationAmi invitationAmi = this.facade.ajouterInvitationAmi(invitation.getIdUtilisateurInvite(), invitation.getIdUtilisateurInvitant());
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("{idInvitation}")
                .buildAndExpand(invitationAmi.getId()).toUri();
        return ResponseEntity.created(location).body(invitationAmi);
    }

    /**
     * Permet d'accepter une invitation d'ami
     *
     * @param idUtilisateur idUtilisateur
     * @param idInvitation idInvitation
     */
    @PutMapping(value = "/{idUtilisateur}/invitationsAmis/{idInvitation}", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity acceptInvitationAmi(
            @PathVariable("idUtilisateur") Long idUtilisateur,
            @PathVariable("idInvitation") Long idInvitation) {
        logger.info("Tentative d'acceptation d'invitation d'amis : " + idInvitation + " de " + idUtilisateur);
        this.facade.accepterInvitationAmi(idUtilisateur,idInvitation);
        return ResponseEntity.ok().build();
    }

    /**
     * Permet de supprimer une invitation d'ami
     *
     * @param idUtilisateur idUtilisateur
     * @param idInvitation idInvitation
     */
    @DeleteMapping(value = "/{idUtilisateur}/invitationsAmis/{idInvitation}", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity deleteInvitationAmi(
            @PathVariable("idUtilisateur") Long idUtilisateur,
            @PathVariable("idInvitation") Long idInvitation) {
        logger.info("Tentative de refus d'invitation d'amis : " + idInvitation + " de " + idUtilisateur);
        this.facade.supprimerInvitationAmi(idUtilisateur,idInvitation);
        return ResponseEntity.ok().build();
    }

    /**
     * Permet de recuperer les invitations de soirees d'un utilisateur
     *
     * @param idUtilisateur id de l'utilisateur
     * @return resultat de la requete
     */
    @GetMapping(value = "/{idUtilisateur}/invitationsSoirees", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity getInvitationSoiree(@PathVariable("idUtilisateur") Long idUtilisateur) {
        logger.info("Tentative de recuperation des invitations de soirees  de : " + idUtilisateur);
        try {
            Utilisateur utilisateur = this.facade.getUtilisateurById(idUtilisateur);
            logger.info("Recuperation des invitations de soirees de l'utilisateur : " + idUtilisateur + " réussie");
            return ResponseEntity.ok(utilisateur.getInvitationsSoirees());
        }catch (UtilisateurNotFoundException e){
            logger.info("Recuperation des invitations de soirees de l'utilisateur : " + idUtilisateur + " echouee -> "+e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * Permet d'ajouter une invitation d'ami
     *
     * @param idUtilisateur Id de l'utilisateur invite
     * @param invitation Invitation
     */
    @PostMapping(value = "/{idUtilisateur}/invitationsSoirees", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity addInvitationAmi(
            @PathVariable("idUtilisateur") Long idUtilisateur,
            @RequestBody InvitationSoiree invitation) {
        logger.info("Tentative d'ajout d'invitation de soiree  : " + invitation.getIdSoiree() + " a l'utilisateur " + invitation.getUtilisateurInvite());
        InvitationSoiree invitationSoiree = this.facade.ajouterInvitationSoiree(invitation.getUtilisateurInvite(), invitation.getIdSoiree());
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("{idInvitation}")
                .buildAndExpand(invitationSoiree.getId()).toUri();
        return ResponseEntity.created(location).body(invitationSoiree);
    }

    /**
     * Permet d'accepter une invitation de soiree
     *
     * @param idUtilisateur idUtilisateur
     * @param idInvitation idInvitation
     */
    @PutMapping(value = "/{idUtilisateur}/invitationsSoirees/{idInvitation}", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity acceptInvitationSoiree(
            @PathVariable("idUtilisateur") Long idUtilisateur,
            @PathVariable("idInvitation") Long idInvitation) {
        logger.info("Tentative d'acceptation d'invitation soirée : " + idInvitation + " de " + idUtilisateur);
        this.facade.accepterInvitationSoiree(idUtilisateur,idInvitation);
        return ResponseEntity.ok().build();
    }

    /**
     * Permet de supprimer une invitation de soiree
     *
     * @param idUtilisateur idUtilisateur
     * @param idInvitation idInvitation
     */
    @DeleteMapping(value = "/{idUtilisateur}/invitationsSoirees/{idInvitation}", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity deleteInvitationSoiree(
            @PathVariable("idUtilisateur") Long idUtilisateur,
            @PathVariable("idInvitation") Long idInvitation) {
        logger.info("Tentative de suppression d'invitation à la soirée : " + idInvitation + " de " + idUtilisateur);
        this.facade.supprimerInvitationSoiree(idUtilisateur,idInvitation);
        return ResponseEntity.ok().build();
    }

    /**
     * Permet d'ajouter un utilisateur à sa liste d'ami
     *
     * @param invitation Invitation comportant les pseudos
     */
    @PostMapping(value = "/{id}/amis", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Utilisateur> addUsertoFriend(@Valid @RequestBody Invitation invitation) {
        logger.info("Tentative d'ajout d'amis : " + invitation.getIdPseudoInvitant() + " et " + invitation.getIdPseudoInvite());
        this.facade.ajouterAmi(invitation.getIdPseudoInvitant(), invitation.getIdPseudoInvite());
        logger.info("Ajout d'amis : " + invitation.getIdPseudoInvitant() + " et " + invitation.getIdPseudoInvite() + " reussi");
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * Permet d'ajouter un utilisateur à sa liste d'ami
     *
     * @param invitation Invitation comportant les pseudos
     */
    @PutMapping(value = "/{id}/amis", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Utilisateur> removeUsertoFriend(
            @Valid @RequestBody Invitation invitation){
        logger.info("Tentative de suppression d'amis : " + invitation.getIdPseudoInvitant() + " et " + invitation.getIdPseudoInvite());
        this.facade.enleverAmi(invitation.getIdPseudoInvitant(), invitation.getIdPseudoInvite());
        logger.info("Suppression d'amis : " + invitation.getIdPseudoInvitant() + " et " + invitation.getIdPseudoInvite() + " reussi");
        return ResponseEntity.ok().build();
    }

    /**
     * Permet d'ajouter un utilisateur à sa liste d'ami
     *
     * @param idUtilisateur1 Utilisateur qui ajoute un amis
     * @param idUtilisateur2 Utilisateur qui est ajouté
     */
    @PutMapping(value = "/{idUtilisateur}/amis/{idAmis}")
    public ResponseEntity<Utilisateur> addFriend(
            @PathVariable("idUtilisateur") Long idUtilisateur1,
            @PathVariable("idAmis") Long idUtilisateur2){

        logger.info("JE SUIS LAAAAAAA !!!!!!!");
        logger.info("Tentative d'ajout d'amis : " + idUtilisateur2);
        this.facade.ajouterAmi(idUtilisateur1, idUtilisateur2);
        return ResponseEntity.ok().build();
    }

}
