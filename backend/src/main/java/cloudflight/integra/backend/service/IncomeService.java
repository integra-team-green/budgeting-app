package cloudflight.integra.backend.service;

import cloudflight.integra.backend.entity.Income;
import org.springframework.stereotype.Service;

public interface IncomeService {
    /**
     * Validates the income then adds it
     * @param income
     */
    void createIncome(Income income);

    /**
     * Returns all incomes
     * @return
     */
    Iterable<Income> getAllIncomes();

    /**
     *
     * @param id
     * @return Income with the given id
     */
    Income getIncomeById(Long id);

    /**
     * Validates the new values then updates the income
     * @param income
     */
    void updateIncome(Income income);

    /**
     * Deletes an income by its ID
     * @param id
     */
    void deleteIncome(Long id);
}
