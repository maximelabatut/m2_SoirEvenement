package evenement.backend.modele.entities;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.ToString;
import net.minidev.json.annotate.JsonIgnore;
import javax.persistence.*;
import javax.validation.constraints.Size;

@Data
@Entity
@ToString(exclude = "participants")
public class Evenement implements Serializable {
    @Id
    @GeneratedValue
    private Long id;
    private String idCreateur;
    @Size(min = 3, max = 200)
    private String nom;
    private LocalDate dateEvenement;
    @Size(min = 3, max = 200)
    private String lieu;


    @ElementCollection(fetch = FetchType.EAGER)
    @JsonIgnore
    private List<Long> participants;

    public Evenement(String nom,String idCreateur, LocalDate dateEvenement, String lieu) {
        this.nom = nom;
        this.idCreateur = idCreateur;
        this.dateEvenement = dateEvenement;
        this.lieu = lieu;
        this.participants = new ArrayList<>();
    }

    public void ajouterParticipant(Long idUtilisateur){
        if(!participants.contains(idUtilisateur)) {
            this.participants.add(idUtilisateur);
        }
    }
}
