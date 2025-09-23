package cloudflight.integra.backend.expense;

import cloudflight.integra.backend.entity.Expense;
import cloudflight.integra.backend.entity.Expense.Frequency;
import cloudflight.integra.backend.entity.Expense.PaymentMethod;
import cloudflight.integra.backend.entity.User;
import cloudflight.integra.backend.entity.validation.ValidationException;
import cloudflight.integra.backend.exception.NotFoundException;
import cloudflight.integra.backend.repository.ExpenseRepository;
import cloudflight.integra.backend.repository.UserRepository;
import cloudflight.integra.backend.service.impl.ExpenseServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link ExpenseServiceImpl}.
 * Covers CRUD operations with valid and invalid attributes, including new fields:
 * frequency, paymentMethod, endDate, nextDueDate.
 */
@SpringBootTest
public class ExpenseServiceImplementationTest {

    @Autowired
    private ExpenseServiceImpl expenseService;

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private UserRepository userRepository;

    private User user1, user2;

    @BeforeEach
    void setUp() {
        expenseRepository.deleteAll();
        userRepository.deleteAll();

        user1 = userRepository.save(new User(null, "Alice", "alice@email.com", "123"));
        user2 = userRepository.save(new User(null, "Bob", "bob@email.com", "456"));
    }

    /** Test creating a valid expense with all required fields including new enums. */
    @Test
    void createExpense_withValidData_succeeds() {
        Expense expense = new Expense(null, user1, BigDecimal.valueOf(100), "Food",
                LocalDate.now(), "Lunch", Frequency.ONE_TIME, null, null, PaymentMethod.CARD, null);

        assertDoesNotThrow(() -> expenseService.createExpense(expense));
        assertNotNull(expense.getId(), "ID should be generated automatically");
    }

    /** Test that creating an expense with negative amount throws ValidationException. */
    @Test
    void createExpense_withNegativeAmount_throwsValidationException() {
        Expense expense = new Expense(null, user1, BigDecimal.valueOf(-50), "Food",
                LocalDate.now(), "Invalid lunch", Frequency.MONTHLY, null, null, PaymentMethod.TRANSFER, null);

        ValidationException ex = assertThrows(ValidationException.class,
                () -> expenseService.createExpense(expense));

        ex.getErrors().forEach(err -> System.out.println("Validation error: " + err));
    }

    /** Test that creating an expense with null category throws ValidationException. */
    @Test
    void createExpense_withNullCategory_throwsValidationException() {
        Expense expense = new Expense(null, user1, BigDecimal.valueOf(50), null,
                LocalDate.now(), "No category", Frequency.MONTHLY, null, null, PaymentMethod.CARD, null);

        ValidationException ex = assertThrows(ValidationException.class,
                () -> expenseService.createExpense(expense));

        ex.getErrors().forEach(err -> System.out.println("Validation error: " + err));
    }

    /** Test retrieving all expenses in repository. */
    @Test
    void getAllExpenses_returnsAllCreatedExpenses() {
        expenseService.createExpense(
                new Expense(null, user1, BigDecimal.valueOf(100), "Food", LocalDate.now(),
                        "Lunch", Frequency.MONTHLY, null, null, PaymentMethod.CARD, null));

        expenseService.createExpense(
                new Expense(null, user2, BigDecimal.valueOf(200), "Transport", LocalDate.now(),
                        "Bus ticket", Frequency.ONE_TIME, null, null, PaymentMethod.TRANSFER, null));

        List<Expense> allExpenses = expenseRepository.findAll();
        assertEquals(2, allExpenses.size(), "There should be 2 expenses in repository");
    }

    /** Test finding an expense by ID returns correct data including enums. */
    @Test
    void getExpenseById_returnsCorrectExpense() {
        Expense expense = expenseService.createExpense(
                new Expense(null, user1, BigDecimal.valueOf(150), "Coffee", LocalDate.now(),
                        "Morning coffee", Frequency.ONE_TIME, null, null, PaymentMethod.CARD, null));

        Expense found = expenseService.getExpense(expense.getId());
        assertEquals(expense.getId(), found.getId());
        assertEquals(expense.getAmount(), found.getAmount());
        assertEquals(expense.getCategory(), found.getCategory());
        assertEquals(expense.getFrequency(), found.getFrequency());
        assertEquals(expense.getPaymentMethod(), found.getPaymentMethod());
    }

    /** Test updating an existing expense with valid data. */
    @Test
    void updateExpense_withValidData_succeeds() {
        Expense expense = expenseService.createExpense(
                new Expense(null, user1, BigDecimal.valueOf(50), "Snack", LocalDate.now(),
                        "Evening snack", Frequency.MONTHLY, null, null, PaymentMethod.TRANSFER, null));

        expense.setAmount(BigDecimal.valueOf(70));
        assertDoesNotThrow(() -> expenseService.updateExpense(expense));

        Expense updated = expenseService.getExpense(expense.getId());
        assertEquals(BigDecimal.valueOf(70), updated.getAmount());
    }

    /** Test deleting an existing expense removes it from repository. */
    @Test
    void deleteExpense_removesExpense() {
        Expense expense = expenseService.createExpense(
                new Expense(null, user1, BigDecimal.valueOf(80), "Coffee", LocalDate.now(),
                        "Morning coffee", Frequency.ONE_TIME, null, null, PaymentMethod.CARD, null));

        expenseService.deleteExpense(expense.getId());
        assertThrows(NotFoundException.class, () -> expenseService.getExpense(expense.getId()));
    }
}
