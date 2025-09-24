package cloudflight.integra.backend.service.impl;

import cloudflight.integra.backend.entity.Income;
import cloudflight.integra.backend.entity.User;
import cloudflight.integra.backend.exception.NotFoundException;
import cloudflight.integra.backend.repository.IncomeRepository;
import cloudflight.integra.backend.repository.UserRepository;
import cloudflight.integra.backend.service.IncomeService;
import cloudflight.integra.backend.entity.validation.IncomeValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class IncomeServiceImpl implements IncomeService {

    private final IncomeRepository incomeRepo;
    private final IncomeValidator incomeValidator;

    public IncomeServiceImpl(IncomeRepository incomeRepo, IncomeValidator incomeValidator) {
        this.incomeRepo = incomeRepo;
        this.incomeValidator = incomeValidator;


    }

    @Override
    @Transactional
    public Income createIncome(Income income) {
        incomeValidator.validate(income);

        User user = new User();
        user.setId(income.getUserId());
        income.setUser(user);

        return incomeRepo.save(income);
    }

    @Override
    @Transactional(readOnly = true)
    public Iterable<Income> getAllIncomes() {
        return incomeRepo.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Income getIncomeById(Long id) {
        if (id == null)
            throw new IllegalArgumentException("Income id must not be null.");
        return incomeRepo.findById(id).orElseThrow(()-> new NotFoundException("Income not found"));
    }

    @Override
    @Transactional
    public void updateIncome(Income income) {
        incomeValidator.validate(income);
        if (incomeRepo.findById(income.getId()).isEmpty()) {
            throw new NotFoundException("Income with id " + income.getId() + " not found for update");
        }
        incomeRepo.save(income);
    }

    @Override
    @Transactional
    public void deleteIncome(Long id) {
        if (incomeRepo.findById(id).isEmpty()) {
            throw new NotFoundException("Income with id " + id + " not found for delete");
        }
        incomeRepo.deleteById(id);
    }
}
