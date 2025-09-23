package cloudflight.integra.backend.service.impl;

import cloudflight.integra.backend.entity.Expense;
import cloudflight.integra.backend.entity.validation.ExpenseValidator;
import cloudflight.integra.backend.exception.NotFoundException;
import cloudflight.integra.backend.repository.ExpenseRepository;
import cloudflight.integra.backend.service.ExpenseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ExpenseServiceImpl implements ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final ExpenseValidator expenseValidator;

    public ExpenseServiceImpl(ExpenseRepository expenseRepository, ExpenseValidator expenseValidator) {
        this.expenseRepository = expenseRepository;
        this.expenseValidator = expenseValidator;
    }

    @Override
    @Transactional
    public Expense createExpense(Expense expense) {
        expenseValidator.validate(expense);
        return expenseRepository.save(expense);
    }

    @Override
    @Transactional(readOnly = true)
    public Expense getExpense(Long id) {
        return expenseRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Expense with id " + id + " not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Expense> getAllExpensesByUser(Long userId) {
        return expenseRepository.findAllByUserId(userId);
    }

    @Override
    @Transactional
    public Expense updateExpense(Expense expense) {
        if (expense.getId() == null) {
            throw new IllegalArgumentException("Expense ID must not be null for update");
        }

        expenseValidator.validate(expense);

        if (!expenseRepository.existsById(expense.getId())) {
            throw new NotFoundException("Expense with id " + expense.getId() + " not found");
        }

        return expenseRepository.save(expense);
    }

    @Override
    @Transactional
    public void deleteExpense(Long id) {
        if (!expenseRepository.existsById(id)) {
            throw new NotFoundException("Expense with id " + id + " not found");
        }
        expenseRepository.deleteById(id);
    }
}
