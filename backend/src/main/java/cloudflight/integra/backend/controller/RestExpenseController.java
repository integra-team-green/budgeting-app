package cloudflight.integra.backend.controller;

import cloudflight.integra.backend.dto.ExpenseDto;
import cloudflight.integra.backend.exceptions.ResourceNotFoundException;
import cloudflight.integra.backend.service.ExpenseService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing expenses.
 * Provides CRUD operations for {@link ExpenseDto}.
 */
@RestController
@RequestMapping("/api/v1/expenses")
public class RestExpenseController {

    private static final Logger log = LoggerFactory.getLogger(RestExpenseController.class);

    private final ExpenseService expenseService;

    public RestExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    /**
     * Get an expense by its ID.
     *
     * @param id ID of the expense
     * @return {@link ExpenseDto} if found
     */
    @GetMapping("/{id}")
    public ResponseEntity<ExpenseDto> getExpenseById(@PathVariable Long id) {
        log.info("Fetching expense with id={}", id);
        ExpenseDto expenseDto = expenseService.findById(id);
        log.debug("Found expense: {}", expenseDto);
        return ResponseEntity.ok(expenseDto);
    }

    /**
     * Create a new expense.
     *
     * @param expenseDto DTO of the expense to create
     * @return created {@link ExpenseDto}
     */
    @PostMapping
    public ResponseEntity<ExpenseDto> addExpense(@Valid @RequestBody ExpenseDto expenseDto) {
        log.info("Creating new expense for userId={}", expenseDto.getUserId());
        ExpenseDto created = expenseService.createExpense(expenseDto);
        log.debug("Created expense: {}", created);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleNotFound(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    /**
     * Get all expenses for a given user.
     *
     * @param userId ID of the user
     * @return list of {@link ExpenseDto}
     */
    @GetMapping
    public ResponseEntity<List<ExpenseDto>> getAllExpenses(@RequestParam Long userId) {
        log.info("Fetching all expenses for userId={}", userId);
        List<ExpenseDto> expenses = expenseService.findAllByUserId(userId);
        log.debug("Found {} expenses for userId={}", expenses.size(), userId);
        return ResponseEntity.ok(expenses);
    }

    /**
     * Update an existing expense.
     *
     * @param id         ID of the expense to update
     * @param expenseDto new values for the expense
     * @return updated {@link ExpenseDto}
     */
    @PutMapping("/{id}")
    public ResponseEntity<ExpenseDto> updateExpense(@PathVariable Long id,
                                                    @Valid @RequestBody ExpenseDto expenseDto) {
        log.info("Updating expense with id={}", id);
        if (!id.equals(expenseDto.getId())) {
            log.warn("ID in path {} does not match ID in body {}", id, expenseDto.getId());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(null);
        }
        ExpenseDto updated = expenseService.updateExpense(id, expenseDto);
        log.debug("Updated expense: {}", updated);
        return ResponseEntity.ok(updated);
    }

    /**
     * Delete an expense by its ID.
     *
     * @param id ID of the expense to delete
     * @return 200 OK if deleted
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExpense(@PathVariable Long id) {
        log.info("Deleting expense with id={}", id);
        expenseService.deleteExpense(id);
        log.debug("Deleted expense with id={}", id);
        return ResponseEntity.ok().build();
    }
}
