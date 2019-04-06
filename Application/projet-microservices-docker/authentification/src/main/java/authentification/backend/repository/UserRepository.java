package authentification.backend.repository;

import authentification.backend.modele.entities.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User,Long> {

    User findUserByUsername(String username);

}
