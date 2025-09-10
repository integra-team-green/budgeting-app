package cloudflight.integra.backend;

import cloudflight.integra.backend.entity.Income;
import cloudflight.integra.backend.repository.impl.InMemoryIncomeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Date;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryIncomeRepositoryTest {

    private InMemoryIncomeRepository repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryIncomeRepository();
    }

    @Test
    void create_generatesAutoIncrementId() {
        Income income1 = new Income(null, BigDecimal.valueOf(100), "Job", new Date(), "Salary");
        Income income2 = new Income(null, BigDecimal.valueOf(200), "Gift", new Date(), "Birthday");

        repository.create(income1);
        repository.create(income2);

        assertNotNull(income1.getId());
        assertNotNull(income2.getId());
        assertEquals(1L, income1.getId());
        assertEquals(2L, income2.getId());
    }

    @Test
    void findById_returnsCorrectIncome() {
        Income income = new Income(null, BigDecimal.valueOf(150), "Freelance", new Date(), "Project");
        repository.create(income);

        Income found = repository.findById(income.getId());

        assertNotNull(found);
        assertEquals(income.getAmount(), found.getAmount());
    }

    @Test
    void update_changesExistingIncome() {
        Income income = new Income(null, BigDecimal.valueOf(300), "Bonus", new Date(), "Year end");
        repository.create(income);

        income.setAmount(BigDecimal.valueOf(500));
        repository.update(income);

        Income updated = repository.findById(income.getId());
        assertEquals(BigDecimal.valueOf(500), updated.getAmount());
    }

    @Test
    void delete_removesIncome() {
        Income income = new Income(null, BigDecimal.valueOf(400), "Lottery", new Date(), "Win");
        repository.create(income);

        repository.delete(income.getId());

        assertNull(repository.findById(income.getId()));
    }

    @Test
    void getAll_returnsAllIncomes() {
        repository.create(new Income(null, BigDecimal.valueOf(100), "Job", new Date(), "Salary"));
        repository.create(new Income(null, BigDecimal.valueOf(200), "Gift", new Date(), "Birthday"));

        Iterable<Income> allIncomes = repository.getAll();
        long count = StreamSupport.stream(allIncomes.spliterator(), false).count();

        assertEquals(2, count);
    }

}