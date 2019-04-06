package gateway.frontend.domain;

public class InvitationSoiree {
    private String id;
    private String utilisateurInvite;
    private String idSoiree;
    private Boolean isAccepte;

    public InvitationSoiree() {
        this.isAccepte = false;
    }

    public InvitationSoiree(String utilisateurInvite, String idSoiree) {
        this();
        this.utilisateurInvite = utilisateurInvite;
        this.idSoiree = idSoiree;
    }

    public InvitationSoiree(String id, String utilisateurInvite, String idSoiree, Boolean isAccepte) {
        this(utilisateurInvite, idSoiree);
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUtilisateurInvite() {
        return utilisateurInvite;
    }

    public void setUtilisateurInvite(String utilisateurInvite) {
        this.utilisateurInvite = utilisateurInvite;
    }

    public String getIdSoiree() {
        return idSoiree;
    }

    public void setIdSoiree(String idSoiree) {
        this.idSoiree = idSoiree;
    }

    public Boolean getAccepte() {
        return isAccepte;
    }

    public void setAccepte(Boolean accepte) {
        isAccepte = accepte;
    }
}
