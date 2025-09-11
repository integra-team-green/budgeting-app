package cloudflight.integra.backend.repository;

import cloudflight.integra.backend.dto.ExpenseDto;
import cloudflight.integra.backend.entity.Expense;

import java.util.List;
import java.util.Optional;

public interface ExpenseRepository {
    /**
     * Add a new expense to the repository.
     *
     * @param expenseDto the expense to add (id should be null for new expenses)
     * @return the created expense with generated id
     */
    ExpenseDto addExpense(ExpenseDto expenseDto);


    /**
     * Update an existing expense in the repository.
     *
     * @param expenseDto the object containing updated data (id must not be null)
     * @return the updated object
     * @throws IllegalArgumentException if the expense ID is null or does not exist in the repository
     */
    ExpenseDto updateExpense(ExpenseDto expenseDto);


    /**
     * Find all expenses for a specific user.
     *
     * @param userId the user ID
     * @return a list of expenses for the user
     */
    List<ExpenseDto> findAllByUserId(Long userId);

    /**
     * Find an expense by its ID.
     *
     * @param id the expense ID
     */
    Optional<ExpenseDto> findById(Long id);


    /**
     * Delete an expense by its ID.
     *
     * @param id the expense ID to delete
     */
    void deleteById(Long id);


}
