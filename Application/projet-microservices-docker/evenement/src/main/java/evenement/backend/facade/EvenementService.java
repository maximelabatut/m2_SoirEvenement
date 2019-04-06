package evenement.backend.facade;

import evenement.backend.modele.entities.Evenement;
import evenement.backend.modele.exception.EvenementIntrouvableException;

import java.time.LocalDate;
import java.util.List;

public interface EvenementService {

    Iterable<Evenement> getAllEvenement();

    Evenement creerEvenement(String nom, String idCreateur, LocalDate dateEvenement, String lieu);

    Evenement getEvenementById(Long id);

    Evenement updateEvenement(Evenement evenement);

    void deleteEvenement(Long id);

    List<Long> getUtilisateurByEvenement(Long id) throws EvenementIntrouvableException;

}
