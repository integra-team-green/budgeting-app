package cloudflight.integra.backend.service;

import cloudflight.integra.backend.dto.ExpenseDTO;

import java.util.List;

/**
 * Service interface for managing Expense DTOs.
 */
public interface ExpenseService {

    /**
     * Create a new expense after validation.
     *
     * @param expenseDTO the expense DTO to create
     * @return the created expense DTO
     */
    ExpenseDTO createExpense(ExpenseDTO expenseDTO);

    /**
     * Find an expense by its ID.
     *
     * @param id the ID of the expense
     * @return the expense DTO
     */
    ExpenseDTO getExpense(Long id);

    /**
     * Find all expenses for a specific user.
     *
     * @param userId the user ID
     * @return list of expense DTOs
     */
    List<ExpenseDTO> getAllExpensesByUser(Long userId);

    /**
     * Update an existing expense.
     *
     * @param expenseDTO the expense DTO with updated fields
     * @return the updated expense DTO
     */
    ExpenseDTO updateExpense(ExpenseDTO expenseDTO);

    /**
     * Delete an expense by ID.
     *
     * @param id the expense ID
     */
    void deleteExpense(Long id);
}
