package utilisateur.backend.facade;


import utilisateur.backend.modele.entities.InvitationAmi;
import utilisateur.backend.modele.entities.InvitationSoiree;
import utilisateur.backend.modele.entities.Utilisateur;
import utilisateur.backend.modele.exceptions.PseudoDejaPrisException;
import utilisateur.backend.modele.exceptions.UtilisateurNotFoundException;

public interface UtilisateurService {
    Utilisateur inscription(String pseudo)  throws PseudoDejaPrisException;

    Iterable<Utilisateur> getAllUtilisateurs();

    Utilisateur getUtilisateurById(Long idUtilisateur) throws UtilisateurNotFoundException;

    Utilisateur updateUtilisateur(Utilisateur utilisateur);

    void deleteUtilisateur(Long idUtilisateur);

    InvitationAmi ajouterInvitationAmi(Long idUtilisateur1, Long idUtilisateur2);

    InvitationSoiree ajouterInvitationSoiree(Long idUtilisateur, Long idSoiree);

    void ajouterAmi(Long idUtilisateur1, Long idUtilisateur2);

    void enleverAmi(Long idUtilisateur1, Long idUtilisateur2);

    Boolean areFriends(Utilisateur utilisateur1, Utilisateur utilisateur2);

    void accepterInvitationAmi(Long idUtilisateur, Long idInvitation);

    void supprimerInvitationAmi(Long idUtilisateur, Long idInvitation);

    void accepterInvitationSoiree(Long idUtilisateur, Long idInvitation);

    void supprimerInvitationSoiree(Long idUtilisateur, Long idInvitation);
}
