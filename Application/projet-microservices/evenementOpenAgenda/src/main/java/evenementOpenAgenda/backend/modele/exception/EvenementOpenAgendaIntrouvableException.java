package evenementOpenAgenda.backend.modele.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class EvenementOpenAgendaIntrouvableException extends RuntimeException{
    public EvenementOpenAgendaIntrouvableException() {
        super("Evenement Open Agenda Introuvable");
    }
}
