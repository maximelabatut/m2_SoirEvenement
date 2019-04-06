package utilisateur.backend.modele.entities;

import lombok.Data;
import net.minidev.json.annotate.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

@Data
@Entity
public class InvitationSoiree implements Serializable {

    @Id
    @GeneratedValue
    private Long id;
    private Long utilisateurInvite;
    private Long idSoiree;
    private Boolean isAccepte;

    public InvitationSoiree(Long utilisateurInvite, Long idSoiree) {
        this.utilisateurInvite = utilisateurInvite;
        this.idSoiree = idSoiree;
        this.isAccepte = false;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUtilisateurInvite() {
        return utilisateurInvite;
    }

    public void setUtilisateurInvite(Long utilisateurInvite) {
        this.utilisateurInvite = utilisateurInvite;
    }

    public Long getIdSoiree() {
        return idSoiree;
    }

    public void setIdSoiree(Long idSoiree) {
        this.idSoiree = idSoiree;
    }

    public Boolean getAccepte() {
        return isAccepte;
    }

    public void setAccepte(Boolean accepte) {
        isAccepte = accepte;
    }
}
