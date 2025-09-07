package cloudflight.integra.backend.repository;

import cloudflight.integra.backend.entity.Credit;

import java.util.List;
import java.util.Optional;

public interface CreditRepository {

    /**
     * Saves a credit entity to the repository.
     *
     * @param credit .
     * @return .
     */
    Credit save(Credit credit);

    /**
     * Finds a credit entity by its ID.
     *
     * @param id .
     * @return .
     */
    Optional<Credit> findById(Long id);

    /**
     * Retrieves all credit entities from the repository.
     *
     * @return .
     */
    List<Credit> findAll();

    /**
     * Deletes a credit entity by its ID.
     *
     * @param id .
     */
    void deleteById(Long id);

    /**
     * Checks if a credit entity exists by its ID.
     *
     * @param id .
     * @return .
     */
    boolean existsById(Long id);

    /**
     * Deletes all credit entities from the repository.
     */
    void deleteAll();
}