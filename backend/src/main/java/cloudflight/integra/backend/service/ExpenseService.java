package cloudflight.integra.backend.service;

import cloudflight.integra.backend.entity.Expense;

import java.util.List;

/**
 * Service interface for managing Expense entities.
 */
public interface ExpenseService {

    /**
     * Create a new expense after validation.
     *
     * @param expense the expense to create
     * @return the created expense
     */
    Expense createExpense(Expense expense);

    /**
     * Find an expense by its ID.
     *
     * @param id the ID of the expense
     * @return the expense
     */
    Expense getExpense(Long id);

    /**
     * Find all expenses for a specific user.
     *
     * @param userId the user ID
     * @return list of expenses
     */
    List<Expense> getAllExpensesByUser(Long userId);

    /**
     * Update an existing expense.
     *
     * @param expense the expense with updated fields
     * @return the updated expense
     */
    Expense updateExpense(Expense expense);

    /**
     * Delete an expense by ID.
     *
     * @param id the expense ID
     */
    void deleteExpense(Long id);
}
