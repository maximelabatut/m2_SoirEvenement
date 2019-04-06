package utilisateur.backend.domain;

public class Invitation {

    private Long idPseudoInvitant;
    private Long idPseudoInvite;

    public Invitation(Long idPseudoInvitant, Long idPseudoInvite) {
        this.idPseudoInvitant = idPseudoInvitant;
        this.idPseudoInvite = idPseudoInvite;
    }

    public Long getIdPseudoInvitant() {
        return idPseudoInvitant;
    }

    public void setIdPseudoInvitant(Long idPseudoInvitant) {
        this.idPseudoInvitant = idPseudoInvitant;
    }

    public Long getIdPseudoInvite() {
        return idPseudoInvite;
    }

    public void setIdPseudoInvite(Long idPseudoInvite) {
        this.idPseudoInvite = idPseudoInvite;
    }
}
