package cloudflight.integra.backend.repository;

import cloudflight.integra.backend.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    @Modifying
    @Query("DELETE FROM Payment p WHERE p.id = :paymentId")
    int deletePaymentByIdUsingQuery(@Param("paymentId") Long paymentId);
}
