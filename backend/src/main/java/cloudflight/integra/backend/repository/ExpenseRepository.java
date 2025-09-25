package cloudflight.integra.backend.repository;

import cloudflight.integra.backend.entity.Expense;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {

  /**
   * Finds all expenses belonging to a specific user.
   *
   * @param userId the ID of the user whose expenses should be retrieved
   * @return a list of Expense objects associated with the given user ID
   */
  List<Expense> findAllByUserId(Long userId);
}
