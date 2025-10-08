package cloudflight.integra.backend.service;

import cloudflight.integra.backend.dto.PaymentDTO;
import java.util.List;

// varianta buna
public interface PaymentService {
    /**
     * Saves a new Payment
     *
     * @param payment
     * @return the payment saved
     */
    PaymentDTO addPayment(PaymentDTO payment);

    /**
     * Find a payment by id
     *
     * @param id ,type: Long
     * @return the payment with the id requested
     */
    PaymentDTO getPaymentById(Long id);

    /**
     * Update a payment
     *
     * @param payment-new payment
     * @return the updated payment
     */
    PaymentDTO updatePayment(PaymentDTO payment);

    /**
     * Deletes a payment by id
     *
     * @param id, type: Long
     * @return the deleted payment
     */
    PaymentDTO deletePayment(Long id);

    /**
     * @return all payments
     */
    List<PaymentDTO> getAllPayments();
}
