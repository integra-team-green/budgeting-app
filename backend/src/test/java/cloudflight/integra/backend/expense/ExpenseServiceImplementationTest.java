package cloudflight.integra.backend.expense;

import cloudflight.integra.backend.dto.ExpenseDTO;
import cloudflight.integra.backend.entity.validation.ExpenseValidator;
import cloudflight.integra.backend.entity.validation.ValidationException;
import cloudflight.integra.backend.exception.NotFoundException;
import cloudflight.integra.backend.repository.inMemoryImpl.InMemoryExpenseRepository;
import cloudflight.integra.backend.service.impl.ExpenseServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link ExpenseServiceImpl}.
 * Covers CRUD operations using DTOs, validation, and exceptions.
 */
public class ExpenseServiceImplementationTest {

    private ExpenseServiceImpl service;

    @BeforeEach
    public void setUp() {
        service = new ExpenseServiceImpl(new InMemoryExpenseRepository(), new ExpenseValidator());
    }

    /** Test creating a valid expense. */
    @Test
    void shouldCreateExpense() {
        ExpenseDTO expense = new ExpenseDTO(null, 1L, new BigDecimal("100"), "Food", LocalDate.now(), "Lunch");
        ExpenseDTO created = service.createExpense(expense);

        assertNotNull(created.getId(), "Created expense should have an ID");
        assertEquals("Food", created.getCategory());
    }

    /** Test that creating an expense with invalid amount throws validation exception. */
    @Test
    void createExpenseWithInvalidAmountShouldThrow() {
        ExpenseDTO expense = new ExpenseDTO(null, 1L, new BigDecimal("-10"), "Food", LocalDate.now(), "Lunch");
        assertThrows(ValidationException.class, () -> service.createExpense(expense));
    }

    /** Test finding an existing expense by ID. */
    @Test
    void shouldFindById() {
        ExpenseDTO expense = service.createExpense(new ExpenseDTO(null, 1L, new BigDecimal("50"), "Coffee", LocalDate.now(), "Morning"));
        ExpenseDTO found = service.findById(expense.getId());

        assertEquals(expense.getId(), found.getId());
    }

    /** Test that finding a non-existing expense throws exception. */
    @Test
    void findByIdNonExistingShouldThrow() {
        assertThrows(NotFoundException.class, () -> service.findById(999L));
    }

    /** Test updating an existing expense. */
    @Test
    void shouldUpdateExpense() {
        ExpenseDTO expense = service.createExpense(new ExpenseDTO(null, 1L, new BigDecimal("50"), "Coffee", LocalDate.now(), "Morning"));
        expense.setAmount(new BigDecimal("60"));
        ExpenseDTO updated = service.updateExpense(expense.getId(), expense);

        assertEquals(new BigDecimal("60"), updated.getAmount());
    }

    /** Test that updating a non-existing expense throws exception. */
    @Test
    void updateNonExistingExpenseShouldThrow() {
        ExpenseDTO expense = new ExpenseDTO(999L, 1L, new BigDecimal("50"), "Coffee", LocalDate.now(), "Morning");
        assertThrows(NotFoundException.class, () -> service.updateExpense(expense.getId(), expense));
    }

    /** Test deleting an existing expense. */
    @Test
    void shouldDeleteExpense() {
        ExpenseDTO expense = service.createExpense(new ExpenseDTO(null, 1L, new BigDecimal("50"), "Coffee", LocalDate.now(), "Morning"));
        service.deleteExpense(expense.getId());
        assertThrows(NotFoundException.class, () -> service.findById(expense.getId()));
    }

    /** Test that deleting a non-existing expense throws exception. */
    @Test
    void deleteNonExistingExpenseShouldThrow() {
        assertThrows(NotFoundException.class, () -> service.deleteExpense(999L));
    }

    /** Test finding all expenses for a specific user. */
    @Test
    void shouldFindAllByUserId() {
        service.createExpense(new ExpenseDTO(null, 1L, new BigDecimal("50"), "Coffee", LocalDate.now(), "Morning"));
        service.createExpense(new ExpenseDTO(null, 1L, new BigDecimal("30"), "Snack", LocalDate.now(), "Evening"));
        service.createExpense(new ExpenseDTO(null, 2L, new BigDecimal("100"), "Food", LocalDate.now(), "Lunch"));

        List<ExpenseDTO> user1Expenses = service.findAllByUserId(1L);
        assertEquals(2, user1Expenses.size(), "User 1 should have 2 expenses");
    }
}
