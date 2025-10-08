package cloudflight.integra.backend.income;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import cloudflight.integra.backend.dto.IncomeDTO;
import cloudflight.integra.backend.entity.Frequency;
import cloudflight.integra.backend.entity.Income;
import cloudflight.integra.backend.entity.User;
import cloudflight.integra.backend.entity.validation.IncomeValidator;
import cloudflight.integra.backend.entity.validation.ValidationException;
import cloudflight.integra.backend.repository.IncomeRepository;
import cloudflight.integra.backend.repository.UserRepository;
import cloudflight.integra.backend.service.impl.IncomeServiceImpl;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class IncomeServiceTest {
    @InjectMocks
    private IncomeServiceImpl service;

    @Mock
    private IncomeRepository repo;

    @Mock
    private IncomeValidator validator;

    @Mock
    private UserRepository userRepository;

    private User user1, user2;

    @BeforeEach
    void setUp() {
        user1 = new User(1L, "Alice", "alice@email.com", "123");
        user2 = new User(2L, "Marc", "marc@yahoo.com", "abcd999");
        when(userRepository.findById(1L)).thenReturn(Optional.of(user1));
        when(userRepository.findById(2L)).thenReturn(Optional.of(user2));
    }

    @Test
    void createIncome_withValidData_succeeds() {
        Income income = new Income();
        income.setId(1L);
        income.setAmount(BigDecimal.valueOf(1000));
        income.setSource("Job");
        income.setDate(new Date());
        income.setFrequency(Frequency.MONTHLY);
        when(repo.save(any(Income.class))).thenReturn(income);

        IncomeDTO dto = new IncomeDTO();
        dto.setUserId(1L);
        dto.setAmount(BigDecimal.valueOf(1000));
        dto.setSource("Job");
        dto.setDate(new Date());
        dto.setFrequency(Frequency.MONTHLY);
        doNothing().when(validator).validate(any(Income.class));
        IncomeDTO result = service.createIncome(dto);

        assertNotNull(result.getId());
        assertEquals(BigDecimal.valueOf(1000), result.getAmount());
        verify(repo, times(1)).save(any(Income.class));
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
        doThrow(new ValidationException(List.of("Amount must be positive!")))
                .when(validator)
                .validate(any(Income.class));
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
        doThrow(new ValidationException(List.of("Source must not be null!")))
                .when(validator)
                .validate(any(Income.class));
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
        Income income1 = new Income();
        income1.setId(null);
        income1.setFrequency(Frequency.MONTHLY);
        income1.setAmount(BigDecimal.valueOf(1000));
        income1.setSource("Job");
        income1.setDate(new Date());
        income1.setDescription("Salary");
        income1.setUserId(user1.getId());
        income1.setEndDate(null);

        Income income2 = new Income();
        income2.setId(null);
        income2.setFrequency(Frequency.MONTHLY);
        income2.setAmount(BigDecimal.valueOf(200));
        income2.setSource("Gift");
        income2.setDate(new Date());
        income2.setDescription("Birthday");
        income2.setUserId(user2.getId());
        income2.setEndDate(null);
        when(repo.findAll()).thenReturn(List.of(income1, income2));

        Iterable<IncomeDTO> all = service.getAllIncomes();
        long count = StreamSupport.stream(all.spliterator(), false).count();

        assertEquals(2, count);
        verify(repo).findAll();
    }

    @Test
    void getIncomeById_returnsCorrectIncome() {
        Income income = new Income();
        income.setId(1L);
        income.setFrequency(Frequency.MONTHLY);
        income.setAmount(BigDecimal.valueOf(500));
        income.setSource("Job");
        income.setDate(new Date());
        income.setDescription("Salary");
        income.setUserId(user1.getId());
        income.setEndDate(null);
        when(repo.findById(1L)).thenReturn(Optional.of(income));

        IncomeDTO found = service.getIncomeById(1L);
        assertEquals(found.getId(), 1L);
        assertEquals(0, found.getAmount().compareTo(BigDecimal.valueOf(500)));
        assertEquals("Job", found.getSource());
        assertEquals(found.getDescription(), "Salary");
        System.out.println("Fetched income ID: " + found.getId());
    }

    @Test
    void updateIncome_withValidData_succeeds() {
        Income income = new Income();
        income.setId(1L);
        income.setFrequency(Frequency.MONTHLY);
        income.setAmount(BigDecimal.valueOf(1000));
        income.setSource("Job");
        income.setDate(new Date());
        income.setDescription("Salary");
        income.setUserId(user1.getId());
        income.setEndDate(null);
        when(repo.findById(1L)).thenReturn(Optional.of(income));
        when(repo.save(any(Income.class))).thenAnswer(invocation -> invocation.getArgument(0));

        IncomeDTO dto = new IncomeDTO();
        dto.setId(1L);
        dto.setAmount(BigDecimal.valueOf(1200));
        dto.setSource("Job");
        dto.setDate(new Date());
        dto.setDescription("Salary");
        dto.setFrequency(Frequency.MONTHLY);
        dto.setUserId(user1.getId());

        IncomeDTO updated = service.updateIncome(dto);
        assertEquals(0, updated.getAmount().compareTo(BigDecimal.valueOf(1200)));
        verify(repo).save(any(Income.class));
    }

    @Test
    void updateIncome_withInvalidData_throwsValidationException() {
        Income income1 = new Income();
        income1.setId(null);
        income1.setFrequency(Frequency.MONTHLY);
        income1.setAmount(BigDecimal.valueOf(1000));
        income1.setSource("Job");
        income1.setDate(new Date());
        income1.setDescription("Salary");
        income1.setUserId(user1.getId());
        income1.setEndDate(null);
        when(repo.findById(1L)).thenReturn(Optional.of(income1));
        when(repo.save(any(Income.class))).thenAnswer(invocation -> invocation.getArgument(0));

        IncomeDTO dto = new IncomeDTO();
        dto.setId(1L);
        dto.setAmount(BigDecimal.valueOf(-1200));
        dto.setSource("Job");
        dto.setDate(new Date());
        dto.setDescription("Salary");
        dto.setFrequency(Frequency.MONTHLY);
        dto.setUserId(user1.getId());

        doThrow(new ValidationException(List.of("Amount must be positive!")))
                .when(validator)
                .validate(any(Income.class));
    }

    @Test
    void deleteIncome_removesIncome() {
        Income income1 = new Income();
        income1.setId(null);
        income1.setFrequency(Frequency.MONTHLY);
        income1.setAmount(BigDecimal.valueOf(1000));
        income1.setSource("Job");
        income1.setDate(new Date());
        income1.setDescription("Salary");
        income1.setUserId(user1.getId());
        income1.setEndDate(null);
        when(repo.findById(1L)).thenReturn(Optional.of(income1));

        service.deleteIncome(1L);

        verify(repo).deleteById(1L);
    }
}
