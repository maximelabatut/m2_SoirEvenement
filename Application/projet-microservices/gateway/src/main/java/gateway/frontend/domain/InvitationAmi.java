package gateway.frontend.domain;

public class InvitationAmi {
    private String id;
    private String idUtilisateurInvite;
    private String idUtilisateurInvitant;
    private Boolean isAccepte;

    public InvitationAmi() {
        this.isAccepte = false;
    }

    public InvitationAmi(String idUtilisateurInvite, String idUtilisateurInvitant) {
        this();
        this.idUtilisateurInvite = idUtilisateurInvite;
        this.idUtilisateurInvitant = idUtilisateurInvitant;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdUtilisateurInvite() {
        return idUtilisateurInvite;
    }

    public void setIdUtilisateurInvite(String idUtilisateurInvite) {
        this.idUtilisateurInvite = idUtilisateurInvite;
    }

    public String getIdUtilisateurInvitant() {
        return idUtilisateurInvitant;
    }

    public void setIdUtilisateurInvitant(String idUtilisateurInvitant) {
        this.idUtilisateurInvitant = idUtilisateurInvitant;
    }

    public Boolean getAccepte() {
        return isAccepte;
    }

    public void setAccepte(Boolean accepte) {
        isAccepte = accepte;
    }
}
