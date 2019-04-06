package authentification.backend.facade;

import authentification.backend.modele.entities.User;
import authentification.backend.modele.exception.IdentifiantsIncorrectsException;
import authentification.backend.modele.exception.PseudoDejaPrisException;
import authentification.backend.modele.exception.UserIntrouvableException;

public interface AuthentificationService {

    User connexion(String username, String password) throws UserIntrouvableException, IdentifiantsIncorrectsException;

    void deconnexion(String username) throws UserIntrouvableException;

    User inscription(String username, String password) throws PseudoDejaPrisException;

    Iterable<User> getAllUsers();

    User getUtilisateurByPseudo(String pseudo) throws UserIntrouvableException;

    void updateUser(User user);

    void deleteUtilisateur(String pseudo) throws UserIntrouvableException;

}
