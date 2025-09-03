package cloudflight.integra.backend.repository;

import cloudflight.integra.backend.entity.Saving;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ISavingJpaRepository extends JpaRepository<Saving, Long> {
}
