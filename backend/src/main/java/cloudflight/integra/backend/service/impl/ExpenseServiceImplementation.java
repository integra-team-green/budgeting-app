package cloudflight.integra.backend.service.impl;

import cloudflight.integra.backend.entity.Expense;
import cloudflight.integra.backend.service.ExpenseService;
import cloudflight.integra.backend.entity.validation.ValidationExceptionExpense;
import org.springframework.stereotype.Service;
import cloudflight.integra.backend.repository.ExpenseRepository;

import java.util.List;

import cloudflight.integra.backend.entity.validation.ExpenseValidation;

/**
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
    public Expense createExpense(Expense expense) {
        try {
            expenseValidation.validateException(expense);
        } catch (IllegalArgumentException e) {
            throw new ValidationExceptionExpense(e.getMessage());
        }
        return expenseRepository.saveExpense(expense);
    }



    @Override
    public List<Expense> findAllByUserId(Long userId) {

        return expenseRepository.findAllByUserId(userId);
    }

    @Override
    public Expense findById(Long id) {
        return expenseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Expense with id: " + id + " not found"));
    }


    @Override
    public Expense updateExpense(Long id, Expense updatedExpense) {
        Expense existingExpense = expenseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Expense with id: " + id + " not found"));
        expenseValidation.validateException(updatedExpense);
        existingExpense.setAmount(updatedExpense.getAmount());
        existingExpense.setCategory(updatedExpense.getCategory());
        existingExpense.setDate(updatedExpense.getDate());
        existingExpense.setDescription(updatedExpense.getDescription());
        return expenseRepository.saveExpense(existingExpense);
    }

    @Override
    public void deleteExpense(Long id) {
        if (expenseRepository.findById(id).isPresent()) {
            expenseRepository.deleteById(id);
        } else {
            throw new RuntimeException("Expense not found");
        }
    }

}
