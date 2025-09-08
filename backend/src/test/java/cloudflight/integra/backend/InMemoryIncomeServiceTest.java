package cloudflight.integra.backend;

import cloudflight.integra.backend.entity.Income;
import cloudflight.integra.backend.repository.IncomeRepository;
import cloudflight.integra.backend.repository.impl.InMemoryIncomeRepository;
import cloudflight.integra.backend.service.impl.InMemoryIncomeService;
import cloudflight.integra.backend.validation.IncomeValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryIncomeServiceTest {

    private InMemoryIncomeService service;

    @BeforeEach
    void setUp() {
        InMemoryIncomeRepository repo = new InMemoryIncomeRepository();
        IncomeValidator validator = new IncomeValidator();
        service = new InMemoryIncomeService(repo, validator);
    }

    @Test
    void testCreateAndGetById() {
        Income income = new Income(1L, new BigDecimal("1000"), "Job", new Date(), "Salary");
        service.createIncome(income);

        Income found = service.getIncomeById(1L);
        assertNotNull(found);
        assertEquals("Job", found.getSource());
    }

    @Test
    void testGetAllIncomes() {
        service.createIncome(new Income(1L, new BigDecimal("1000"), "Job", new Date(), "Salary"));
        service.createIncome(new Income(2L, new BigDecimal("200"), "Gift", new Date(), "Birthday"));

        int count = 0;
        for (Income i : service.getAllIncomes()) count++;
        assertEquals(2, count);
    }

    @Test
    void testUpdateIncome() {
        Income income = new Income(1L, new BigDecimal("1000"), "Job", new Date(), "Salary");
        service.createIncome(income);

        income.setAmount(new BigDecimal("1500"));
        service.updateIncome(income);

        Income updated = service.getIncomeById(1L);
        assertEquals(new BigDecimal("1500"), updated.getAmount());
    }

    @Test
    void testDeleteIncome() {
        Income income = new Income(1L, new BigDecimal("1000"), "Job", new Date(), "Salary");
        service.createIncome(income);

        service.deleteIncome(1L);
        assertNull(service.getIncomeById(1L));
    }
}