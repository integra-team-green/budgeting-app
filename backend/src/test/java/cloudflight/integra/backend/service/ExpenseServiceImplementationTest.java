package cloudflight.integra.backend.service;

import cloudflight.integra.backend.entity.Expense;
import cloudflight.integra.backend.entity.validation.ExpenseValidation;
import cloudflight.integra.backend.repository.impl.InMemoryExpenseRepository;
import cloudflight.integra.backend.service.impl.ExpenseServiceImplementation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ExpenseServiceImplementationTest {

    private ExpenseServiceImplementation service;

    @BeforeEach
    public void setUp() {
        service = new ExpenseServiceImplementation(new InMemoryExpenseRepository(), new ExpenseValidation());
    }

    @Test
    void shouldCreateExpense() {
        Expense expense = new Expense(null, 1L, new BigDecimal("100"), "Food", LocalDate.now(), "Lunch");
        Expense created = service.createExpense(expense);

        assertNotNull(created.getId());
        assertEquals("Food", created.getCategory());
    }

    @Test
    void createExpenseWithInvalidAmountShouldThrow() {
        Expense expense = new Expense(null, 1L, new BigDecimal("-10"), "Food", LocalDate.now(), "Lunch");
        assertThrows(RuntimeException.class, () -> service.createExpense(expense));
    }

    @Test
    void shouldFindById() {
        Expense expense = service.createExpense(new Expense(null, 1L, new BigDecimal("50"), "Coffee", LocalDate.now(), "Morning"));
        Expense found = service.findById(expense.getId());

        assertEquals(expense.getId(), found.getId());
    }

    @Test
    void findByIdNonExistingShouldThrow() {
        assertThrows(RuntimeException.class, () -> service.findById(999L));
    }

    @Test
    void shouldUpdateExpense() {
        Expense expense = service.createExpense(new Expense(null, 1L, new BigDecimal("50"), "Coffee", LocalDate.now(), "Morning"));
        expense.setAmount(new BigDecimal("60"));
        Expense updated = service.updateExpense(expense.getId(), expense);

        assertEquals(new BigDecimal("60"), updated.getAmount());
    }

    @Test
    void updateNonExistingExpenseShouldThrow() {
        Expense expense = new Expense(999L, 1L, new BigDecimal("50"), "Coffee", LocalDate.now(), "Morning");
        assertThrows(RuntimeException.class, () -> service.updateExpense(expense.getId(), expense));
    }

    @Test
    void shouldDeleteExpense() {
        Expense expense = service.createExpense(new Expense(null, 1L, new BigDecimal("50"), "Coffee", LocalDate.now(), "Morning"));
        service.deleteExpense(expense.getId());
        assertThrows(RuntimeException.class, () -> service.findById(expense.getId()));
    }

    @Test
    void deleteNonExistingExpenseShouldThrow() {
        assertThrows(RuntimeException.class, () -> service.deleteExpense(999L));
    }

    @Test
    void shouldFindAllByUserId() {
        service.createExpense(new Expense(null, 1L, new BigDecimal("50"), "Coffee", LocalDate.now(), "Morning"));
        service.createExpense(new Expense(null, 1L, new BigDecimal("30"), "Snack", LocalDate.now(), "Evening"));
        service.createExpense(new Expense(null, 2L, new BigDecimal("100"), "Food", LocalDate.now(), "Lunch"));

        List<Expense> user1Expenses = service.findAllByUserId(1L);
        assertEquals(2, user1Expenses.size());
    }
}
