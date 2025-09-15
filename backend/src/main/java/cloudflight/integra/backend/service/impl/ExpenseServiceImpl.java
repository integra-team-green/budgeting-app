package cloudflight.integra.backend.service.impl;

import cloudflight.integra.backend.dto.ExpenseDTO;
import cloudflight.integra.backend.entity.validation.ExpenseValidator;
import cloudflight.integra.backend.exception.NotFoundException;
import cloudflight.integra.backend.repository.ExpenseRepository;
import cloudflight.integra.backend.service.ExpenseService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 * Implementation of ExpenseService using in-memory repository.
 */
@Service
public class ExpenseServiceImpl implements ExpenseService {
    private final ExpenseRepository expenseRepository;
    private final ExpenseValidator expenseValidation;

    public ExpenseServiceImpl(ExpenseRepository expenseRepository, ExpenseValidator expenseValidation) {
        this.expenseRepository = expenseRepository;
        this.expenseValidation = expenseValidation;
    }


    @Override
    public ExpenseDTO createExpense(ExpenseDTO expenseDto) {
        expenseValidation.validate(expenseDto);
        return expenseRepository.addExpense(expenseDto);
    }


    @Override
    public ExpenseDTO updateExpense(Long id, ExpenseDTO updatedExpense) {
        expenseValidation.validate(updatedExpense);

        if (updatedExpense.getId() == null || !id.equals(updatedExpense.getId())) {
            throw new IllegalArgumentException("ID in path and DTO do not match");
        }
        ExpenseDTO existingExpense = expenseRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Expense with id " + id + " not found"));
        return expenseRepository.updateExpense(updatedExpense);
    }



    @Override
    public List<ExpenseDTO> findAllByUserId(Long userId) {

        return expenseRepository.findAllByUserId(userId);
    }

    @Override
    public ExpenseDTO findById(Long id) {
        return expenseRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Expense with id: " + id + " not found"));
    }


    public void deleteExpense(Long id) {
        ExpenseDTO existing = expenseRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Expense with id " + id + " not found"));
        expenseRepository.deleteById(id);
    }

}
