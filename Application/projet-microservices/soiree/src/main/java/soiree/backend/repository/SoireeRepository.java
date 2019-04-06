package soiree.backend.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import soiree.backend.modele.entities.Soiree;

@Repository
public interface SoireeRepository extends CrudRepository<Soiree, Long> {

    Soiree findSoireeById(Long idSoiree);
}
