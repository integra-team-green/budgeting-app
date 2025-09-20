package cloudflight.integra.backend.service.impl;
import cloudflight.integra.backend.entity.Payment;
import cloudflight.integra.backend.entity.validation.PaymentValidator;
import cloudflight.integra.backend.entity.validation.ValidationException;
import cloudflight.integra.backend.exception.NotFoundException;
import cloudflight.integra.backend.repository.DBPaymentRepository;
import cloudflight.integra.backend.repository.PaymentRepository;
import cloudflight.integra.backend.service.PaymentService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentServiceImpl implements PaymentService {
    private final DBPaymentRepository dbPaymentRepository;
    private final PaymentValidator paymentValidator;

    public PaymentServiceImpl(DBPaymentRepository dbPaymentRepository, PaymentValidator paymentValidator) {
        this.dbPaymentRepository = dbPaymentRepository;
        this.paymentValidator = paymentValidator;
    }

    @Override
    public Payment addPayment(Payment payment) {
        try {
            paymentValidator.validate(payment);
        } catch (ValidationException e) {
            throw new ValidationException(List.of(e.getMessage()));
        }
        return dbPaymentRepository.save(payment);
    }

    @Override
    public Payment getPayment(Long id) {
        return dbPaymentRepository.findById(id).orElseThrow(() -> new NotFoundException("Payment not found with id: " + id));
    }

    @Override
    public Payment updatePayment(Payment payment) {
        paymentValidator.validate(payment);
        if (!dbPaymentRepository.existsById(payment.getId())) {
            throw new NotFoundException("Payment not found with id: " + payment.getId());
        }
        return dbPaymentRepository.save(payment);
    }

    @Override
    public Payment deletePayment(Long id) {
        Payment payment = dbPaymentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Payment not found with id: " + id));
        dbPaymentRepository.delete(payment);
        return payment;
    }

    @Override
    public List<Payment> getPayments() {
        return dbPaymentRepository.findAll();
    }
}
