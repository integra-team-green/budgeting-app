package cloudflight.integra.backend.service;

import cloudflight.integra.backend.entity.Expense;
import java.util.List;

public interface ExpenseService {
    /**
     * Create a new expense after validating input fields.
     *
     * @param expense the expense to create
     * @return the created expense
     */
    Expense createExpense(Expense expense);

    /**
     * Get all expenses for a specific user.
     *
     * @param userId the user ID
     * @return a list of expenses for the user
     */
    List<Expense> findAllByUserId(Long userId);


    /**
     * Finds an expense by its ID.
     *
     * @param id the ID of the expense to find
     * @return the]]] object with the given ID
     * @throws RuntimeException if no expense with the given ID is found
     */
    Expense findById(Long id);

    /**
     * Update an existing expense by ID.
     *
     * @param id the ID of the expense to update
     * @param updatedExpense the expense object containing updated fields
     * @return the updated {@link Expense} object after saving
     * @throws RuntimeException if no expense with the given ID exists
     * @throws IllegalArgumentException if the updated expense fails validation
     */
    Expense updateExpense(Long id, Expense updatedExpense);

    /**
     * Delete an expense by its ID.
     *
     * @param id the expense ID to delete
     */
    void deleteExpense(Long id);
}
