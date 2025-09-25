package cloudflight.integra.backend.controller;

import cloudflight.integra.backend.controller.problem.ExpenseApiErrorResponses;
import cloudflight.integra.backend.dto.ExpenseDTO;
import cloudflight.integra.backend.entity.Expense;
import cloudflight.integra.backend.service.ExpenseService;
import java.util.List;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/** REST controller for managing {@link Expense} entities. */
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
   * Creates a new expense.
   *
   * @param dto the {@link ExpenseDTO} representing the expense to create
   * @return {@link ResponseEntity} containing the created expense with generated ID
   */
  @PostMapping
  public ResponseEntity<ExpenseDTO> createExpense(@RequestBody ExpenseDTO dto) {
    log.info("Creating expense: {}", dto);
    ExpenseDTO created = expenseService.createExpense(dto);
    return new ResponseEntity<>(created, HttpStatus.CREATED);
  }

  /**
   * Retrieves an expense by its ID.
   *
   * @param id the ID of the expense to retrieve
   * @return {@link ResponseEntity} containing the found {@link ExpenseDTO}
   */
  @GetMapping("/{id}")
  public ResponseEntity<ExpenseDTO> getExpenseById(@PathVariable Long id) {
    log.debug("Fetching expense with id {}", id);
    ExpenseDTO expense = expenseService.getExpense(id);
    return new ResponseEntity<>(expense, HttpStatus.OK);
  }

  /**
   * Retrieves all expenses.
   *
   * @return {@link ResponseEntity} containing the list of all {@link ExpenseDTO}
   */
  @GetMapping
  public ResponseEntity<List<ExpenseDTO>> getAllExpenses() {
    log.debug("Fetching all expenses");
    List<ExpenseDTO> expenses =
        StreamSupport.stream(expenseService.getAllExpenses().spliterator(), false).toList();
    return new ResponseEntity<>(expenses, HttpStatus.OK);
  }

  /**
   * Updates an existing expense.
   *
   * @param id the ID of the expense to update
   * @param dto the updated {@link ExpenseDTO}
   * @return {@link ResponseEntity} containing the updated expense
   * @throws IllegalArgumentException if the ID in the path does not match the ID in the DTO
   */
  @PutMapping("/{id}")
  public ResponseEntity<ExpenseDTO> updateExpense(
      @PathVariable Long id, @RequestBody ExpenseDTO dto) {
    log.info("Updating expense with id {}", id);
    dto.setId(id);
    expenseService.updateExpense(dto);
    return new ResponseEntity<>(dto, HttpStatus.OK);
  }

  /**
   * Deletes an expense by its ID.
   *
   * @param id the ID of the expense to delete
   * @return {@link ResponseEntity} with status 204 (No Content) if deleted successfully
   */
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteExpense(@PathVariable Long id) {
    log.warn("Deleting expense with id {}", id);
    expenseService.deleteExpense(id);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
}
