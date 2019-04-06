package evenementOpenAgenda.backend.facade;

import evenementOpenAgenda.backend.modele.entities.EvenementOpenAgenda;
import evenementOpenAgenda.backend.modele.exception.EvenementOpenAgendaIntrouvableException;

import java.util.Collection;
import java.util.Optional;

public interface EvenementOpenAgendaService {

    Iterable<EvenementOpenAgenda> getAllEvenement();

    EvenementOpenAgenda getEvenementById(String uid) throws EvenementOpenAgendaIntrouvableException;

    Collection<Long> getUtilisateurByEvenement(String id) throws EvenementOpenAgendaIntrouvableException;

    EvenementOpenAgenda updateEvenement(EvenementOpenAgenda evenement);
}
