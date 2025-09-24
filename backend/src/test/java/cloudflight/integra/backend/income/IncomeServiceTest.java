package cloudflight.integra.backend.income;

import cloudflight.integra.backend.dto.IncomeDTO;
import cloudflight.integra.backend.entity.Frequency;
import cloudflight.integra.backend.entity.Income;
import cloudflight.integra.backend.entity.User;
import cloudflight.integra.backend.exception.NotFoundException;
import cloudflight.integra.backend.repository.IncomeRepository;
import cloudflight.integra.backend.repository.UserRepository;
import cloudflight.integra.backend.service.impl.IncomeServiceImpl;
import cloudflight.integra.backend.entity.validation.IncomeValidator;
import cloudflight.integra.backend.entity.validation.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Date;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest
class IncomeServiceTest {
    @Autowired
    private IncomeServiceImpl service;
    @Autowired
    private IncomeRepository repo;
    @Autowired
    private UserRepository userRepository;

    private User user1,user2;

    @BeforeEach
    void setUp() {
        repo.deleteAll();
        userRepository.deleteAll();
        user1 =  userRepository.save(new User(null, "Alice", "alice@email.com", "123"));
        user2 = userRepository.save(new User(null, "Marc", "marc@yahoo.com", "abcd999"));
    }

    @Test
    void createIncome_withValidData_succeeds() {
        Income income = new Income(null,user1.getId(),BigDecimal.valueOf(1000), "Job", new Date(), "Salary", Frequency.ONE_TIME,null);

        assertDoesNotThrow(() -> service.createIncome(income));
        assertNotNull(income.getId(), "ID should be generated automatically");
        System.out.println("Created income ID: " + income.getId());
    }

    @Test
    void createIncome_withNegativeAmount_throwsValidationException() {
        // Arrange
        Income income = new Income(null, user1.getId() ,BigDecimal.valueOf(-100), "Job", new Date(), "Salary",Frequency.MONTHLY,null);

        // Act & Assert
        ValidationException ex = assertThrows(
                ValidationException.class,
                () -> service.createIncome(income)
        );

        ex.getErrors().forEach(err ->
                System.out.println("Validation error: " + err)
        );
    }

    @Test
    void createIncome_withNullSource_throwsValidationException() {
        // Arrange
        Income income = new Income(null, user1.getId(),BigDecimal.valueOf(100), null, new Date(), "Salary",Frequency.MONTHLY,null);

        // Act & Assert
        ValidationException ex = assertThrows(
                ValidationException.class,
                () -> service.createIncome(income)
        );

        ex.getErrors().forEach(err ->
                System.out.println("Validation error: " + err)
        );
    }

    @Test
    void createIncome_withNonExistingUserId_throwsException() {
        Income income = new Income();
        income.setAmount(BigDecimal.valueOf(500));
        income.setSource("Test Job");
        income.setDate(new Date());
        income.setDescription("Test Desc");
        income.setFrequency(Frequency.ONE_TIME);

        income.setUserId(9999L);

        assertThrows(Exception.class, () -> service.createIncome(income));
    }


    @Test
    void getAllIncomes_returnsAllCreatedIncomes() {
        Income income1 = new Income(null, user1.getId(),BigDecimal.valueOf(100), "Job", new Date(), "Salary",Frequency.MONTHLY,null);
        Income income2 = new Income(null, user2.getId(),BigDecimal.valueOf(200), "Gift", new Date(), "Birthday",Frequency.MONTHLY,null);

        service.createIncome(income1);
        service.createIncome(income2);

        Iterable<Income> all = service.getAllIncomes();
        long count = StreamSupport.stream(all.spliterator(), false).count();

        assertEquals(2, count);
        System.out.println("Total incomes in repository: " + count);
    }

    @Test
    void getIncomeById_returnsCorrectIncome() {
        Income income = new Income(null, user1.getId(),BigDecimal.valueOf(500), "Freelance", new Date(), "Project",Frequency.MONTHLY,null);
        service.createIncome(income);

        Income found = service.getIncomeById(income.getId());
        assertEquals(found.getId(), income.getId());
        assertEquals(0,found.getAmount().compareTo(BigDecimal.valueOf(500)));
        assertEquals(income.getSource(), found.getSource());
        assertEquals(found.getDescription(), income.getDescription());
        System.out.println("Fetched income ID: " + found.getId());
    }

    @Test
    void updateIncome_withValidData_succeeds() {
        Income income = new Income(null, user1.getId(),BigDecimal.valueOf(1000), "Job", new Date(), "Salary",Frequency.MONTHLY,null);
        service.createIncome(income);

        income.setAmount(BigDecimal.valueOf(1200));
        assertDoesNotThrow(() -> service.updateIncome(income));

        Income updated = service.getIncomeById(income.getId());
        assertEquals(0, updated.getAmount().compareTo(BigDecimal.valueOf(1200)));
        System.out.println("Updated income amount: " + updated.getAmount());
    }

    @Test
    void updateIncome_withInvalidData_throwsValidationException() {
        Income income = new Income(null, user1.getId(),BigDecimal.valueOf(1000), "Job", new Date(), "Salary",Frequency.MONTHLY,null);
        service.createIncome(income);

        income.setAmount(BigDecimal.valueOf(-500));

        ValidationException ex =
                assertThrows(ValidationException.class, () -> service.updateIncome(income));

        ex.getErrors().forEach(err ->
                System.out.println("Validation error on update: " + err)
        );
    }


    @Test
    void deleteIncome_removesIncome() {
        Income income = new Income(null, user1.getId(),BigDecimal.valueOf(500), "Freelance", new Date(), "Project",Frequency.MONTHLY,null);
        service.createIncome(income);

        service.deleteIncome(income.getId());

        assertThrows(NotFoundException.class, () -> service.getIncomeById(income.getId()));
        System.out.println("Deleted income ID: " + income.getId());
    }


}
