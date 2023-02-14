package ro.siit.tripscatalog.persistence;

/**
 * Extends CrudRepository for operating with database.
 * Custom findByUser method
 *
 * @author Radu Popescu
 *
 */

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ro.siit.tripscatalog.model.Trip;
import ro.siit.tripscatalog.model.User;

import java.util.List;

@Repository
public interface TripRepository extends CrudRepository<Trip, Long> {
    List<Trip> findByUser (User user);
}
