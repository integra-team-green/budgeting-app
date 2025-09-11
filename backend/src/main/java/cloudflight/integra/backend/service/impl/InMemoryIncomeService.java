package cloudflight.integra.backend.service.impl;

import cloudflight.integra.backend.entity.Income;
import cloudflight.integra.backend.exception.NotFoundException;
import cloudflight.integra.backend.repository.IncomeRepository;
import cloudflight.integra.backend.service.IncomeService;
import cloudflight.integra.backend.validation.IncomeValidator;
import org.springframework.stereotype.Service;

@Service
public class InMemoryIncomeService implements IncomeService {

    private final IncomeRepository incomeRepo;
    private final IncomeValidator incomeValidator;

    public InMemoryIncomeService(IncomeRepository incomeRepo, IncomeValidator incomeValidator) {
        this.incomeRepo = incomeRepo;
        this.incomeValidator = incomeValidator;
    }

    @Override
    public void createIncome(Income income) {
        incomeValidator.validate(income);
        incomeRepo.create(income);
    }

    @Override
    public Iterable<Income> getAllIncomes() {
        return incomeRepo.getAll();
    }

    @Override
    public Income getIncomeById(Long id) {
        Income income = incomeRepo.findById(id);
        if (income == null) {
            throw new NotFoundException("Income with id " + id + " not found");
        }
        return income;
    }

    @Override
    public void updateIncome(Income income) {
        incomeValidator.validate(income);
        if (incomeRepo.findById(income.getId()) == null) {
            throw new NotFoundException("Income with id " + income.getId() + " not found for update");
        }
        incomeRepo.update(income);
    }

    @Override
    public void deleteIncome(Long id) {
        if (incomeRepo.findById(id) == null) {
            throw new NotFoundException("Income with id " + id + " not found for delete");
        }
        incomeRepo.delete(id);
    }
}
