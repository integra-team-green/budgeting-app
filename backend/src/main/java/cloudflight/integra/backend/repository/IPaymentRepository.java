package cloudflight.integra.backend.repository;

import cloudflight.integra.backend.model.Payment;

import java.util.List;
import java.util.Optional;

public interface IPaymentRepository {
    /**
     * Saves a new Payment
     * @param payment
     * @return the payment saved
     */
    Payment save(Payment payment);

    /**
     *
     * @return all payments
     */
    List<Payment> findAll();

    /**
     * Deletes a payment by id
     * @param id, type: Long
     * @return the deleted payment
     */
    Optional<Payment> delete(Long id);

    /**
     * Find a payment by id
     * @param id ,type: Long
     * @return the payment with the id requested
     */
    Optional<Payment> findById(Long id);

    /**
     * Update a payment
     * @param payment-new payment
     * @return the updated payment
     */
    Optional<Payment> update(Payment payment);
}
