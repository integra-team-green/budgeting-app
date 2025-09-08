package cloudflight.integra.backend;

import cloudflight.integra.backend.entity.Income;
import cloudflight.integra.backend.repository.impl.InMemoryIncomeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryIncomeRepositoryTest {
    private InMemoryIncomeRepository repo;

    @BeforeEach
    void setUp() {
        repo = new InMemoryIncomeRepository();
    }

    @Test
    void testCreateAndFindById() {
        Income income = new Income(1L, new BigDecimal("1000"), "Job", new Date(), "Salary");
        repo.create(income);

        Income found = repo.findById(1L);
        assertNotNull(found);
        assertEquals(income, found);
    }

    @Test
    void testGetAll() {
        Income i1 = new Income(1L, new BigDecimal("1000"), "Job", new Date(), "Salary");
        Income i2 = new Income(2L, new BigDecimal("500"), "Freelance", new Date(), "Bonus");
        repo.create(i1);
        repo.create(i2);

        Iterable<Income> all = repo.getAll();
        int count = 0;
        for (Income i : all) count++;

        assertEquals(2, count);
    }

    @Test
    void testUpdate() {
        Income income = new Income(1L, new BigDecimal("1000"), "Job", new Date(), "Salary");
        repo.create(income);

        income.setAmount(new BigDecimal("1200"));
        repo.update(income);

        Income updated = repo.findById(1L);
        assertEquals(new BigDecimal("1200"), updated.getAmount());
    }

    @Test
    void testDelete() {
        Income income = new Income(1L, new BigDecimal("1000"), "Job", new Date(), "Salary");
        repo.create(income);

        repo.delete(1L);
        assertNull(repo.findById(1L));
    }
}