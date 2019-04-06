package gateway.frontend.domain;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

public class Evenement {
    private String id;
    private String idCreateur;
    private String nom;
    private String lieu;
    private String dateEvenement;
    private List<Long> participants;

    public Evenement(String id, String idCreateur,String nom, String lieu, String dateEvenement, List<Long> participants) {
        this.id = id;
        this.idCreateur = idCreateur;
        this.nom = nom;
        this.lieu = lieu;
        this.dateEvenement = dateEvenement;
        this.participants = participants;
    }

    public String getIdCreateur() {
        return idCreateur;
    }

    public void setIdCreateur(String idCreateur) {
        this.idCreateur = idCreateur;
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

    public String getLieu() {
        return lieu;
    }

    public void setLieu(String lieu) {
        this.lieu = lieu;
    }

    public String getDateEvenement() {
        return dateEvenement;
    }

    public void setDateEvenement(String dateEvenement) {
        this.dateEvenement = dateEvenement;
    }

    public List<Long> getParticipants() {
        return participants;
    }

    public void setParticipants(List<Long> participants) {
        this.participants = participants;
    }
}
