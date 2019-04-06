package utilisateur.backend.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import utilisateur.backend.modele.entities.Utilisateur;

@Repository
public interface UtilisateurRepository extends CrudRepository<Utilisateur, Long> {

    Utilisateur findUtilisateurById(Long idUtilisateur);

}
