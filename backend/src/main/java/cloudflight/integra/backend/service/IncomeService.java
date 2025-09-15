package cloudflight.integra.backend.service;

import cloudflight.integra.backend.entity.Income;


/**
 * Service interface for managing Income entities
 */
public interface IncomeService {
    /**
     * Adds an Income
     *
     * @param income the income to add
     */
    void createIncome(Income income);

    /**
     * Returns all Incomes
     *
     * @return an Iterable of all Incomes
     */
    Iterable<Income> getAllIncomes();

    /**
     * Returns an Income by its ID
     *
     * @param id the identifier of the Income
     * @return Income with the given id
     */
    Income getIncomeById(Long id);

    /**
     * Updates an Income
     *
     * @param income the income to be updated
     */
    void updateIncome(Income income);

    /**
     * Deletes an Income by its ID
     *
     * @param id the identifier of the Income
     */
    void deleteIncome(Long id);
}
