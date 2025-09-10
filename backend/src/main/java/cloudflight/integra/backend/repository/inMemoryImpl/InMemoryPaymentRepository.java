package cloudflight.integra.backend.repository.inMemoryImpl;

import cloudflight.integra.backend.model.Payment;
import cloudflight.integra.backend.repository.IPaymentRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class InMemoryPaymentRepository implements IPaymentRepository {
    private final List<Payment> payments;
    private Long currentId;

    public InMemoryPaymentRepository() {
        this.payments = new ArrayList<>();
        this.currentId = 0L;
    }

    @Override
    public Payment save(Payment payment) {
        if (payment.getId() == null) {
            payment.setId(++this.currentId);
        }
        payments.add(payment);
        return payment;
    }

    @Override
    public List<Payment> findAll() {
        return payments;
    }

    @Override
    public Optional<Payment> delete(Long id) {
        Payment deletedPayment = findById(id).orElse(null);
        payments.remove(deletedPayment);
        return Optional.ofNullable(deletedPayment);
    }

    @Override
    public Optional<Payment>findById(Long id) {
        return payments.stream().filter(p -> p.getId().equals(id)).findFirst();
    }

    @Override
    public Optional<Payment> update(Payment payment) {
        return findById(payment.getId())
                .map(oldPayment-> {
                    payments.remove(oldPayment);
                    payments.add(payment);
                    return payment;});
    }
}
