package evenement.backend.repository;

import evenement.backend.modele.entities.Evenement;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EvenementRepository extends CrudRepository<Evenement, Long> {

    Evenement findEvenementById(Long id);

}
