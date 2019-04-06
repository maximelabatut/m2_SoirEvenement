package gateway.frontend.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Utilisateur {

    private String id;
    private String pseudo;
    private String nom;
    private String prenom;
    private String adresse;
    private String ville;
    private String codePostal;
    private String email;
    private String dateInscription;
    private String dateNaiss;
    private List<Utilisateur> amis;
    private List<InvitationAmi> invitationsAmis;
    private List<InvitationSoiree> invitationsSoirees;

    public Utilisateur() {
    }

    public Utilisateur(String id, String pseudo, String nom, String prenom, String adresse, String ville, String codePostal, String email, String dateInscription, String dateNaiss, List<Utilisateur> amis) {
        this.id = id;
        this.pseudo = pseudo;
        this.nom = nom;
        this.prenom = prenom;
        this.adresse = adresse;
        this.ville = ville;
        this.codePostal = codePostal;
        this.email = email;
        this.dateInscription = dateInscription;
        this.dateNaiss = dateNaiss;
        this.amis = amis;
        this.invitationsAmis = new ArrayList<>();
        this.invitationsSoirees = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public String getCodePostal() {
        return codePostal;
    }

    public void setCodePostal(String codePostal) {
        this.codePostal = codePostal;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDateInscription() {
        return dateInscription;
    }

    public void setDateInscription(String dateInscription) {
        this.dateInscription = dateInscription;
    }

    public String getDateNaiss() {
        return dateNaiss;
    }

    public void setDateNaiss(String dateNaiss) {
        this.dateNaiss = dateNaiss;
    }

    public List<Utilisateur> getAmis() {
        return amis;
    }

    public void setAmis(List<Utilisateur> amis) {
        this.amis = amis;
    }

    public List<InvitationAmi> getInvitationsAmis() {
        return invitationsAmis;
    }

    public void setInvitationsAmis(List<InvitationAmi> invitationsAmis) {
        this.invitationsAmis = invitationsAmis;
    }

    public List<InvitationSoiree> getInvitationsSoirees() {
        return invitationsSoirees;
    }

    public void setInvitationsSoirees(List<InvitationSoiree> invitationsSoirees) {
        this.invitationsSoirees = invitationsSoirees;
    }
}
