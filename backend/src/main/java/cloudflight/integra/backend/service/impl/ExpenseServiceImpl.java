package cloudflight.integra.backend.service.impl;

import cloudflight.integra.backend.dto.ExpenseDTO;
import cloudflight.integra.backend.entity.Expense;
import cloudflight.integra.backend.entity.validation.ExpenseValidator;
import cloudflight.integra.backend.exception.NotFoundException;
import cloudflight.integra.backend.mapper.ExpenseMapper;
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
    public ExpenseDTO createExpense(ExpenseDTO expenseDTO) {
        Expense expense = ExpenseMapper.getFromDto(expenseDTO);
        expenseValidator.validate(expense);
        Expense saved = expenseRepository.save(expense);
        return ExpenseMapper.getDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public ExpenseDTO getExpense(Long id) {
        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Expense with id " + id + " not found"));
        return ExpenseMapper.getDto(expense);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExpenseDTO> getAllExpensesByUser(Long userId) {
        return ExpenseMapper.getExpenseDtoFromExpense(expenseRepository.findAllByUserId(userId));
    }

    @Override
    @Transactional
    public ExpenseDTO updateExpense(ExpenseDTO expenseDTO) {
        if (expenseDTO.getId() == null) {
            throw new IllegalArgumentException("Expense ID must not be null for update");
        }

        if (!expenseRepository.existsById(expenseDTO.getId())) {
            throw new NotFoundException("Expense with id " + expenseDTO.getId() + " not found");
        }

        Expense expense = ExpenseMapper.getFromDto(expenseDTO);
        expenseValidator.validate(expense);

        Expense updated = expenseRepository.save(expense);
        return ExpenseMapper.getDto(updated);
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
