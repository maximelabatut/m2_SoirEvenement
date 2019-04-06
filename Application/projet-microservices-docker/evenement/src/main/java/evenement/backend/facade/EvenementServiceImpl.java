package evenement.backend.facade;


import evenement.backend.modele.entities.Evenement;
import evenement.backend.modele.exception.EvenementIntrouvableException;
import evenement.backend.repository.EvenementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class EvenementServiceImpl implements EvenementService {

    private EvenementRepository evenementRepository;

    @Autowired
    public EvenementServiceImpl(EvenementRepository evenementRepository) {
        this.evenementRepository = evenementRepository;
    }

    /**
     * Recuperation de tous les evenements
     *
     * @return les evenements
     */
    @Override
    public Iterable<Evenement> getAllEvenement() {
        return this.evenementRepository.findAll();
    }

    /**
     * Créer un evenement
     *
     * @param nom   nom de l'evenement
     * @param dateEvenement   date de l'evenement
     * @param lieu lieu de l'evenement
     * @return evenement
     */
    @Override
    public Evenement creerEvenement(String nom, String idCreateur ,LocalDate dateEvenement, String lieu) {
        // Creation de l'evenement
        Evenement evenement = new Evenement(nom,idCreateur,dateEvenement, lieu);
        // Insertion de l'utilisateur dans la BDD
        this.evenementRepository.save(evenement);
        // Retourne l'evenement créé
        return evenement;
    }

    /**
     * Recuperation d'un évenement
     *
     * @param id numéro de l'évenement
     * @return un évenement
     */
    @Override
    @Transactional(readOnly = true)
    public Evenement getEvenementById(Long id) {
        Evenement evenement = this.evenementRepository.findEvenementById(id);
        if(evenement == null){
            throw new EvenementIntrouvableException();
        }
        return evenement;
    }

    /**
     * Mise à jour d'un évenement
     *
     * @param evenement les parametres de l'évenement
     * @return un évenement
     */
    @Override
    public Evenement updateEvenement(Evenement evenement) {
        return this.evenementRepository.save(evenement);
    }

    /**
     * Archivage d'un évenement
     *
     * @param id numéro de l'évenement
     */
    @Override
    public void deleteEvenement(Long id) {
        this.evenementRepository.delete(this.evenementRepository.findEvenementById(id));
    }

    /**
     * Recuperation des participants
     *
     * @param id numéro de l'évenement
     * @return un évenement
     */
    @Override
    public List<Long> getUtilisateurByEvenement(Long id) throws EvenementIntrouvableException{
        return this.evenementRepository.findEvenementById(id).getParticipants();
    }
}
