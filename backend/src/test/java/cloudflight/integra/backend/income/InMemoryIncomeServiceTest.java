package cloudflight.integra.backend.income;

import cloudflight.integra.backend.entity.Income;
import cloudflight.integra.backend.entity.validation.IncomeValidator;
import cloudflight.integra.backend.entity.validation.ValidationException;
import cloudflight.integra.backend.exception.NotFoundException;
import cloudflight.integra.backend.repository.inMemoryImpl.InMemoryIncomeRepositoryImpl;
import cloudflight.integra.backend.service.impl.IncomeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Date;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class InMemoryIncomeServiceTest {

    private IncomeServiceImpl service;

    @BeforeEach
    void setUp() {
        InMemoryIncomeRepositoryImpl repo = new InMemoryIncomeRepositoryImpl();
        IncomeValidator validator = new IncomeValidator();
        service = new IncomeServiceImpl(repo, validator);
    }

    @Test
    void createIncome_withValidData_succeeds() {
        Income income = new Income(null, BigDecimal.valueOf(1000), "Job", new Date(), "Salary");

        assertDoesNotThrow(() -> service.createIncome(income));
        assertNotNull(income.getId(), "ID should be generated automatically");
        System.out.println("Created income ID: " + income.getId());
    }

    @Test
    void createIncome_withNegativeAmount_throwsValidationException() {
        Income income = new Income(null, BigDecimal.valueOf(-100), "Job", new Date(), "Salary");

        ValidationException ex = assertThrows(ValidationException.class, () -> service.createIncome(income));
        ex.getErrors().forEach(err ->
                System.out.println("Validation error: "  + " - " + err)
        );
    }

    @Test
    void createIncome_withNullSource_throwsValidationException() {
        Income income = new Income(null, BigDecimal.valueOf(100), null, new Date(), "Salary");

        ValidationException ex = assertThrows(ValidationException.class, () -> service.createIncome(income));
        ex.getErrors().forEach(err ->
                System.out.println("Validation error: " + err)
        );
    }

    @Test
    void getAllIncomes_returnsAllCreatedIncomes() {
        Income income1 = new Income(null, BigDecimal.valueOf(100), "Job", new Date(), "Salary");
        Income income2 = new Income(null, BigDecimal.valueOf(200), "Gift", new Date(), "Birthday");

        service.createIncome(income1);
        service.createIncome(income2);

        Iterable<Income> all = service.getAllIncomes();
        long count = StreamSupport.stream(all.spliterator(), false).count();

        assertEquals(2, count);
        System.out.println("Total incomes in repository: " + count);
    }

    @Test
    void getIncomeById_returnsCorrectIncome() {
        Income income = new Income(null, BigDecimal.valueOf(500), "Freelance", new Date(), "Project");
        service.createIncome(income);

        Income found = service.getIncomeById(income.getId());
        assertEquals(income, found);
        System.out.println("Fetched income ID: " + found.getId());
    }

    @Test
    void updateIncome_withValidData_succeeds() {
        Income income = new Income(null, BigDecimal.valueOf(1000), "Job", new Date(), "Salary");
        service.createIncome(income);

        income.setAmount(BigDecimal.valueOf(1200));
        assertDoesNotThrow(() -> service.updateIncome(income));

        Income updated = service.getIncomeById(income.getId());
        assertEquals(BigDecimal.valueOf(1200), updated.getAmount());
        System.out.println("Updated income amount: " + updated.getAmount());
    }

    @Test
    void updateIncome_withInvalidData_throwsValidationException() {
        Income income = new Income(null, BigDecimal.valueOf(1000), "Job", new Date(), "Salary");
        service.createIncome(income);

        income.setAmount(BigDecimal.valueOf(-500));
        ValidationException ex = assertThrows(ValidationException.class, () -> service.updateIncome(income));
        ex.getErrors().forEach(err ->
                System.out.println("Validation error on update: " + err)
        );
    }

    @Test
    void deleteIncome_removesIncome() {
        Income income = new Income(null, BigDecimal.valueOf(500), "Freelance", new Date(), "Project");
        service.createIncome(income);

        service.deleteIncome(income.getId());
        assertThrows(NotFoundException.class, () -> service.getIncomeById(income.getId()));
        System.out.println("Deleted income ID: " + income.getId());
    }
}
