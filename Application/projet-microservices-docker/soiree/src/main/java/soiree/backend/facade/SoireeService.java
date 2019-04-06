package soiree.backend.facade;

import soiree.backend.modele.entities.Soiree;

public interface SoireeService {

    Soiree updateSoiree(Soiree soiree);

    Iterable<Soiree> getAllSoirees();

    Soiree getSoireeById(Long idSoiree);

    Soiree creerSoiree(Soiree soiree);

    void ajouterEvenement(Long idSoiree, String idEvenement);

    void ajouterEvenementOpenAgenda(Long idSoiree, String idEvenementOpenAgenda);

}
