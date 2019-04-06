package utilisateur.backend.modele.entities;


import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@ToString(exclude = {"amis","invitationsAmis","invitationsSoirees"})
public class Utilisateur implements Serializable {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @Size(min = 3, max = 50)
    private String pseudo;
    @Size(min = 3, max = 50)
    private String nom;
    @Size(min = 3, max = 50)
    private String prenom;
    @Size(min = 3, max = 200)
    private String adresse;
    @Size(min = 3, max = 50)
    private String ville;
    @Size(min = 5, max = 5)
    private String codePostal;
    @Email
    private String email;
    private LocalDate dateInscription;
    private LocalDate dateNaiss;

    @JsonBackReference
    @ManyToMany(fetch = FetchType.EAGER)
    @Fetch(value = FetchMode.SUBSELECT)
    private List<Utilisateur> amis;

    @JsonBackReference(value="idUtilisateurInvite")
    @OneToMany(fetch = FetchType.EAGER, cascade=CascadeType.ALL)
    @Fetch(value = FetchMode.SUBSELECT)
    private List<InvitationAmi> invitationsAmis;

    @JsonBackReference(value="utilisateurInvite")
    @OneToMany(fetch = FetchType.EAGER, cascade=CascadeType.ALL)
    @Fetch(value = FetchMode.SUBSELECT)
    private List<InvitationSoiree> invitationsSoirees;

    public Utilisateur(String pseudo) {
        this.pseudo = pseudo;
        this.dateInscription = LocalDate.now();
        this.amis = new ArrayList<>();
        this.invitationsAmis = new ArrayList<>();
        this.invitationsSoirees = new ArrayList<>();
    }
}
