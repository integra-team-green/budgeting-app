package cloudflight.integra.backend.repository.inMemoryImpl;

import cloudflight.integra.backend.entity.Payment;
import cloudflight.integra.backend.repository.PaymentRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryPaymentRepositoryImpl implements PaymentRepository {
  private final List<Payment> payments;
  private final AtomicLong currentId;

  public InMemoryPaymentRepositoryImpl() {
    this.payments = new ArrayList<>();
    this.currentId = new AtomicLong(0);
  }

  @Override
  public Payment save(Payment payment) {
    if (payment.getId() == null) {
      payment.setId(currentId.incrementAndGet());
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
  public Optional<Payment> findById(Long id) {
    return payments.stream().filter(p -> p.getId().equals(id)).findFirst();
  }

  @Override
  public Optional<Payment> update(Payment payment) {
    return findById(payment.getId())
        .map(
            oldPayment -> {
              payments.remove(oldPayment);
              payments.add(payment);
              return payment;
            });
  }
}
