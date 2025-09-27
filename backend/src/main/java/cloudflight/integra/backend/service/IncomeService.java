package cloudflight.integra.backend.service;

import cloudflight.integra.backend.dto.IncomeDTO;

/** Service interface for managing Income entities */
public interface IncomeService {
  /**
   * Adds an Income
   *
   * @param income the income to add
   */
  IncomeDTO createIncome(IncomeDTO income);

  /**
   * Returns all Incomes
   *
   * @return an Iterable of all Incomes
   */
  Iterable<IncomeDTO> getAllIncomes();

  /**
   * Returns an Income by its ID
   *
   * @param id the identifier of the Income
   * @return Income with the given id
   */
  IncomeDTO getIncomeById(Long id);

  /**
   * Updates an Income
   *
   * @param income the income to be updated
   */
  IncomeDTO updateIncome(IncomeDTO income);

  /**
   * Deletes an Income by its ID
   *
   * @param id the identifier of the Income
   */
  void deleteIncome(Long id);
}
