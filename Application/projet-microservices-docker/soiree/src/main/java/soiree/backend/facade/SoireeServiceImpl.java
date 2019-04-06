package soiree.backend.facade;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import soiree.backend.modele.entities.Soiree;
import soiree.backend.modele.exceptions.SoireeNotFoundException;
import soiree.backend.repository.SoireeRepository;

@Service
@Transactional
public class SoireeServiceImpl implements SoireeService {

    private final SoireeRepository soireeRepository;

    public SoireeServiceImpl(SoireeRepository soireeRepository) {
        this.soireeRepository = soireeRepository;
    }

    @Override
    public Soiree updateSoiree(Soiree soiree) {
        return this.soireeRepository.save(soiree);
    }

    @Override
    @Transactional(readOnly = true)
    public Iterable<Soiree> getAllSoirees() {
        return this.soireeRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Soiree getSoireeById(Long idSoiree) {
        Soiree soiree = this.soireeRepository.findSoireeById(idSoiree);
        if(soiree != null) {
            return soiree;
        }else {
            throw new SoireeNotFoundException();
        }
    }

    @Override
    public Soiree creerSoiree(Soiree soiree) {
        Soiree creerSoiree = new Soiree(soiree.getNom(), soiree.getUtilisateur(), soiree.getNbPlaces(), soiree.getDateSoiree(), soiree.getHeureDebut());
        creerSoiree.setEvenements(soiree.getEvenements());
        creerSoiree.setEvenementsOpenAgenda(soiree.getEvenementsOpenAgenda());
        creerSoiree.setParticipants(soiree.getParticipants());
        this.soireeRepository.save(creerSoiree);
        return creerSoiree;
    }

    @Override
    public void ajouterEvenement(Long idSoiree, String idEvenement) {
        Soiree soiree = this.getSoireeById(idSoiree);
        soiree.getEvenements().add(idEvenement);
        this.updateSoiree(soiree);
    }

    @Override
    public void ajouterEvenementOpenAgenda(Long idSoiree, String idEvenementOpenAgenda) {
        Soiree soiree = this.getSoireeById(idSoiree);
        soiree.getEvenementsOpenAgenda().add(idEvenementOpenAgenda);
        this.updateSoiree(soiree);
    }
}
