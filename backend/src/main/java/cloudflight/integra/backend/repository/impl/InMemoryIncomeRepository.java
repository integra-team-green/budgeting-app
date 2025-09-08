package cloudflight.integra.backend.repository.impl;

import cloudflight.integra.backend.entity.Income;
import cloudflight.integra.backend.repository.IncomeRepository;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class InMemoryIncomeRepository implements IncomeRepository {
    private final Map<Long, Income> incomes;

    public InMemoryIncomeRepository() {

        this.incomes = new HashMap<>();
    }

    @Override
    public void create(Income income) {
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
}
