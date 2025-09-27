package cloudflight.integra.backend.income;

import static org.junit.jupiter.api.Assertions.*;

import cloudflight.integra.backend.entity.Frequency;
import cloudflight.integra.backend.entity.Income;
import cloudflight.integra.backend.entity.User;
import cloudflight.integra.backend.repository.IncomeRepository;
import cloudflight.integra.backend.repository.UserRepository;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;
import java.util.stream.StreamSupport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class IncomeRepositoryTest {

  @Autowired private IncomeRepository repository;

  @Autowired private UserRepository userRepository;

  private User user1, user2;

  @BeforeEach
  void setUp() {
    repository.deleteAll();
    userRepository.deleteAll();
    user1 = userRepository.save(new User(null, "Alice", "alice@email.com", "123"));
    user2 = userRepository.save(new User(null, "Marc", "marc@yahoo.com", "abcd999"));
  }

  @Test
  void create_generatesAutoIncrementId() {
    Income income1 =
        new Income(
            null,
            user1.getId(),
            BigDecimal.valueOf(100),
            "Job",
            new Date(),
            "Salary",
            Frequency.MONTHLY,
            null);
    Income income2 =
        new Income(
            null,
            user2.getId(),
            BigDecimal.valueOf(200),
            "Gift",
            new Date(),
            "Birthday",
            Frequency.YEARLY,
            null);

    repository.save(income1);
    repository.save(income2);

    assertNotNull(income1.getId());
    assertNotNull(income2.getId());
    assertEquals(0, income1.getAmount().compareTo(BigDecimal.valueOf(100)));
    assertEquals(0, income2.getAmount().compareTo(BigDecimal.valueOf(200)));
    assertEquals("Job", income1.getSource());
    assertEquals("Salary", income1.getDescription());
    assertEquals(Frequency.MONTHLY, income1.getFrequency());
    assertEquals("Birthday", income2.getDescription());
    assertEquals(Frequency.YEARLY, income2.getFrequency());
  }

  @Test
  void findById_returnsCorrectIncome() {
    Income income =
        new Income(
            null,
            user1.getId(),
            BigDecimal.valueOf(150),
            "Freelance",
            new Date(),
            "Project",
            Frequency.MONTHLY,
            null);
    repository.save(income);

    Optional<Income> found = repository.findById(income.getId());

    assertNotNull(found);
    assertEquals(0, found.get().getAmount().compareTo(BigDecimal.valueOf(150)));
  }

  @Test
  void update_changesExistingIncome() {
    Income income =
        new Income(
            null,
            user1.getId(),
            BigDecimal.valueOf(300),
            "Bonus",
            new Date(),
            "Year end",
            Frequency.MONTHLY,
            null);
    repository.save(income);

    income.setAmount(BigDecimal.valueOf(500));
    repository.save(income);

    Optional<Income> updated = repository.findById(income.getId());
    assertEquals(0, updated.get().getAmount().compareTo(BigDecimal.valueOf(500)));
  }

  @Test
  void delete_removesIncome() {
    Income income =
        new Income(
            null,
            user1.getId(),
            BigDecimal.valueOf(400),
            "Lottery",
            new Date(),
            "Win",
            Frequency.MONTHLY,
            null);
    repository.save(income);

    repository.deleteById(income.getId());

    assertTrue(repository.findById(income.getId()).isEmpty());
  }

  @Test
  void getAll_returnsAllIncomes() {
    repository.save(
        new Income(
            null,
            user1.getId(),
            BigDecimal.valueOf(100),
            "Job",
            new Date(),
            "Salary",
            Frequency.MONTHLY,
            null));
    repository.save(
        new Income(
            null,
            user2.getId(),
            BigDecimal.valueOf(200),
            "Gift",
            new Date(),
            "Birthday",
            Frequency.YEARLY,
            null));

    Iterable<Income> allIncomes = repository.findAll();
    long count = StreamSupport.stream(allIncomes.spliterator(), false).count();

    assertEquals(2, count);
  }
}
