package gateway.frontend.domain;

public class SoireeVenir {

    private String id;
    private String nom;
    private String createur;
    private String dateSoiree;
    private String heureDebut;
    private int nbParticpants;
    private int nbEvenements;

    public SoireeVenir(String id, String nom, String createur, String dateSoiree, String heureDebut, int nbParticpants, int nbEvenements) {
        this.id = id;
        this.nom = nom;
        this.createur = createur;
        this.dateSoiree = dateSoiree;
        this.heureDebut = heureDebut;
        this.nbParticpants = nbParticpants;
        this.nbEvenements = nbEvenements;
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

    public String getCreateur() {
        return createur;
    }

    public void setCreateur(String createur) {
        this.createur = createur;
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

    public int getNbParticpants() {
        return nbParticpants;
    }

    public void setNbParticpants(int nbParticpants) {
        this.nbParticpants = nbParticpants;
    }

    public int getNbEvenements() {
        return nbEvenements;
    }

    public void setNbEvenements(int nbEvenements) {
        this.nbEvenements = nbEvenements;
    }
}
