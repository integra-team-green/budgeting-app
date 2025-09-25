package cloudflight.integra.backend.expense;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import cloudflight.integra.backend.dto.ExpenseDTO;
import cloudflight.integra.backend.dto.PaymentDTO;
import cloudflight.integra.backend.entity.Payment;
import cloudflight.integra.backend.entity.User;
import cloudflight.integra.backend.entity.validation.ValidationException;
import cloudflight.integra.backend.exception.NotFoundException;
import cloudflight.integra.backend.service.ExpenseService;
import cloudflight.integra.backend.service.PaymentService;
import cloudflight.integra.backend.service.UserService;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Transactional
public class ExpenseServiceImplementationTest {

  @Autowired private ExpenseService expenseService;

  @Autowired private PaymentService paymentService;

  @Autowired private UserService userService;

  private User testUser;
  private ExpenseDTO expense;
  private PaymentDTO payment;

  @BeforeEach
  void setUp() {
    expenseService.getAllExpenses().forEach(e -> expenseService.deleteExpense(e.getId()));
    paymentService.getAllPayments().forEach(p -> paymentService.deletePayment(p.getId()));
    userService.getAllUsers().forEach(u -> userService.deleteUser(u.getId()));

    // User
    testUser = new User();
    testUser.setName("Expense Test User");
    testUser.setEmail("expenseuser+" + UUID.randomUUID() + "@example.com");
    testUser.setPassword("password");
    testUser = userService.addUser(testUser);

    // Expense
    expense = new ExpenseDTO();
    expense.setCategory("Rent");
    expense.setAmount(BigDecimal.valueOf(1200));
    expense.setDate(LocalDate.now());
    expense.setFrequency(ExpenseDTO.Frequency.MONTHLY);
    expense.setPaymentMethod(ExpenseDTO.PaymentMethod.CARD);
    expense.setUserId(testUser.getId());

    // Payment
    payment = new PaymentDTO();
    payment.setName("Rent Payment");
    payment.setAmount(expense.getAmount());
    payment.setStatus(Payment.StatusEnum.PENDING);
    payment.setPaymentDate(LocalDate.now());
    payment.setExpense(expense);

    payment = paymentService.addPayment(payment);
    expense = payment.getExpense();
  }

  @Test
  void testAddExpense() {
    ExpenseDTO found = expenseService.getExpense(expense.getId());

    assertThat(found.getCategory()).isEqualTo("Rent");
    assertThat(found.getAmount()).isEqualByComparingTo(new BigDecimal("1200"));
    assertThat(found.getUserId()).isEqualTo(testUser.getId());
    assertThat(found.getPaymentMethod()).isEqualTo(ExpenseDTO.PaymentMethod.CARD);
  }

  @Test
  void testFindAllExpenses() {
    List<ExpenseDTO> expenses = (List<ExpenseDTO>) expenseService.getAllExpenses();
    assertThat(expenses).hasSize(1);
  }

  @Test
  void testUpdateExpense() {
    expense.setAmount(new BigDecimal("1500"));
    expense.setCategory("Updated Rent");
    expenseService.updateExpense(expense);

    ExpenseDTO updated = expenseService.getExpense(expense.getId());
    assertThat(updated.getAmount()).isEqualByComparingTo(new BigDecimal("1500"));
    assertThat(updated.getCategory()).isEqualTo("Updated Rent");
  }

  @Test
  void testDeleteExpense() {
    expenseService.deleteExpense(expense.getId());

    List<ExpenseDTO> expenses = (List<ExpenseDTO>) expenseService.getAllExpenses();
    assertThat(expenses).isEmpty();
  }

  @Test
  void testAddExpenseWithNegativeAmount() {
    ExpenseDTO invalidExpense = new ExpenseDTO();
    invalidExpense.setCategory("Invalid");
    invalidExpense.setAmount(BigDecimal.valueOf(-500));
    invalidExpense.setDate(LocalDate.now());
    invalidExpense.setFrequency(ExpenseDTO.Frequency.MONTHLY);
    invalidExpense.setPaymentMethod(ExpenseDTO.PaymentMethod.CARD);
    invalidExpense.setUserId(testUser.getId());

    ValidationException ex =
        assertThrows(ValidationException.class, () -> expenseService.createExpense(invalidExpense));
    assertThat(ex.getMessage()).contains("Amount must be greater than 0");
  }

  @Test
  void testDeleteNonExistingExpense() {
    Long nonExistingId = 9999L;

    NotFoundException ex =
        assertThrows(NotFoundException.class, () -> expenseService.deleteExpense(nonExistingId));
    assertThat(ex.getMessage()).contains("Expense not found");
  }
}
