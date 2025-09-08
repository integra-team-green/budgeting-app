package cloudflight.integra.backend.service.impl;

import cloudflight.integra.backend.model.Payment;
import cloudflight.integra.backend.model.validator.PaymentNotFoundException;
import cloudflight.integra.backend.model.validator.PaymentValidator;
import cloudflight.integra.backend.model.validator.ValidationException;
import cloudflight.integra.backend.repository.IPaymentRepository;
import cloudflight.integra.backend.service.IService;

import java.util.List;

@org.springframework.stereotype.Service
public class ServiceImpl implements IService {
    private final IPaymentRepository IPaymentRepository;
    private final PaymentValidator paymentValidator;

    public ServiceImpl(IPaymentRepository IPaymentRepository, PaymentValidator paymentValidator) {
        this.IPaymentRepository = IPaymentRepository;
        this.paymentValidator = paymentValidator;
    }

    @Override
    public Payment addPayment(Payment payment) {
        try {
            paymentValidator.validate(payment);
        } catch (ValidationException e) {
            throw new RuntimeException(e.getMessage());
        }
        return IPaymentRepository.save(payment);
    }

    @Override
    public Payment getPayment(Long id) {
        return IPaymentRepository.findById(id).orElseThrow(() -> new PaymentNotFoundException("Payment not found with id: " + id));
    }

    @Override
    public Payment updatePayment(Payment payment) {
        try {
            paymentValidator.validate(payment);
        } catch (ValidationException e) {
            throw new RuntimeException(e.getMessage());
        }
        return IPaymentRepository.update(payment).orElseThrow(() -> new PaymentNotFoundException("Payment not found with id: " + payment.getId()));

    }

    @Override
    public Payment deletePayment(Long id) {
       return IPaymentRepository.delete(id).orElseThrow(() -> new PaymentNotFoundException("Payment not found with id: " + id));
    }

    @Override
    public List<Payment> getPayments() {
        return IPaymentRepository.findAll();
    }
}
