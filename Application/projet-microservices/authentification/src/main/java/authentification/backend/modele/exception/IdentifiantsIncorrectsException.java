package authentification.backend.modele.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class IdentifiantsIncorrectsException extends RuntimeException {
    public IdentifiantsIncorrectsException() {
        super("Identifiants incorrects");
    }
}
