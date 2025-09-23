package cloudflight.integra.backend.payment;
import cloudflight.integra.backend.dto.ExpenseDTO;
import cloudflight.integra.backend.dto.PaymentDTO;
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
import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest
public class PaymentServiceTest {

    @Autowired
    private PaymentRepository paymentRepository;

    @MockitoBean
    private PaymentValidator paymentValidator;

    private PaymentService paymentService;

    @Autowired
    private ExpenseRepository expenseRepository;

    private ExpenseDTO expense;
    private PaymentDTO payment1;
    private PaymentDTO payment2;

    @BeforeEach
    void setUp() {
        paymentService = new PaymentServiceImpl(paymentRepository, expenseRepository, paymentValidator);

        Expense expenseEntity = new Expense();
        expenseEntity.setCategory("Rent");
        expenseEntity.setAmount(BigDecimal.valueOf(1000));
        expenseEntity.setDate(LocalDate.now());
        expenseEntity = expenseRepository.saveAndFlush(expenseEntity);

        expense = new ExpenseDTO();
        expense.setId(expenseEntity.getId());
        expense.setCategory(expenseEntity.getCategory());
        expense.setAmount(expenseEntity.getAmount());
        expense.setDate(expenseEntity.getDate());

        payment1 = new PaymentDTO();
        payment1.setName("September Rent");
        payment1.setExpense(expense);
        payment1.setAmount(BigDecimal.valueOf(500));
        payment1.setStatus(Payment.StatusEnum.PENDING);
        payment1.setPaymentDate(LocalDate.of(2025, 9, 22));

        payment2 = new PaymentDTO();
        payment2.setName("October Rent");
        payment2.setExpense(expense);
        payment2.setAmount(BigDecimal.valueOf(600));
        payment2.setStatus(Payment.StatusEnum.PAID);
        payment2.setPaymentDate(LocalDate.of(2025, 10, 1));
    }

    @Test
    void testAddAndGetPayment() {
        PaymentDTO saved = paymentService.addPayment(payment1);
        PaymentDTO found = paymentService.getPaymentById(saved.getId());

        assertThat(found.getName()).isEqualTo("September Rent");
        assertThat(found.getAmount()).isEqualByComparingTo(new BigDecimal("500"));
        assertThat(found.getStatus()).isEqualTo(Payment.StatusEnum.PENDING);
        assertThat(found.getPaymentDate()).isEqualTo(LocalDate.of(2025, 9, 22));
        assertThat(found.getExpense().getCategory()).isEqualTo("Rent");
    }

    @Test
    void testFindAll() {
        paymentService.addPayment(payment1);
        paymentService.addPayment(payment2);

        List<PaymentDTO> payments = paymentService.getAllPayments();
        assertThat(payments).hasSize(2);
    }

    @Test
    void testDelete() {
        PaymentDTO saved1 = paymentService.addPayment(payment1);
        PaymentDTO saved2 = paymentService.addPayment(payment2);

        paymentService.deletePayment(saved2.getId());

        List<PaymentDTO> payments = paymentService.getAllPayments();
        assertThat(payments).hasSize(1);
        assertThat(payments.get(0).getName()).isEqualTo("September Rent");
    }

    @Test
    void testUpdate() {
        PaymentDTO saved = paymentService.addPayment(payment1);

        saved.setAmount(new BigDecimal("550"));
        saved.setStatus(Payment.StatusEnum.PAID);
        paymentService.updatePayment(saved);

        PaymentDTO updated = paymentService.getPaymentById(saved.getId());
        assertThat(updated.getAmount()).isEqualByComparingTo(new BigDecimal("550"));
        assertThat(updated.getStatus()).isEqualTo(Payment.StatusEnum.PAID);
    }

    @Test
    void testAddPaymentWithNegativeAmount() {
        PaymentDTO invalidPayment = new PaymentDTO();
        invalidPayment.setName("Invalid Payment");
        invalidPayment.setExpense(expense);
        invalidPayment.setAmount(BigDecimal.valueOf(-100)); //sumă negativă
        invalidPayment.setStatus(Payment.StatusEnum.PENDING);
        invalidPayment.setPaymentDate(LocalDate.of(2025, 11, 1));
        try {
            paymentService.addPayment(invalidPayment);
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage()).contains("amount must be positive");
        }
    }

    @Test
    void testUpdatePaymentWithNegativeAmount() {
        PaymentDTO saved = paymentService.addPayment(payment1);

        saved.setAmount(BigDecimal.valueOf(-200)); //sumă negativă

        try {
            paymentService.updatePayment(saved);
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage()).contains("amount must be positive");
        }
    }

    @Test
    void testDeleteNonExistingPayment() {
        Long nonExistingId = 9999L;

        try {
            paymentService.deletePayment(nonExistingId);
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage()).contains("Payment not found");
        }
    }

    @Test
    void shouldThrowExceptionForNegativeAmount() {
        PaymentDTO invalidPayment = new PaymentDTO();
        invalidPayment.setName("Invalid Payment");
        invalidPayment.setExpense(expense);
        invalidPayment.setAmount(BigDecimal.valueOf(-100)); //sumă negativă
        invalidPayment.setStatus(Payment.StatusEnum.PENDING);
        invalidPayment.setPaymentDate(LocalDate.now());

        assertThrows(IllegalArgumentException.class, () -> paymentService.addPayment(invalidPayment));
    }

}
