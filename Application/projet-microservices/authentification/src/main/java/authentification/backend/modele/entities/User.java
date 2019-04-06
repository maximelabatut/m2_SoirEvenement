package authentification.backend.modele.entities;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
@Data
public class User implements Serializable {

    public static final String USER = "USER";

    @Id
    @GeneratedValue
    private long id;
    private String username;
    private String password;
    private String role;
    private Boolean active;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.role = USER;
        this.active = false;
    }
}
