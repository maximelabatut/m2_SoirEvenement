package authentification.backend.modele.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class PseudoDejaPrisException extends RuntimeException {
    public PseudoDejaPrisException() {
        super("Pseudo deja pris");
    }
}
