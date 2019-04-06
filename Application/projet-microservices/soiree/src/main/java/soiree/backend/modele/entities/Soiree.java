package soiree.backend.modele.entities;

import lombok.Data;
import net.minidev.json.annotate.JsonIgnore;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
@Entity
public class Soiree {

    @Id
    @GeneratedValue
    private Long id;

    private String nom;

    private Long utilisateur;

    @ElementCollection(fetch = FetchType.EAGER)
    @Fetch(value = FetchMode.SUBSELECT)
    @JsonIgnore
    private List<String> evenements;

    @ElementCollection(fetch = FetchType.EAGER)
    @Fetch(value = FetchMode.SUBSELECT)
    @JsonIgnore
    private List<String> evenementsOpenAgenda;

    @ElementCollection(fetch = FetchType.EAGER)
    @Fetch(value = FetchMode.SUBSELECT)
    @JsonIgnore
    private List<Long> participants;

    private Long nbPlaces;

    private LocalDate dateSoiree;

    private String heureDebut;

    public Soiree(String nom, Long utilisateur, Long nbPlaces, LocalDate dateSoiree, String heureDebut) {
        this.nom = nom;
        this.utilisateur = utilisateur;
        this.evenements = new ArrayList<>();
        this.evenementsOpenAgenda = new ArrayList<>();
        this.participants = new ArrayList<>();
        this.nbPlaces = nbPlaces;
        this.dateSoiree = dateSoiree;
        this.heureDebut = heureDebut;
    }
}
