package cloudflight.integra.backend.income;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import cloudflight.integra.backend.dto.IncomeDTO;
import cloudflight.integra.backend.entity.Frequency;
import cloudflight.integra.backend.entity.User;
import cloudflight.integra.backend.entity.validation.ValidationException;
import cloudflight.integra.backend.exception.NotFoundException;
import cloudflight.integra.backend.repository.IncomeRepository;
import cloudflight.integra.backend.repository.UserRepository;
import cloudflight.integra.backend.service.impl.IncomeServiceImpl;
import java.math.BigDecimal;
import java.util.Date;
import java.util.stream.StreamSupport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class IncomeServiceTest {
  @Autowired private IncomeServiceImpl service;
  @Autowired private IncomeRepository repo;
  @Autowired private UserRepository userRepository;

  private User user1, user2;

  @BeforeEach
  void setUp() {
    repo.deleteAll();
    userRepository.deleteAll();
    user1 = userRepository.save(new User(null, "Alice", "alice@email.com", "123"));
    user2 = userRepository.save(new User(null, "Marc", "marc@yahoo.com", "abcd999"));
  }

  @Test
  void createIncome_withValidData_succeeds() {
    IncomeDTO income = new IncomeDTO();
    income.setId(null);
    income.setFrequency(Frequency.MONTHLY);
    income.setAmount(BigDecimal.valueOf(1000));
    income.setSource("Job");
    income.setDate(new Date());
    income.setDescription("Salary");
    income.setUserId(user1.getId());
    income.setEndDate(null);

    IncomeDTO i = assertDoesNotThrow(() -> service.createIncome(income));
    assertNotNull(i.getId(), "ID should be generated automatically");
    System.out.println("Created income ID: " + i.getId());
  }

  @Test
  void createIncome_withNegativeAmount_throwsValidationException() {
    // Arrange
    IncomeDTO income = new IncomeDTO();
    income.setId(null);
    income.setFrequency(Frequency.MONTHLY);
    income.setAmount(BigDecimal.valueOf(-100));
    income.setSource("Job");
    income.setDate(new Date());
    income.setDescription("Salary");
    income.setUserId(user1.getId());
    income.setEndDate(null);

    // Act & Assert
    ValidationException ex =
        assertThrows(ValidationException.class, () -> service.createIncome(income));

    ex.getErrors().forEach(err -> System.out.println("Validation error: " + err));
  }

  @Test
  void createIncome_withNullSource_throwsValidationException() {
    // Arrange
    IncomeDTO income = new IncomeDTO();
    income.setId(null);
    income.setFrequency(Frequency.MONTHLY);
    income.setAmount(BigDecimal.valueOf(1000));
    income.setSource(null);
    income.setDate(new Date());
    income.setDescription("Salary");
    income.setUserId(user1.getId());
    income.setEndDate(null);

    // Act & Assert
    ValidationException ex =
        assertThrows(ValidationException.class, () -> service.createIncome(income));

    ex.getErrors().forEach(err -> System.out.println("Validation error: " + err));
  }

  @Test
  void createIncome_withNonExistingUserId_throwsException() {
    IncomeDTO income = new IncomeDTO();
    income.setId(null);
    income.setFrequency(Frequency.MONTHLY);
    income.setAmount(BigDecimal.valueOf(1000));
    income.setSource("Job");
    income.setDate(new Date());
    income.setDescription("Salary");
    income.setEndDate(null);

    income.setUserId(9999L);

    assertThrows(Exception.class, () -> service.createIncome(income));
  }

  @Test
  void getAllIncomes_returnsAllCreatedIncomes() {
    IncomeDTO income1 = new IncomeDTO();
    income1.setId(null);
    income1.setFrequency(Frequency.MONTHLY);
    income1.setAmount(BigDecimal.valueOf(1000));
    income1.setSource("Job");
    income1.setDate(new Date());
    income1.setDescription("Salary");
    income1.setUserId(user1.getId());
    income1.setEndDate(null);

    IncomeDTO income2 = new IncomeDTO();
    income2.setId(null);
    income2.setFrequency(Frequency.MONTHLY);
    income2.setAmount(BigDecimal.valueOf(200));
    income2.setSource("Gift");
    income2.setDate(new Date());
    income2.setDescription("Birthday");
    income2.setUserId(user2.getId());
    income2.setEndDate(null);

    service.createIncome(income1);
    service.createIncome(income2);

    Iterable<IncomeDTO> all = service.getAllIncomes();
    long count = StreamSupport.stream(all.spliterator(), false).count();

    assertEquals(2, count);
    System.out.println("Total incomes in repository: " + count);
  }

  @Test
  void getIncomeById_returnsCorrectIncome() {
    IncomeDTO income = new IncomeDTO();
    income.setId(null);
    income.setFrequency(Frequency.MONTHLY);
    income.setAmount(BigDecimal.valueOf(500));
    income.setSource("Job");
    income.setDate(new Date());
    income.setDescription("Salary");
    income.setUserId(user1.getId());
    income.setEndDate(null);
    IncomeDTO saved = service.createIncome(income);

    IncomeDTO found = service.getIncomeById(saved.getId());
    assertEquals(found.getId(), saved.getId());
    assertEquals(0, found.getAmount().compareTo(BigDecimal.valueOf(500)));
    assertEquals(saved.getSource(), found.getSource());
    assertEquals(found.getDescription(), saved.getDescription());
    System.out.println("Fetched income ID: " + found.getId());
  }

  @Test
  void updateIncome_withValidData_succeeds() {
    IncomeDTO income = new IncomeDTO();
    income.setId(null);
    income.setFrequency(Frequency.MONTHLY);
    income.setAmount(BigDecimal.valueOf(1000));
    income.setSource("Job");
    income.setDate(new Date());
    income.setDescription("Salary");
    income.setUserId(user1.getId());
    income.setEndDate(null);
    IncomeDTO saved = service.createIncome(income);

    saved.setAmount(BigDecimal.valueOf(1200));
    assertDoesNotThrow(() -> service.updateIncome(saved));

    IncomeDTO updated = service.getIncomeById(saved.getId());
    assertEquals(0, updated.getAmount().compareTo(BigDecimal.valueOf(1200)));
    System.out.println("Updated income amount: " + updated.getAmount());
  }

  @Test
  void updateIncome_withInvalidData_throwsValidationException() {
    IncomeDTO income1 = new IncomeDTO();
    income1.setId(null);
    income1.setFrequency(Frequency.MONTHLY);
    income1.setAmount(BigDecimal.valueOf(1000));
    income1.setSource("Job");
    income1.setDate(new Date());
    income1.setDescription("Salary");
    income1.setUserId(user1.getId());
    income1.setEndDate(null);
    service.createIncome(income1);

    income1.setAmount(BigDecimal.valueOf(-500));

    ValidationException ex =
        assertThrows(ValidationException.class, () -> service.updateIncome(income1));

    ex.getErrors().forEach(err -> System.out.println("Validation error on update: " + err));
  }

  @Test
  void deleteIncome_removesIncome() {
    IncomeDTO income1 = new IncomeDTO();
    income1.setId(null);
    income1.setFrequency(Frequency.MONTHLY);
    income1.setAmount(BigDecimal.valueOf(1000));
    income1.setSource("Job");
    income1.setDate(new Date());
    income1.setDescription("Salary");
    income1.setUserId(user1.getId());
    income1.setEndDate(null);
    IncomeDTO saved = service.createIncome(income1);

    service.deleteIncome(saved.getId());

    assertThrows(NotFoundException.class, () -> service.getIncomeById(saved.getId()));
    System.out.println("Deleted income ID: " + income1.getId());
  }
}
