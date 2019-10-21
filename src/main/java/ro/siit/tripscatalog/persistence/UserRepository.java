package ro.siit.tripscatalog.persistence;

/**
 * Extends CrudRepository for operating with database.
 * Custom findByUsername method
 *
 * @author Radu Popescu
 *
 */

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ro.siit.tripscatalog.model.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    User findByUsername (String username);
}
