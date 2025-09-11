package cloudflight.integra.backend.service.impl;

import cloudflight.integra.backend.dto.ExpenseDto;
import cloudflight.integra.backend.entity.validation.ExpenseValidation;
import cloudflight.integra.backend.exceptions.ResourceNotFoundException;
import cloudflight.integra.backend.repository.ExpenseRepository;
import cloudflight.integra.backend.service.ExpenseService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 * Implementation of ExpenseService using in-memory repository.
 */
@Service
public class ExpenseServiceImplementation implements ExpenseService {
    private final ExpenseRepository expenseRepository;
    private final ExpenseValidation expenseValidation;

    public ExpenseServiceImplementation(ExpenseRepository expenseRepository, ExpenseValidation expenseValidation) {
        this.expenseRepository = expenseRepository;
        this.expenseValidation = expenseValidation;
    }


    @Override
    public ExpenseDto createExpense(ExpenseDto expenseDto) {
        expenseValidation.validate(expenseDto);
        return expenseRepository.addExpense(expenseDto);
    }


    @Override
    public ExpenseDto updateExpense(Long id, ExpenseDto updatedExpense) {
        expenseValidation.validate(updatedExpense);

        if (updatedExpense.getId() == null || !id.equals(updatedExpense.getId())) {
            throw new IllegalArgumentException("ID in path and DTO do not match");
        }
        ExpenseDto existingExpense = expenseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Expense with id " + id + " not found"));
        return expenseRepository.updateExpense(updatedExpense);
    }



    @Override
    public List<ExpenseDto> findAllByUserId(Long userId) {

        return expenseRepository.findAllByUserId(userId);
    }

    @Override
    public ExpenseDto findById(Long id) {
        return expenseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Expense with id: " + id + " not found"));
    }


    public void deleteExpense(Long id) {
        ExpenseDto existing = expenseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Expense with id " + id + " not found"));
        expenseRepository.deleteById(id);
    }

}
