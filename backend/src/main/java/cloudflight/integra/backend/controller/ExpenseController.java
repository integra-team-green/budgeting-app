package cloudflight.integra.backend.controller;

import cloudflight.integra.backend.controller.problem.ExpenseApiErrorResponses;
import cloudflight.integra.backend.dto.ExpenseDTO;
import cloudflight.integra.backend.entity.Expense;
import cloudflight.integra.backend.mapper.ExpenseMapper;
import cloudflight.integra.backend.service.ExpenseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing {@link Expense} entities.
 */
@RestController
@ExpenseApiErrorResponses
@RequestMapping("/api/v1/expenses")
public class ExpenseController {

    private static final Logger log = LoggerFactory.getLogger(ExpenseController.class);

    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    /**
     * Get an expense by its ID.
     *
     * @param id the ID of the expense to retrieve
     * @return the {@link Expense} if found
     */
    @GetMapping("/{id}")
    public ResponseEntity<Expense> getExpense(@PathVariable Long id) {
        log.info("Fetching expense with id={}", id);
        ExpenseDTO expenseDTO = expenseService.getExpense(id);
        Expense expense = ExpenseMapper.getFromDto(expenseDTO);
        log.info("Expense retrieved: {}", expense);
        return ResponseEntity.ok(expense);
    }

    /**
     * Get all expenses for a specific user.
     *
     * @param userId the ID of the user whose expenses to retrieve
     * @return list of {@link Expense} objects
     */
    @GetMapping
    public ResponseEntity<List<Expense>> getAllExpenses(@RequestParam Long userId) {
        log.info("Fetching all expenses for userId={}", userId);
        List<ExpenseDTO> expenseDTOs = expenseService.getAllExpensesByUser(userId);
        List<Expense> expenses = ExpenseMapper.getExpenseFromDto(expenseDTOs);
        log.info("Found {} expenses", expenses.size());
        return ResponseEntity.ok(expenses);
    }

    /**
     * Create a new expense.
     *
     * @param expense the expense to create
     * @return the created {@link Expense} with generated ID
     */
    @PostMapping
    public ResponseEntity<Expense> addExpense(@RequestBody Expense expense) {
        log.info("Creating new expense for userId={}", expense.getUser().getId());
        ExpenseDTO expenseDTO = ExpenseMapper.getDto(expense);
        ExpenseDTO createdDTO = expenseService.createExpense(expenseDTO);
        Expense created = ExpenseMapper.getFromDto(createdDTO);
        log.info("Expense created: {}", created);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * Update an existing expense.
     *
     * @param id      the ID of the expense to update
     * @param expense the updated expense object
     * @return the updated {@link Expense}
     * @throws IllegalArgumentException if the ID in the path and in the body do not match
     */
    @PutMapping("/{id}")
    public ResponseEntity<Expense> updateExpense(@PathVariable Long id, @RequestBody Expense expense) {
        log.info("Updating expense with id={}", id);
        if (!id.equals(expense.getId())) {
            throw new IllegalArgumentException("ID in path and body do not match.");
        }
        ExpenseDTO expenseDTO = ExpenseMapper.getDto(expense);
        ExpenseDTO updatedDTO = expenseService.updateExpense(expenseDTO);
        Expense updated = ExpenseMapper.getFromDto(updatedDTO);
        log.info("Expense updated: {}", updated);
        return ResponseEntity.ok(updated);
    }

    /**
     * Delete an expense by its ID.
     *
     * @param id the ID of the expense to delete
     * @return 204 No Content if deleted successfully
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExpense(@PathVariable Long id) {
        log.info("Deleting expense with id={}", id);
        expenseService.deleteExpense(id);
        log.info("Expense deleted");
        return ResponseEntity.noContent().build();
    }
}
