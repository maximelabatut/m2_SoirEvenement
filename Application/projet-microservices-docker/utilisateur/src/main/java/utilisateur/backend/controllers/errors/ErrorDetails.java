package utilisateur.backend.controllers.errors;

import java.time.LocalDate;

public class ErrorDetails {
    private LocalDate timeStamp;
    private String message;
    private String details;

    public ErrorDetails(String message, String details) {
        super();
        this.timeStamp = LocalDate.now();
        this.message = message;
        this.details = details;
    }

    public LocalDate getTimeStamp() {
        return timeStamp;
    }

    public String getMessage() {
        return message;
    }

    public String getDetails() {
        return details;
    }
}
