package evenement.backend.controllers.errors;

import evenement.backend.modele.exception.AucunUtilisateurInEvenementException;
import evenement.backend.modele.exception.EvenementIntrouvableException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestController
@ControllerAdvice
public class CustomizedResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails("Une erreur est survenue", ex.getBindingResult().toString());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDetails);
    }


    // Exception AucunUtilisateurInEvenementException
    @ExceptionHandler(AucunUtilisateurInEvenementException.class)
    public final ResponseEntity<ErrorDetails> handleAucunUtilisateurInEvenement(AucunUtilisateurInEvenementException ex, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(ex.getMessage(), request.getDescription(false));
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDetails);
    }


    // Exception EvenementIntrouvableException
    @ExceptionHandler(EvenementIntrouvableException.class)
    public final ResponseEntity<ErrorDetails> handleEvenementIntrouvableException(EvenementIntrouvableException ex, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(ex.getMessage(), request.getDescription(false));
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDetails);
    }


}
