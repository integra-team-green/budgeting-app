package cloudflight.integra.backend.service;

import cloudflight.integra.backend.model.Payment;

import java.util.List;

public interface IService {
    /**
     * Saves a new Payment
     * @param payment
     * @return the payment saved
     */
    Payment addPayment(Payment payment);
    /**
     * Find a payment by id
     * @param id ,type: Long
     * @return the payment with the id requested
     */
    Payment getPayment(Long id);
    /**
     * Update a payment
     * @param payment-new payment
     * @return the updated payment
     */
    Payment updatePayment(Payment payment);
    /**
     * Deletes a payment by id
     * @param id, type: Long
     * @return the deleted payment
     */
    Payment deletePayment(Long id);
    /**
     *
     * @return all payments
     */
    List<Payment> getPayments();
}
