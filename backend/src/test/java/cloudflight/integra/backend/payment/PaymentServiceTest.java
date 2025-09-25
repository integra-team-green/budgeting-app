package cloudflight.integra.backend.payment;

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

// varianta buna
@SpringBootTest
@Transactional
public class PaymentServiceTest {

  @Autowired private PaymentService paymentService;

  @Autowired private ExpenseService expenseService;

  @Autowired private UserService userService;

  private User testUser;
  private ExpenseDTO expense1;
  private ExpenseDTO expense2;
  private PaymentDTO payment1;
  private PaymentDTO payment2;

  @BeforeEach
  void setUp() {
    paymentService.getAllPayments().forEach(p -> paymentService.deletePayment(p.getId()));
    expenseService.getAllExpenses().forEach(e -> expenseService.deleteExpense(e.getId()));
    userService.getAllUsers().forEach(u -> userService.deleteUser(u.getId()));
    // Creăm un user
    testUser = new User();
    testUser.setName("Test User");
    testUser.setEmail("testuser+" + UUID.randomUUID() + "@example.com");
    testUser.setPassword("password");
    testUser = userService.addUser(testUser);

    // Expense 1 pentru Payment 1
    expense1 = new ExpenseDTO();
    expense1.setCategory("Rent");
    expense1.setAmount(BigDecimal.valueOf(1000));
    expense1.setDate(LocalDate.now());
    expense1.setFrequency(ExpenseDTO.Frequency.MONTHLY);
    expense1.setPaymentMethod(ExpenseDTO.PaymentMethod.CARD);
    expense1.setUserId(testUser.getId());
    expense1 = expenseService.createExpense(expense1);

    payment1 = new PaymentDTO();
    payment1.setName("September Rent");
    payment1.setAmount(BigDecimal.valueOf(500));
    payment1.setStatus(Payment.StatusEnum.PENDING);
    payment1.setPaymentDate(LocalDate.of(2025, 9, 22));
    payment1.setExpense(expense1);
    payment1 = paymentService.addPayment(payment1);

    // Expense 2 pentru Payment 2
    expense2 = new ExpenseDTO();
    expense2.setCategory("Utilities");
    expense2.setAmount(BigDecimal.valueOf(200));
    expense2.setDate(LocalDate.now());
    expense2.setFrequency(ExpenseDTO.Frequency.MONTHLY);
    expense2.setPaymentMethod(ExpenseDTO.PaymentMethod.TRANSFER);
    expense2.setUserId(testUser.getId());
    expense2 = expenseService.createExpense(expense2);

    payment2 = new PaymentDTO();
    payment2.setName("October Utilities");
    payment2.setAmount(BigDecimal.valueOf(200));
    payment2.setStatus(Payment.StatusEnum.PAID);
    payment2.setPaymentDate(LocalDate.of(2025, 10, 1));
    payment2.setExpense(expense2);
    payment2 = paymentService.addPayment(payment2);
  }

  @Test
  void testAddAndGetPayment() {
    PaymentDTO found = paymentService.getPaymentById(payment1.getId());

    assertThat(found.getName()).isEqualTo("September Rent");
    assertThat(found.getAmount()).isEqualByComparingTo(new BigDecimal("500"));
    assertThat(found.getStatus()).isEqualTo(Payment.StatusEnum.PENDING);
    assertThat(found.getPaymentDate()).isEqualTo(LocalDate.of(2025, 9, 22));
    assertThat(found.getExpense().getCategory()).isEqualTo("Rent");
  }

  @Test
  void testFindAll() {
    List<PaymentDTO> payments = paymentService.getAllPayments();
    assertThat(payments).hasSize(2);
  }

  @Test
  void testDelete() {
    paymentService.deletePayment(payment2.getId());

    List<PaymentDTO> payments = paymentService.getAllPayments();
    assertThat(payments).hasSize(1);
    assertThat(payments.get(0).getName()).isEqualTo("September Rent");
  }

  @Test
  void testUpdate() {
    payment1.setAmount(new BigDecimal("550"));
    payment1.setStatus(Payment.StatusEnum.PAID);
    paymentService.updatePayment(payment1);

    PaymentDTO updated = paymentService.getPaymentById(payment1.getId());
    assertThat(updated.getAmount()).isEqualByComparingTo(new BigDecimal("550"));
    assertThat(updated.getStatus()).isEqualTo(Payment.StatusEnum.PAID);
  }

  @Test
  void testAddPaymentWithNegativeAmount() {
    PaymentDTO invalidPayment = new PaymentDTO();
    invalidPayment.setName("Invalid Payment");
    invalidPayment.setExpense(expense1);
    invalidPayment.setAmount(BigDecimal.valueOf(-100));
    invalidPayment.setStatus(Payment.StatusEnum.PENDING);
    invalidPayment.setPaymentDate(LocalDate.of(2025, 11, 1));

    ValidationException ex =
        assertThrows(ValidationException.class, () -> paymentService.addPayment(invalidPayment));
    assertThat(ex.getMessage()).contains("Amount must be greater than 0");
  }

  @Test
  void testUpdatePaymentWithNegativeAmount() {
    payment1.setAmount(BigDecimal.valueOf(-200));

    ValidationException ex =
        assertThrows(ValidationException.class, () -> paymentService.updatePayment(payment1));
    assertThat(ex.getMessage()).contains("Amount must be greater than 0");
  }

  @Test
  void testDeleteNonExistingPayment() {
    Long nonExistingId = 9999L;

    NotFoundException ex =
        assertThrows(NotFoundException.class, () -> paymentService.deletePayment(nonExistingId));
    assertThat(ex.getMessage()).contains("Payment not found");
  }
}
