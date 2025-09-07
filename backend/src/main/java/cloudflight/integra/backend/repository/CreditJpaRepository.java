package cloudflight.integra.backend.repository;


/**
 * Spring Data JPA repository - use this in production profile.
 * <p>
 * To use this, create a Spring bean adapter that implements CreditRepository or
 * autowire this interface directly in service (but for parity with in-memory
 * implementation we adapted a wrapper).
 */

public interface CreditJpaRepository
    //extends JpaRepository<Credit, Long>
{
    // Additional query methods can be added here.
}