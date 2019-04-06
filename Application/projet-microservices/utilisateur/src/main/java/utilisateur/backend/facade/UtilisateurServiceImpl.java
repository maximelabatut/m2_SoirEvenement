package utilisateur.backend.facade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import utilisateur.backend.modele.entities.InvitationAmi;
import utilisateur.backend.modele.entities.InvitationSoiree;
import utilisateur.backend.modele.entities.Utilisateur;
import utilisateur.backend.modele.exceptions.PseudoDejaPrisException;
import utilisateur.backend.modele.exceptions.UtilisateurNotFoundException;
import utilisateur.backend.repository.UtilisateurRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class UtilisateurServiceImpl implements UtilisateurService {

    private final UtilisateurRepository utilisateurRepository;

    @Autowired
    public UtilisateurServiceImpl(UtilisateurRepository utilisateurRepository) {
        this.utilisateurRepository = utilisateurRepository;
    }

    /**
     * Inscrit un utilisateur
     *
     * @param pseudo   pseudo de l'utilisateur
     * @return utilisateur inscrit
     */
    @Override
    public Utilisateur inscription(String pseudo) throws PseudoDejaPrisException{
        for(Utilisateur utilisateur : utilisateurRepository.findAll()){
            if(utilisateur.getPseudo().equals(pseudo)){
                throw new PseudoDejaPrisException();
            }
        }
        // Creation de l'utilisateur
        Utilisateur utilisateur = new Utilisateur(pseudo);
        utilisateur.setPrenom(pseudo);
        utilisateur.setNom("   ");
        // Insertion de l'utilisateur dans la BDD
        this.utilisateurRepository.save(utilisateur);
        // Retourne l'utilisateur cree
        return utilisateur;

    }

    /**
     * Modification d'un utilisateur
     *
     * @param utilisateur utilisateur
     * @return utilisateur modifie
     */
    @Override
    public Utilisateur updateUtilisateur(Utilisateur utilisateur) {
        return this.utilisateurRepository.save(utilisateur);
    }

    /**
     * Recuperation de tous les utilisateurs
     *
     * @return les utilisateurs
     */
    @Override
    @Transactional(readOnly = true)
    public Iterable<Utilisateur> getAllUtilisateurs() {
        return this.utilisateurRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Utilisateur getUtilisateurById(Long idUtilisateur) throws UtilisateurNotFoundException {
        Utilisateur utilisateur = this.utilisateurRepository.findUtilisateurById(idUtilisateur);
        if(utilisateur != null) {
            return utilisateur;
        }else {
            throw new UtilisateurNotFoundException();
        }
    }

    @Override
    public void deleteUtilisateur(Long idUtilisateur) {
        this.utilisateurRepository.delete(this.utilisateurRepository.findUtilisateurById(idUtilisateur));
    }

    /**
     * Ajoute une invitation d'amis
     *
     * @param idUtilisateur1 id de l'utilisateur qui se fait ajouter en ami
     * @param idUtilisateur2 id de l'utilisateur qui ajoute son ami
     */
    @Override
    public InvitationAmi ajouterInvitationAmi(Long idUtilisateur1,Long idUtilisateur2) {
        Utilisateur utilisateur1 = this.getUtilisateurById(idUtilisateur1);
        InvitationAmi invitationAmi = new InvitationAmi(idUtilisateur1,idUtilisateur2);
        utilisateur1.getInvitationsAmis().add(invitationAmi);
        this.utilisateurRepository.save(utilisateur1);
        return invitationAmi;
    }

    /**
     * Ajoute une invitation de soiree
     *
     * @param idUtilisateur id de l'utilisateur qui se fait inviter
     * @param idSoiree id de l'evenement
     */
    @Override
    public InvitationSoiree ajouterInvitationSoiree(Long idUtilisateur,Long idSoiree) {
        Utilisateur utilisateur = this.getUtilisateurById(idUtilisateur);
        InvitationSoiree invitationSoiree = new InvitationSoiree(idUtilisateur, idSoiree);
        utilisateur.getInvitationsSoirees().add(invitationSoiree);
        this.utilisateurRepository.save(utilisateur);
        return invitationSoiree;
    }

    /**
     * Ajoute un couple d'amis
     *
     * @param idUtilisateur1 id de l'utilisateur qui se fait ajouter en ami
     * @param idUtilisateur2 id de l'utilisateur qui ajoute son ami
     */
    @Override
    public void ajouterAmi(Long idUtilisateur1,Long idUtilisateur2) {
        Utilisateur utilisateur1 = this.getUtilisateurById(idUtilisateur1);
        Utilisateur utilisateur2 = this.getUtilisateurById(idUtilisateur2);
        if(!this.areFriends(utilisateur1, utilisateur2)) {
            utilisateur1.getAmis().add(utilisateur2);
            utilisateur2.getAmis().add(utilisateur1);
            this.utilisateurRepository.save(utilisateur1);
            this.utilisateurRepository.save(utilisateur2);
        }
    }

    /**
     * Supprime un couple d'amis
     *
     * @param idUtilisateur1 id de l'utilisateur qui se fait enlever
     * @param idUtilisateur2 id de l'utilisateur qui enleve son ami
     */
    @Override
    public void enleverAmi(Long idUtilisateur1, Long idUtilisateur2) {
        Utilisateur utilisateur1 = this.getUtilisateurById(idUtilisateur1);
        Utilisateur utilisateur2 = this.getUtilisateurById(idUtilisateur2);
        if(this.areFriends(utilisateur1, utilisateur2)) {
            this.deleteFriend(utilisateur1,utilisateur2);
            this.deleteFriend(utilisateur2,utilisateur1);
            this.utilisateurRepository.save(utilisateur1);
            this.utilisateurRepository.save(utilisateur2);
        }
    }

    public void accepterInvitationAmi(Long idUtilisateur, Long idInvitation){
        Utilisateur utilisateur = this.utilisateurRepository.findUtilisateurById(idUtilisateur);
        if(utilisateur!= null) {
            for(int i=0; i<utilisateur.getInvitationsAmis().size(); i++){
                if(utilisateur.getInvitationsAmis().get(i).getId().equals(idInvitation)){
                    utilisateur.getInvitationsAmis().get(i).setIsAccepte(true);
                    this.ajouterAmi(idUtilisateur, utilisateur.getInvitationsAmis().get(i).getIdUtilisateurInvitant());
                    this.supprimerInvitationAmi(idUtilisateur, idInvitation);
                }
            }
        }
        this.updateUtilisateur(utilisateur);
    }

    public void supprimerInvitationAmi(Long idUtilisateur, Long idInvitation){
        Utilisateur utilisateur = this.utilisateurRepository.findUtilisateurById(idUtilisateur);
        List<InvitationAmi> invitationAmis = new ArrayList<>();
        if(utilisateur!= null) {
            for(int i=0; i<utilisateur.getInvitationsAmis().size(); i++){
                if(!utilisateur.getInvitationsAmis().get(i).getId().equals(idInvitation)){
                    invitationAmis.add(utilisateur.getInvitationsAmis().get(i));
                }
            }
        }
        utilisateur.setInvitationsAmis(invitationAmis);
        this.updateUtilisateur(utilisateur);
    }

    public void accepterInvitationSoiree(Long idUtilisateur, Long idInvitation){
        Utilisateur utilisateur = this.utilisateurRepository.findUtilisateurById(idUtilisateur);
        if(utilisateur!= null) {
            for(int i=0; i<utilisateur.getInvitationsSoirees().size(); i++){
                if(utilisateur.getInvitationsSoirees().get(i).getId().equals(idInvitation)){
                    utilisateur.getInvitationsSoirees().get(i).setIsAccepte(true);
                    this.supprimerInvitationSoiree(idUtilisateur, idInvitation);
                }
            }
        }
        this.updateUtilisateur(utilisateur);
    }

    public void supprimerInvitationSoiree(Long idUtilisateur, Long idInvitation){
        Utilisateur utilisateur = this.utilisateurRepository.findUtilisateurById(idUtilisateur);
        List<InvitationSoiree> invitationSoirees = new ArrayList<>();
        if(utilisateur!= null) {
            for(int i=0; i<utilisateur.getInvitationsSoirees().size(); i++){
                if(!utilisateur.getInvitationsSoirees().get(i).getId().equals(idInvitation)){
                    invitationSoirees.add(utilisateur.getInvitationsSoirees().get(i));
                }
            }
        }
        utilisateur.setInvitationsSoirees(invitationSoirees);
        this.updateUtilisateur(utilisateur);
    }

    private void deleteFriend(Utilisateur utilisateur1, Utilisateur utilisateur2){
        for (int i = 0 ; i < utilisateur1.getAmis().size() ; i++) {
            if(utilisateur1.getAmis().get(i).getPseudo().equals(utilisateur2.getPseudo())){
                utilisateur1.getAmis().remove(i);
            }
        }
    }

    public Boolean areFriends(Utilisateur utilisateur1, Utilisateur utilisateur2){
        Boolean friendShip1 = false;
        for (Utilisateur ami : utilisateur1.getAmis()){
            if(ami.getPseudo().equals(utilisateur2.getPseudo())){
                friendShip1 = true;
            }
        }
        Boolean friendShip2 = false;
        for (Utilisateur ami : utilisateur2.getAmis()){
            if(ami.getPseudo().equals(utilisateur1.getPseudo())){
                friendShip2 = true;
            }
        }
        return friendShip1 && friendShip2;
    }
}
