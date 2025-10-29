package cloudflight.integra.backend.payment;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import cloudflight.integra.backend.dto.PaymentDTO;
import cloudflight.integra.backend.entity.Payment;
import cloudflight.integra.backend.entity.validation.PaymentValidator;
import cloudflight.integra.backend.entity.validation.ValidationException;
import cloudflight.integra.backend.exception.NotFoundException;
import cloudflight.integra.backend.repository.PaymentRepository;
import cloudflight.integra.backend.service.impl.PaymentServiceImpl;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class PaymentServiceTest {
    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private PaymentValidator paymentValidator;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    private Payment payment;
    private PaymentDTO paymentDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        payment = new Payment();
        payment.setId(1L);
        payment.setName("Rent Payment");
        payment.setStatus(Payment.StatusEnum.PENDING);
        payment.setPaymentDate(LocalDate.of(2025, 9, 22));

        paymentDTO = new PaymentDTO();
        paymentDTO.setId(payment.getId());
        paymentDTO.setName(payment.getName());
        paymentDTO.setStatus(payment.getStatus());
        paymentDTO.setPaymentDate(payment.getPaymentDate());
        paymentDTO.setExpenseId(10L);
    }

    @Test
    void testAddPayment() {
        doNothing().when(paymentValidator).validate(paymentDTO);
        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);

        PaymentDTO created = paymentService.addPayment(paymentDTO);

        assertThat(created).isNotNull();
        assertThat(created.getId()).isEqualTo(payment.getId());
        assertThat(created.getName()).isEqualTo("Rent Payment");
    }

    @Test
    void testGetPaymentById() {
        when(paymentRepository.findById(payment.getId())).thenReturn(Optional.of(payment));

        PaymentDTO found = paymentService.getPaymentById(payment.getId());
        assertThat(found.getName()).isEqualTo("Rent Payment");
    }

    @Test
    void testUpdatePayment() {
        doNothing().when(paymentValidator).validate(paymentDTO);
        when(paymentRepository.existsById(paymentDTO.getId())).thenReturn(true);
        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);

        PaymentDTO updated = paymentService.updatePayment(paymentDTO);
        assertThat(updated.getName()).isEqualTo("Rent Payment");
    }

    @Test
    void testGetAllPayments() {
        when(paymentRepository.findAll()).thenReturn(List.of(payment));

        List<PaymentDTO> all = paymentService.getAllPayments();
        assertThat(all).hasSize(1);
        assertThat(all.get(0).getName()).isEqualTo("Rent Payment");
    }

    @Test
    void testDeletePaymentNotFound() {
        when(paymentRepository.findById(payment.getId())).thenReturn(Optional.empty());

        NotFoundException ex =
                assertThrows(NotFoundException.class, () -> paymentService.deletePayment(payment.getId()));
        assertThat(ex.getMessage()).contains("Payment not found");
    }

    @Test
    void testAddPaymentWithInvalidAmount() {
        doThrow(new ValidationException("Amount must be greater than 0"))
                .when(paymentValidator)
                .validate(paymentDTO);

        ValidationException ex = assertThrows(ValidationException.class, () -> paymentService.addPayment(paymentDTO));
        assertThat(ex.getMessage()).contains("Amount must be greater than 0");
    }

    @Test
    void testGetPaymentByIdNotFound() {
        when(paymentRepository.findById(payment.getId())).thenReturn(Optional.empty());

        NotFoundException ex =
                assertThrows(NotFoundException.class, () -> paymentService.getPaymentById(payment.getId()));
        assertThat(ex.getMessage()).contains("Payment not found");
    }

    @Test
    void testUpdatePaymentNotFound() {
        doNothing().when(paymentValidator).validate(paymentDTO);
        when(paymentRepository.existsById(paymentDTO.getId())).thenReturn(false);

        NotFoundException ex = assertThrows(NotFoundException.class, () -> paymentService.updatePayment(paymentDTO));
        assertThat(ex.getMessage()).contains("Payment not found");
    }
}
