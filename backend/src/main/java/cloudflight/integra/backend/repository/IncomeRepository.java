package cloudflight.integra.backend.repository;

import cloudflight.integra.backend.entity.Income;
import org.springframework.stereotype.Repository;


public interface IncomeRepository {
    /**
     * Saves an income
     *
     * @param income
     */
    void create(Income income);

    /**
     *
     * @return all incomes
     */
    Iterable<Income> getAll();

    /**
     * Finds an income by ID.
     * @param id
     * @return the income with the searched ID, or an exception
     */
    Income findById(Long id);

    /**
     * Updates an income
     * @param income
     */
    void update(Income income);

    /**
     * Deletes an income
     * @param id
     */
    void delete(Long id);
}
