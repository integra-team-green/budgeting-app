package cloudflight.integra.backend.payment;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import cloudflight.integra.backend.entity.Expense;
import cloudflight.integra.backend.entity.Payment;
import cloudflight.integra.backend.entity.User;
import cloudflight.integra.backend.repository.ExpenseRepository;
import cloudflight.integra.backend.repository.PaymentRepository;
import cloudflight.integra.backend.repository.UserRepository;
import jakarta.validation.ConstraintViolationException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

// varianta buna
@DataJpaTest
public class PaymentRepoTest {

  @Autowired private PaymentRepository paymentRepository;

  @Autowired private ExpenseRepository expenseRepository;

  @Autowired private UserRepository userRepository;

  private User testUser;
  private Expense expense1;
  private Expense expense2;
  private Payment payment1;
  private Payment payment2;

  @BeforeEach
  void setUp() {
    paymentRepository.deleteAll();
    expenseRepository.deleteAll();
    userRepository.deleteAll();

    testUser = new User();
    testUser.setName("Test User");
    testUser = userRepository.saveAndFlush(testUser);

    // Expense 1
    expense1 = new Expense();
    expense1.setCategory("Rent");
    expense1.setAmount(BigDecimal.valueOf(1000));
    expense1.setDate(LocalDate.now());
    expense1.setFrequency(Expense.Frequency.MONTHLY);
    expense1.setPaymentMethod(Expense.PaymentMethod.CARD);
    expense1.setUser(testUser);
    expense1 = expenseRepository.saveAndFlush(expense1);

    // Expense 2
    expense2 = new Expense();
    expense2.setCategory("Rent");
    expense2.setAmount(BigDecimal.valueOf(1200));
    expense2.setDate(LocalDate.now().plusMonths(1));
    expense2.setFrequency(Expense.Frequency.MONTHLY);
    expense2.setPaymentMethod(Expense.PaymentMethod.CARD);
    expense2.setUser(testUser);
    expense2 = expenseRepository.saveAndFlush(expense2);

    // Payment 1
    payment1 = new Payment();
    payment1.setName("September Rent");
    payment1.setExpense(expense1);
    payment1.setAmount(new BigDecimal("500"));
    payment1.setStatus(Payment.StatusEnum.PENDING);
    payment1.setPaymentDate(LocalDate.of(2025, 9, 22));

    // Payment 2
    payment2 = new Payment();
    payment2.setName("October Rent");
    payment2.setExpense(expense2);
    payment2.setAmount(new BigDecimal("600"));
    payment2.setStatus(Payment.StatusEnum.PAID);
    payment2.setPaymentDate(LocalDate.of(2025, 10, 1));
  }

  @Test
  void testSaveFindById() {
    Payment saved = paymentRepository.save(payment1);
    Payment found =
        paymentRepository
            .findById(saved.getId())
            .orElseThrow(() -> new RuntimeException("Payment not found"));

    assertEquals("September Rent", found.getName());
    assertEquals(new BigDecimal("500"), found.getAmount());
    assertEquals(Payment.StatusEnum.PENDING, found.getStatus());
    assertEquals(LocalDate.of(2025, 9, 22), found.getPaymentDate());
  }

  @Test
  void testFindAll() {
    List<Payment> payments = paymentRepository.findAll();
    assertEquals(0, payments.size());

    paymentRepository.save(payment1);
    paymentRepository.save(payment2);
    payments = paymentRepository.findAll();
    assertEquals(2, payments.size());
  }

  @Test
  void testDelete() {
    Payment saved1 = paymentRepository.saveAndFlush(payment1);
    Payment saved2 = paymentRepository.saveAndFlush(payment2);

    paymentRepository.deleteById(saved2.getId());
    paymentRepository.flush();

    List<Payment> payments = paymentRepository.findAll();
    assertEquals(1, payments.size());
    assertEquals("September Rent", payments.get(0).getName());
  }

  @Test
  void testUpdate() {
    Payment saved = paymentRepository.save(payment1);

    saved.setAmount(new BigDecimal("550"));
    saved.setStatus(Payment.StatusEnum.PAID);
    paymentRepository.save(saved);

    Payment updated = paymentRepository.findById(saved.getId()).get();
    assertEquals(new BigDecimal("550"), updated.getAmount());
    assertEquals(Payment.StatusEnum.PAID, updated.getStatus());
  }

  @Test
  void shouldNotAllowNegativeAmount() {
    Payment payment = new Payment();
    payment.setAmount(BigDecimal.valueOf(-100));
    payment.setName("Test Payment");
    payment.setPaymentDate(LocalDate.now());

    assertThrows(
        ConstraintViolationException.class,
        () -> {
          paymentRepository.saveAndFlush(payment);
        });
  }
}
