package cloudflight.integra.backend.repository;

import cloudflight.integra.backend.entity.Income;

/**
 * Repository interface provides CRUD operations for Income entity.
 */
public interface IncomeRepository {
    /**
     * Saves the Income entity
     *
     * @param income the Income entity that was added
     */
    void create(Income income);

    /**
     * Retrieves all Income entities.
     *
     * @return An Iterable of all Income entities.
     */
    Iterable<Income> getAll();

    /**
     * Finds an income by ID.
     * @param id the identifier of the entity
     * @return the income with the searched ID, or an exception
     */
    Income findById(Long id);

    /**
     * Updates an Income by its ID
     *
     * @param income the Income entity that was updated
     */
    void update(Income income);

    /**
     * Deletes an Income by its ID.
     *
     * @param id the identifier of the Income
     */
    void delete(Long id);
}
