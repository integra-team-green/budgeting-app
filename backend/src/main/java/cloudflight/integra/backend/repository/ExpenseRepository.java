package cloudflight.integra.backend.repository;

import cloudflight.integra.backend.entity.Expense;

import java.util.List;
import java.util.Optional;

public interface ExpenseRepository {
    /**
     * Save a new expense or update an existing one.
     *
     * @param expense the expense to save
     * @return the saved expense
     */
    Expense saveExpense(Expense expense);

    /**
     * Find all expenses for a specific user.
     *
     * @param userId the user ID
     * @return a list of expenses for the user
     */
    List<Expense> findAllByUserId(Long userId);

    /**
     * Find an expense by its ID.
     *
     * @param id the expense ID
     */
    Optional<Expense> findById(Long id);

    /**
     * Delete an expense by its ID.
     *
     * @param id the expense ID to delete
     */
    void deleteById(Long id);

    /**
     * Updates an existing expense in the repository.
     *
     *
     * @param expense the object containing updated data
     * @return the updated object
     * @throws IllegalArgumentException if the expense ID is null or does not exist in the repository
     */
    Expense updateExpense(Expense expense);
}
