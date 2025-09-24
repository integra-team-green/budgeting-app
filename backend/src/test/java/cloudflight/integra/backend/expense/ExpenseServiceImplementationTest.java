package cloudflight.integra.backend.expense;

import cloudflight.integra.backend.dto.ExpenseDTO;
import cloudflight.integra.backend.entity.Expense;
import cloudflight.integra.backend.entity.User;
import cloudflight.integra.backend.entity.validation.ValidationException;
import cloudflight.integra.backend.repository.ExpenseRepository;
import cloudflight.integra.backend.repository.UserRepository;
import cloudflight.integra.backend.service.ExpenseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ExpenseServiceImplementationTest {

    @Autowired
    private ExpenseService service;

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private UserRepository userRepository;

    private User user1, user2;

    @BeforeEach
    void setUp() {
        expenseRepository.deleteAll();
        userRepository.deleteAll();

        user1 = userRepository.save(new User(null, "Alice", "alice@email.com", "123", LocalDate.now(), BigDecimal.valueOf(500)));
        user2 = userRepository.save(new User(null, "Marc", "marc@yahoo.com", "abcd999", LocalDate.now(), BigDecimal.valueOf(800)));
    }

    @Test
    void createExpense_withValidData_succeeds() {
        ExpenseDTO expenseDTO = new ExpenseDTO(
                null,
                user1.getId(),
                BigDecimal.valueOf(150),
                "Food",
                LocalDate.now(),
                "Lunch",
                ExpenseDTO.Frequency.MONTHLY,
                null,
                null,
                ExpenseDTO.PaymentMethod.CARD
        );

        assertDoesNotThrow(() -> service.createExpense(expenseDTO));

        List<ExpenseDTO> expenses = (List<ExpenseDTO>) service.getAllExpenses();
        assertFalse(expenses.isEmpty());
    }

    @Test
    void createExpense_withNegativeAmount_throwsValidationException() {
        ExpenseDTO expenseDTO = new ExpenseDTO(
                null,
                user1.getId(),
                BigDecimal.valueOf(-100),
                "Food",
                LocalDate.now(),
                "Lunch",
                ExpenseDTO.Frequency.MONTHLY,
                null,
                null,
                ExpenseDTO.PaymentMethod.CARD
        );

        ValidationException ex = assertThrows(ValidationException.class, () -> service.createExpense(expenseDTO));
        ex.getErrors().forEach(err -> System.out.println("Validation error: " + err));
    }

    @Test
    void createExpense_withNullCategory_throwsValidationException() {
        ExpenseDTO expenseDTO = new ExpenseDTO(
                null,
                user1.getId(),
                BigDecimal.valueOf(100),
                null,
                LocalDate.now(),
                "Dinner",
                ExpenseDTO.Frequency.MONTHLY,
                null,
                null,
                ExpenseDTO.PaymentMethod.CARD
        );

        ValidationException ex = assertThrows(ValidationException.class, () -> service.createExpense(expenseDTO));
        ex.getErrors().forEach(err -> System.out.println("Validation error: " + err));
    }

    @Test
    void getAllExpenses_returnsAllCreatedExpenses() {
        ExpenseDTO expense1 = new ExpenseDTO(null, user1.getId(), BigDecimal.valueOf(100), "Food", LocalDate.now(), "Lunch", ExpenseDTO.Frequency.MONTHLY, null, null, ExpenseDTO.PaymentMethod.CARD);
        ExpenseDTO expense2 = new ExpenseDTO(null, user2.getId(), BigDecimal.valueOf(200), "Transport", LocalDate.now(), "Taxi", ExpenseDTO.Frequency.ONE_TIME, null, null, ExpenseDTO.PaymentMethod.CARD);

        service.createExpense(expense1);
        service.createExpense(expense2);

        List<ExpenseDTO> all = (List<ExpenseDTO>) service.getAllExpenses();

        assertEquals(2, all.size());
    }

    @Test
    void getExpenseById_returnsCorrectExpense() {
        ExpenseDTO expenseDTO = new ExpenseDTO(null, user1.getId(), BigDecimal.valueOf(500), "Rent", LocalDate.now(), "Monthly rent", ExpenseDTO.Frequency.MONTHLY, null, null, ExpenseDTO.PaymentMethod.TRANSFER);
        ExpenseDTO created = service.createExpense(expenseDTO);

        ExpenseDTO found = service.getExpense(created.getId());

        assertEquals(0, found.getAmount().compareTo(BigDecimal.valueOf(500)));
        assertEquals("Rent", found.getCategory());
    }

    @Test
    void updateExpense_withValidData_succeeds() {
        ExpenseDTO expenseDTO = new ExpenseDTO(null, user1.getId(), BigDecimal.valueOf(300), "Utilities", LocalDate.now(), "Electric bill", ExpenseDTO.Frequency.MONTHLY, null, null, ExpenseDTO.PaymentMethod.TRANSFER);
        ExpenseDTO created = service.createExpense(expenseDTO);

        created.setAmount(BigDecimal.valueOf(350));
        created.setDescription("Updated electric bill");

        assertDoesNotThrow(() -> service.updateExpense(created));

        ExpenseDTO updated = service.getExpense(created.getId());
        assertEquals(0, updated.getAmount().compareTo(BigDecimal.valueOf(350)));
        assertEquals("Updated electric bill", updated.getDescription());
    }

    @Test
    void deleteExpense_removesExpense() {
        ExpenseDTO expenseDTO = new ExpenseDTO(null, user1.getId(), BigDecimal.valueOf(400), "Shopping", LocalDate.now(), "Clothes", ExpenseDTO.Frequency.ONE_TIME, null, null, ExpenseDTO.PaymentMethod.CARD);
        ExpenseDTO created = service.createExpense(expenseDTO);

        service.deleteExpense(created.getId());

        assertThrows(RuntimeException.class, () -> service.getExpense(created.getId()));
    }
}
