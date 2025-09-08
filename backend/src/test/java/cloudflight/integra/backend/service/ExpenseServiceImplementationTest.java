package cloudflight.integra.backend.service;

import cloudflight.integra.backend.dto.ExpenseDto;
import cloudflight.integra.backend.entity.validation.ExpenseValidation;
import cloudflight.integra.backend.entity.validation.ValidationExceptionExpense;
import cloudflight.integra.backend.repository.impl.InMemoryExpenseRepository;
import cloudflight.integra.backend.service.impl.ExpenseServiceImplementation;
import cloudflight.integra.backend.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link ExpenseServiceImplementation}.
 * Covers CRUD operations using DTOs, validation, and exceptions.
 */
public class ExpenseServiceImplementationTest {

    private ExpenseServiceImplementation service;

    @BeforeEach
    public void setUp() {
        service = new ExpenseServiceImplementation(new InMemoryExpenseRepository(), new ExpenseValidation());
    }

    /** Test creating a valid expense. */
    @Test
    void shouldCreateExpense() {
        ExpenseDto expense = new ExpenseDto(null, 1L, new BigDecimal("100"), "Food", LocalDate.now(), "Lunch");
        ExpenseDto created = service.createExpense(expense);

        assertNotNull(created.getId(), "Created expense should have an ID");
        assertEquals("Food", created.getCategory());
    }

    /** Test that creating an expense with invalid amount throws validation exception. */
    @Test
    void createExpenseWithInvalidAmountShouldThrow() {
        ExpenseDto expense = new ExpenseDto(null, 1L, new BigDecimal("-10"), "Food", LocalDate.now(), "Lunch");
        assertThrows(ValidationExceptionExpense.class, () -> service.createExpense(expense));
    }

    /** Test finding an existing expense by ID. */
    @Test
    void shouldFindById() {
        ExpenseDto expense = service.createExpense(new ExpenseDto(null, 1L, new BigDecimal("50"), "Coffee", LocalDate.now(), "Morning"));
        ExpenseDto found = service.findById(expense.getId());

        assertEquals(expense.getId(), found.getId());
    }

    /** Test that finding a non-existing expense throws exception. */
    @Test
    void findByIdNonExistingShouldThrow() {
        assertThrows(ResourceNotFoundException.class, () -> service.findById(999L));
    }

    /** Test updating an existing expense. */
    @Test
    void shouldUpdateExpense() {
        ExpenseDto expense = service.createExpense(new ExpenseDto(null, 1L, new BigDecimal("50"), "Coffee", LocalDate.now(), "Morning"));
        expense.setAmount(new BigDecimal("60"));
        ExpenseDto updated = service.updateExpense(expense.getId(), expense);

        assertEquals(new BigDecimal("60"), updated.getAmount());
    }

    /** Test that updating a non-existing expense throws exception. */
    @Test
    void updateNonExistingExpenseShouldThrow() {
        ExpenseDto expense = new ExpenseDto(999L, 1L, new BigDecimal("50"), "Coffee", LocalDate.now(), "Morning");
        assertThrows(ResourceNotFoundException.class, () -> service.updateExpense(expense.getId(), expense));
    }

    /** Test deleting an existing expense. */
    @Test
    void shouldDeleteExpense() {
        ExpenseDto expense = service.createExpense(new ExpenseDto(null, 1L, new BigDecimal("50"), "Coffee", LocalDate.now(), "Morning"));
        service.deleteExpense(expense.getId());
        assertThrows(ResourceNotFoundException.class, () -> service.findById(expense.getId()));
    }

    /** Test that deleting a non-existing expense throws exception. */
    @Test
    void deleteNonExistingExpenseShouldThrow() {
        assertThrows(ResourceNotFoundException.class, () -> service.deleteExpense(999L));
    }

    /** Test finding all expenses for a specific user. */
    @Test
    void shouldFindAllByUserId() {
        service.createExpense(new ExpenseDto(null, 1L, new BigDecimal("50"), "Coffee", LocalDate.now(), "Morning"));
        service.createExpense(new ExpenseDto(null, 1L, new BigDecimal("30"), "Snack", LocalDate.now(), "Evening"));
        service.createExpense(new ExpenseDto(null, 2L, new BigDecimal("100"), "Food", LocalDate.now(), "Lunch"));

        List<ExpenseDto> user1Expenses = service.findAllByUserId(1L);
        assertEquals(2, user1Expenses.size(), "User 1 should have 2 expenses");
    }
}
