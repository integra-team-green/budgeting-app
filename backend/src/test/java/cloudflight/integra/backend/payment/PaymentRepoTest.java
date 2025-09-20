package cloudflight.integra.backend.payment;
import cloudflight.integra.backend.entity.Expense;
import cloudflight.integra.backend.entity.Payment;
import cloudflight.integra.backend.repository.ExpenseRepository;
import cloudflight.integra.backend.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


@DataJpaTest
public class PaymentRepoTest {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private ExpenseRepository expenseRepository;

    private Expense expense;

    private Payment payment1;
    private Payment payment2;

    @BeforeEach
    void setUp() {
        //expense
        expense = new Expense();
        expense.setCategory("Rent");
        expense.setAmount(BigDecimal.valueOf(1000));
        expense.setDate(LocalDate.now());
        expenseRepository.saveAndFlush(expense);


        //payment 1
        payment1 = new Payment();
        payment1.setName("September Rent");
        payment1.setExpense(expense);
        payment1.setAmount(new BigDecimal("500"));
        payment1.setStatus(Payment.StatusEnum.PENDING);
        payment1.setPaymentDate(LocalDate.of(2025, 9, 22));

        //payment 2
        payment2 = new Payment();
        payment2.setName("October Rent");
        payment2.setExpense(expense);
        payment2.setAmount(new BigDecimal("600"));
        payment2.setStatus(Payment.StatusEnum.PAID);
        payment2.setPaymentDate(LocalDate.of(2025, 10, 1));

    }


    @Test
    void testSaveFindById() {
        Payment saved = paymentRepository.save(payment1);
        Payment found = paymentRepository.findById(saved.getId())
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
        Payment saved1 = paymentRepository.save(payment1);
        Payment saved2 = paymentRepository.save(payment2);

        paymentRepository.deleteById(saved2.getId());

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
        Payment invalidPayment = new Payment();
        invalidPayment.setName("Invalid");
        invalidPayment.setExpense(expense);
        invalidPayment.setAmount(new BigDecimal("-100")); //sumă negativă
        invalidPayment.setStatus(Payment.StatusEnum.PENDING);
        invalidPayment.setPaymentDate(LocalDate.now());

        assertThrows(DataIntegrityViolationException.class, () -> paymentRepository.saveAndFlush(invalidPayment));
    }
}
