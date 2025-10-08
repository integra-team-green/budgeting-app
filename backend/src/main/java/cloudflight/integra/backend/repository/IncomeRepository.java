package cloudflight.integra.backend.repository;

import cloudflight.integra.backend.entity.Income;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/** Repository interface provides CRUD operations for Income entity. */
@Repository
public interface IncomeRepository extends JpaRepository<Income, Long> {}
