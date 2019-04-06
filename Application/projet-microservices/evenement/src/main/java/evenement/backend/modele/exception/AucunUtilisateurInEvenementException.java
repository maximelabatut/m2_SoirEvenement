package evenement.backend.modele.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class AucunUtilisateurInEvenementException extends RuntimeException {
    public AucunUtilisateurInEvenementException() {
        super("Aucun utilisateur trouve");
    }
}
