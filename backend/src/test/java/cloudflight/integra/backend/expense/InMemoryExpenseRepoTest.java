package cloudflight.integra.backend.expense;

import cloudflight.integra.backend.dto.ExpenseDTO;
import cloudflight.integra.backend.repository.inMemoryImpl.InMemoryExpenseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link InMemoryExpenseRepository}.
 *
 * <p>Tests cover CRUD operations:
 * - addExpense
 * - updateExpense
 * - findById
 * - findAllByUserId
 * - deleteById
 * Also verifies that exceptions are thrown for invalid operations.
 */
public class InMemoryExpenseRepoTest {

    private InMemoryExpenseRepository repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryExpenseRepository();
    }

    /**
     * Test that an expense can be added and retrieved by ID.
     */
    @Test
    void shouldSaveAndFindById() {
        ExpenseDTO expense = new ExpenseDTO(null, 1L, new BigDecimal("100"), "Food", LocalDate.now(), "Lunch");
        ExpenseDTO saved = repository.addExpense(expense);

        assertNotNull(saved.getId(), "Saved expense should have an ID");

        Optional<ExpenseDTO> found = repository.findById(saved.getId());
        assertTrue(found.isPresent(), "Expense should be found by ID");
        assertEquals("Food", found.get().getCategory());
    }

    /**
     * Test that all expenses for a given user are returned.
     */
    @Test
    void shouldReturnAllExpensesForUser() {
        repository.addExpense(new ExpenseDTO(null, 1L, new BigDecimal("50"), "Transport", LocalDate.now(), null));
        repository.addExpense(new ExpenseDTO(null, 2L, new BigDecimal("30"), "Food", LocalDate.now(), null));
        repository.addExpense(new ExpenseDTO(null, 1L, new BigDecimal("20"), "Coffee", LocalDate.now(), null));

        List<ExpenseDTO> user1Expenses = repository.findAllByUserId(1L);
        assertEquals(2, user1Expenses.size(), "User 1 should have 2 expenses");
    }

    /**
     * Test updating an existing expense.
     */
    @Test
    void shouldUpdateExistingExpense() {
        ExpenseDTO expense = repository.addExpense(new ExpenseDTO(null, 1L, new BigDecimal("25"), "Snack", LocalDate.now(), null));
        expense.setAmount(new BigDecimal("30"));
        ExpenseDTO updated = repository.updateExpense(expense);

        assertEquals(new BigDecimal("30"), updated.getAmount(), "Amount should be updated to 30");
    }

    /**
     * Test that updating a non-existing expense throws an exception.
     */
    @Test
    void updateNonExistingExpenseShouldThrow() {
        ExpenseDTO expense = new ExpenseDTO(999L, 1L, new BigDecimal("30"), "Snack", LocalDate.now(), null);
        assertThrows(IllegalArgumentException.class, () -> repository.updateExpense(expense));
    }

    /**
     * Test deleting an existing expense.
     */
    @Test
    void shouldDeleteExistingExpense() {
        ExpenseDTO expense = repository.addExpense(new ExpenseDTO(null, 1L, new BigDecimal("15"), "Snack", LocalDate.now(), null));
        Long id = expense.getId();

        repository.deleteById(id);
        assertFalse(repository.findById(id).isPresent(), "Expense should be deleted");
    }

    /**
     * Test that deleting a non-existing expense throws an exception.
     */
    @Test
    void deleteNonExistingExpenseShouldThrow() {
        assertThrows(IllegalArgumentException.class, () -> repository.deleteById(999L));
    }
}
