package cloudflight.integra.backend.repository.inMemoryImpl;

import cloudflight.integra.backend.entity.Income;
import cloudflight.integra.backend.repository.IncomeRepository;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class InMemoryIncomeRepositoryImpl implements IncomeRepository {

    private final Map<Long, Income> incomes;
    private final AtomicLong idGenerator;

    public InMemoryIncomeRepositoryImpl() {
        this.incomes = new ConcurrentHashMap<>();
        this.idGenerator = new AtomicLong(0);
    }

    @Override
    public void create(Income income) {
        income.setId(idGenerator.incrementAndGet());
        incomes.put(income.getId(), income);
    }

    @Override
    public Iterable<Income> getAll() {
        return incomes.values();
    }

    @Override
    public Income findById(Long id) {
        return incomes.get(id);
    }

    @Override
    public void update(Income income) {
        incomes.put(income.getId(), income);
    }

    @Override
    public void delete(Long id) {
        incomes.remove(id);
    }

    @Override
    public void clearAll() {
        incomes.clear();
        idGenerator.set(0L);
    }
}
