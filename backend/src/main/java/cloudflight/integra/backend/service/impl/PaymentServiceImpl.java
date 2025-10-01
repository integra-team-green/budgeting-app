package cloudflight.integra.backend.service.impl;

import cloudflight.integra.backend.dto.PaymentDTO;
import cloudflight.integra.backend.entity.Payment;
import cloudflight.integra.backend.entity.validation.PaymentValidator;
import cloudflight.integra.backend.exception.NotFoundException;
import cloudflight.integra.backend.mapper.PaymentMapper;
import cloudflight.integra.backend.repository.PaymentRepository;
import cloudflight.integra.backend.service.PaymentService;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// varianta buna
@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository dbPaymentRepository;
    private final PaymentValidator paymentValidator;

    public PaymentServiceImpl(PaymentRepository dbPaymentRepository, PaymentValidator paymentValidator) {
        this.dbPaymentRepository = dbPaymentRepository;
        this.paymentValidator = paymentValidator;
    }

    @Override
    @Transactional
    public PaymentDTO addPayment(PaymentDTO paymentDTO) {
        paymentValidator.validate(paymentDTO);

        Payment payment = PaymentMapper.getFromDTO(paymentDTO);
        payment = dbPaymentRepository.save(payment);

        return PaymentMapper.getDTO(payment);
    }

    @Override
    @Transactional(readOnly = true)
    public PaymentDTO getPaymentById(Long id) {
        Payment payment = dbPaymentRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("Payment not found with id: " + id));
        return PaymentMapper.getDTO(payment);
    }

    @Override
    @Transactional
    public PaymentDTO updatePayment(PaymentDTO paymentDTO) {
        paymentValidator.validate(paymentDTO);

        if (!dbPaymentRepository.existsById(paymentDTO.getId())) {
            throw new NotFoundException("Payment not found with id: " + paymentDTO.getId());
        }

        Payment payment = PaymentMapper.getFromDTO(paymentDTO);
        payment = dbPaymentRepository.save(payment);

        return PaymentMapper.getDTO(payment);
    }

    @Override
    @Transactional
    public PaymentDTO deletePayment(Long id) {
        Payment payment = dbPaymentRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("Payment not found with id: " + id));

        dbPaymentRepository.delete(payment);
        return PaymentMapper.getDTO(payment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaymentDTO> getAllPayments() {
        List<Payment> payments = dbPaymentRepository.findAll();
        return PaymentMapper.getPaymentDTOsFromPayments(payments);
    }
}
