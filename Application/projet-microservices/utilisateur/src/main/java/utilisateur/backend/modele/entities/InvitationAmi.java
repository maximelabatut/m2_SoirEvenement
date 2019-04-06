package utilisateur.backend.modele.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import net.minidev.json.annotate.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
public class InvitationAmi implements Serializable {

    @Id
    @GeneratedValue
    private Long id;
    private Long idUtilisateurInvite;
    private Long idUtilisateurInvitant;
    private Boolean isAccepte;

    public InvitationAmi(Long idUtilisateurInvite, Long idUtilisateurInvitant) {
        this.idUtilisateurInvite = idUtilisateurInvite;
        this.idUtilisateurInvitant = idUtilisateurInvitant;
        this.isAccepte = false;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdUtilisateurInvite() {
        return idUtilisateurInvite;
    }

    public void setIdUtilisateurInvite(Long idUtilisateurInvite) {
        this.idUtilisateurInvite = idUtilisateurInvite;
    }

    public Long getIdUtilisateurInvitant() {
        return idUtilisateurInvitant;
    }

    public void setIdUtilisateurInvitant(Long idUtilisateurInvitant) {
        this.idUtilisateurInvitant = idUtilisateurInvitant;
    }

    public Boolean getAccepte() {
        return isAccepte;
    }

    public void setAccepte(Boolean accepte) {
        isAccepte = accepte;
    }
}
