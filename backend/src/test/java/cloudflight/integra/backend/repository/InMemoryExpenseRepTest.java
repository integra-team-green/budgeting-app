package cloudflight.integra.backend.repository;

import cloudflight.integra.backend.entity.Expense;
import cloudflight.integra.backend.repository.impl.InMemoryExpenseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryExpenseRepTest {

    private InMemoryExpenseRepository repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryExpenseRepository();
    }

    @Test
    void shouldSaveAndFindById() {
        Expense expense = new Expense(null, 1L, new BigDecimal("100"), "Food", LocalDate.now(), "Lunch");
        Expense saved = repository.saveExpense(expense);

        assertNotNull(saved.getId());

        Optional<Expense> found = repository.findById(saved.getId());
        assertTrue(found.isPresent());
        assertEquals("Food", found.get().getCategory());
    }

    @Test
    void shouldReturnAllExpensesForUser() {
        repository.saveExpense(new Expense(null, 1L, new BigDecimal("50"), "Transport", LocalDate.now(), null));
        repository.saveExpense(new Expense(null, 2L, new BigDecimal("30"), "Food", LocalDate.now(), null));
        repository.saveExpense(new Expense(null, 1L, new BigDecimal("20"), "Coffee", LocalDate.now(), null));

        List<Expense> user1Expenses = repository.findAllByUserId(1L);
        assertEquals(2, user1Expenses.size());
    }

    @Test
    void shouldUpdateExistingExpense() {
        Expense expense = repository.saveExpense(new Expense(null, 1L, new BigDecimal("25"), "Snack", LocalDate.now(), null));
        expense.setAmount(new BigDecimal("30"));
        Expense updated = repository.updateExpense(expense);

        assertEquals(new BigDecimal("30"), updated.getAmount());
    }

    @Test
    void updateNonExistingExpenseShouldThrow() {
        Expense expense = new Expense(999L, 1L, new BigDecimal("30"), "Snack", LocalDate.now(), null);
        assertThrows(IllegalArgumentException.class, () -> repository.updateExpense(expense));
    }

    @Test
    void shouldDeleteExistingExpense() {
        Expense expense = repository.saveExpense(new Expense(null, 1L, new BigDecimal("15"), "Snack", LocalDate.now(), null));
        Long id = expense.getId();

        repository.deleteById(id);
        assertFalse(repository.findById(id).isPresent());
    }

    @Test
    void deleteNonExistingExpenseShouldThrow() {
        assertThrows(IllegalArgumentException.class, () -> repository.deleteById(999L));
    }
}
