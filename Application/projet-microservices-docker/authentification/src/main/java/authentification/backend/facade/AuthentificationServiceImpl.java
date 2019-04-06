package authentification.backend.facade;


import authentification.backend.modele.entities.User;
import authentification.backend.modele.exception.IdentifiantsIncorrectsException;
import authentification.backend.modele.exception.PseudoDejaPrisException;
import authentification.backend.modele.exception.UserIntrouvableException;
import authentification.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AuthentificationServiceImpl implements AuthentificationService {

    private UserRepository repository;

    @Autowired
    public AuthentificationServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public User connexion(String username, String password) throws UserIntrouvableException, IdentifiantsIncorrectsException {
        User user = this.repository.findUserByUsername(username);
        if(user == null){
            throw new UserIntrouvableException();
        }
        if(!user.getPassword().equals(password)){
            throw new IdentifiantsIncorrectsException();
        }
        user.setActive(true);
        this.repository.save(user);
        return user;
    }

    @Override
    public void deconnexion(String username) throws UserIntrouvableException{
        User user = this.repository.findUserByUsername(username);
        if(user == null){
            throw new UserIntrouvableException();
        }
        user.setActive(false);
        this.repository.save(user);
    }

    @Override
    public User inscription(String username, String password) throws PseudoDejaPrisException {
        if(this.repository.findUserByUsername(username)==null) {
            User user = new User(username, password);
            this.repository.save(user);
            return user;
        }else {
            throw new PseudoDejaPrisException();
        }
    }

    @Override
    public Iterable<User> getAllUsers() {
        return this.repository.findAll();
    }

    @Override
    public User getUtilisateurByPseudo(String pseudo) throws UserIntrouvableException {
        User user = this.repository.findUserByUsername(pseudo);
        if(user == null) {
            throw new UserIntrouvableException();
        }
        return user;
    }

    @Override
    public void updateUser(User user) {
        this.repository.save(user);
    }

    @Override
    public void deleteUtilisateur(String pseudo) throws UserIntrouvableException {
        User user = this.repository.findUserByUsername(pseudo);
        if(user == null) {
            throw new UserIntrouvableException();
        }
        this.repository.delete(user);
    }
}
