package cloudflight.integra.backend.service;

import cloudflight.integra.backend.dto.ExpenseDTO;

// varianta buna
/** Service interface for managing expenses. */
public interface ExpenseService {

  /**
   * Creates a new Expense.
   *
   * @param expenseDTO the expense to be created
   * @return the created Expense
   */
  ExpenseDTO createExpense(ExpenseDTO expenseDTO);

  /**
   * Returns all Expenses.
   *
   * @return an Iterable of all Expenses
   */
  Iterable<ExpenseDTO> getAllExpenses();

  /**
   * Returns an Expense by its ID.
   *
   * @param id the identifier of the Expense
   * @return Expense with the given id
   */
  ExpenseDTO getExpense(Long id);

  /**
   * Updates an Expense.
   *
   * @param expenseDTO the expense to be updated
   */
  void updateExpense(ExpenseDTO expenseDTO);

  /**
   * Deletes an Expense by its ID.
   *
   * @param id the identifier of the Expense
   */
  void deleteExpense(Long id);
}
