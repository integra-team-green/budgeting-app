package cloudflight.integra.backend.service.impl;
import cloudflight.integra.backend.entity.Payment;
import cloudflight.integra.backend.entity.validation.PaymentValidator;
import cloudflight.integra.backend.entity.validation.ValidationException;
import cloudflight.integra.backend.exception.NotFoundException;
import cloudflight.integra.backend.repository.PaymentRepository;
import cloudflight.integra.backend.service.PaymentService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository dbPaymentRepository;
    private final PaymentValidator paymentValidator;

    public PaymentServiceImpl(PaymentRepository dbPaymentRepository, PaymentValidator paymentValidator) {
        this.dbPaymentRepository = dbPaymentRepository;
        this.paymentValidator = paymentValidator;
    }

    @Transactional
    public Payment addPayment(Payment payment) {
        try {
            paymentValidator.validate(payment);
        } catch (ValidationException e) {
            throw new ValidationException(List.of(e.getMessage()));
        }
        return dbPaymentRepository.save(payment);
    }

    @Transactional(readOnly = true)
    public Payment getPayment(Long id) {
        return dbPaymentRepository.findById(id).orElseThrow(() -> new NotFoundException("Payment not found with id: " + id));
    }

    @Transactional
    public Payment updatePayment(Payment payment) {
        paymentValidator.validate(payment);
        if (!dbPaymentRepository.existsById(payment.getId())) {
            throw new NotFoundException("Payment not found with id: " + payment.getId());
        }
        return dbPaymentRepository.save(payment);
    }

    @Transactional
    public Payment deletePayment(Long id) {
        Payment payment = dbPaymentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Payment not found with id: " + id));
        dbPaymentRepository.delete(payment);
        return payment;
    }

    @Transactional(readOnly = true)
    public List<Payment> getPayments() {
        return dbPaymentRepository.findAll();
    }
}
