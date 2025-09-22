package cloudflight.integra.backend.payment;
import cloudflight.integra.backend.entity.Expense;
import cloudflight.integra.backend.entity.Payment;
import cloudflight.integra.backend.entity.validation.PaymentValidator;
import cloudflight.integra.backend.repository.ExpenseRepository;
import cloudflight.integra.backend.repository.PaymentRepository;
import cloudflight.integra.backend.service.PaymentService;
import cloudflight.integra.backend.service.impl.PaymentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
public class PaymentServiceTest {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private ExpenseRepository expenseRepository;

    @MockitoBean
    private PaymentValidator paymentValidator;

    private PaymentService paymentService;

    private Expense expense;
    private Payment payment1;
    private Payment payment2;

    @BeforeEach
    void setUp() {
        paymentService = new PaymentServiceImpl(paymentRepository, paymentValidator);

        expense = new Expense();
        expense.setCategory("Rent");
        expense.setAmount(BigDecimal.valueOf(1000));
        expense.setDate(LocalDate.now());
        expense = expenseRepository.save(expense);


        payment1 = new Payment();
        payment1.setName("September Rent");
        payment1.setExpense(expense);
        payment1.setAmount(new BigDecimal("500"));
        payment1.setStatus(Payment.Status.PENDING);
        payment1.setPaymentDate(LocalDate.of(2025, 9, 22));

        payment2 = new Payment();
        payment2.setName("October Rent");
        payment2.setExpense(expense);
        payment2.setAmount(new BigDecimal("600"));
        payment2.setStatus(Payment.Status.PAID);
        payment2.setPaymentDate(LocalDate.of(2025, 10, 1));
    }

    @Test
    void testAddAndGetPayment() {
        Payment saved = paymentService.addPayment(payment1);
        Payment found = paymentService.getPayment(saved.getId());

        assertThat(found.getName()).isEqualTo("September Rent");
        assertThat(found.getAmount()).isEqualByComparingTo(new BigDecimal("500"));
        assertThat(found.getStatus()).isEqualTo(Payment.Status.PENDING);
        assertThat(found.getPaymentDate()).isEqualTo(LocalDate.of(2025, 9, 22));
        assertThat(found.getExpense().getCategory()).isEqualTo("Rent");
    }

    @Test
    void testFindAll() {
        paymentService.addPayment(payment1);
        paymentService.addPayment(payment2);

        List<Payment> payments = paymentService.getPayments();
        assertThat(payments).hasSize(2);
    }

    @Test
    void testDelete() {
        Payment saved1 = paymentService.addPayment(payment1);
        Payment saved2 = paymentService.addPayment(payment2);

        paymentService.deletePayment(saved2.getId());

        List<Payment> payments = paymentService.getPayments();
        assertThat(payments).hasSize(1);
        assertThat(payments.get(0).getName()).isEqualTo("September Rent");
    }

    @Test
    void testUpdate() {
        Payment saved = paymentService.addPayment(payment1);

        saved.setAmount(new BigDecimal("550"));
        saved.setStatus(Payment.Status.PAID);
        paymentService.updatePayment(saved);

        Payment updated = paymentService.getPayment(saved.getId());
        assertThat(updated.getAmount()).isEqualByComparingTo(new BigDecimal("550"));
        assertThat(updated.getStatus()).isEqualTo(Payment.Status.PAID);
    }
}
