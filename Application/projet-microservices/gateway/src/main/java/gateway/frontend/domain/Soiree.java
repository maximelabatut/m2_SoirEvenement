package gateway.frontend.domain;

import java.util.List;

public class Soiree {
    private String id;
    private String nom;
    private String utilisateur;
    private List<String> evenements;
    private List<String> evenementsOpenAgenda;
    private List<String> participants;
    private Long nbPlaces;
    private String dateSoiree;
    private String heureDebut;

    public Soiree() {

    }

    public Soiree(String id, String nom, String utilisateur, List<String> evenements, List<String> evenementsOpenAgenda, List<String> participants, Long nbPlaces, String dateSoiree, String heureDebut) {
        this.id = id;
        this.nom = nom;
        this.utilisateur = utilisateur;
        this.evenements = evenements;
        this.evenementsOpenAgenda = evenementsOpenAgenda;
        this.participants = participants;
        this.nbPlaces = nbPlaces;
        this.dateSoiree = dateSoiree;
        this.heureDebut = heureDebut;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(String utilisateur) {
        this.utilisateur = utilisateur;
    }

    public List<String> getEvenements() {
        return evenements;
    }

    public void setEvenements(List<String> evenements) {
        this.evenements = evenements;
    }

    public List<String> getEvenementsOpenAgenda() {
        return evenementsOpenAgenda;
    }

    public void setEvenementsOpenAgenda(List<String> evenementsOpenAgenda) {
        this.evenementsOpenAgenda = evenementsOpenAgenda;
    }

    public List<String> getParticipants() {
        return participants;
    }

    public void setParticipants(List<String> participants) {
        this.participants = participants;
    }

    public Long getNbPlaces() {
        return nbPlaces;
    }

    public void setNbPlaces(Long nbPlaces) {
        this.nbPlaces = nbPlaces;
    }

    public String getDateSoiree() {
        return dateSoiree;
    }

    public void setDateSoiree(String dateSoiree) {
        this.dateSoiree = dateSoiree;
    }

    public String getHeureDebut() {
        return heureDebut;
    }

    public void setHeureDebut(String heureDebut) {
        this.heureDebut = heureDebut;
    }
}
