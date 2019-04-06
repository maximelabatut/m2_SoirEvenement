package evenementOpenAgenda.backend.repository;

import evenementOpenAgenda.backend.modele.entities.EvenementOpenAgenda;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EvenementOpenAgendaRepository extends CrudRepository<EvenementOpenAgenda, Long> {

    EvenementOpenAgenda findEvenementOpenAgendaByUid(String uid);

    Boolean existsEvenementOpenAgendaByUid(String uid);

    void deleteEvenementOpenAgendaByUid(String uid);

}
