package cloudflight.integra.backend.service.impl;

import cloudflight.integra.backend.dto.ExpenseDTO;
import cloudflight.integra.backend.entity.Expense;
import cloudflight.integra.backend.entity.validation.ExpenseValidator;
import cloudflight.integra.backend.exception.NotFoundException;
import cloudflight.integra.backend.mapper.ExpenseMapper;
import cloudflight.integra.backend.repository.ExpenseRepository;
import cloudflight.integra.backend.service.ExpenseService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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

        Expense entity = ExpenseMapper.getFromDto(expenseDto);
        Expense saved = expenseRepository.save(entity);
        return ExpenseMapper.getDto(saved);
    }


    @Override
    public ExpenseDTO updateExpense(Long id, ExpenseDTO updatedExpense) {
        expenseValidation.validate(updatedExpense);

        if (updatedExpense.getId() == null || !id.equals(updatedExpense.getId())) {
            throw new IllegalArgumentException("ID in path and DTO do not match");
        }

        Expense existing = expenseRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Expense with id " + id + " not found"));

        existing.setCategory(updatedExpense.getCategory());
        existing.setAmount(updatedExpense.getAmount());
        existing.setDate(updatedExpense.getDate());
        existing.setDescription(updatedExpense.getDescription());
        existing.setUserId(updatedExpense.getUserId());

        Expense saved = expenseRepository.save(existing);
        return ExpenseMapper.getDto(saved);
    }



    @Override
    public List<ExpenseDTO> findAllByUserId(Long userId) {
        return expenseRepository.findAll().stream()  // filtrăm pe userId
                .filter(e -> userId.equals(e.getUserId()))
                .map(ExpenseMapper::getDto)
                .collect(Collectors.toList());
    }

    @Override
    public ExpenseDTO findById(Long id) {
        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Expense with id: " + id + " not found"));
        return ExpenseMapper.getDto(expense);
    }


    public void deleteExpense(Long id) {
        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Expense with id " + id + " not found"));

        ExpenseDTO existingExpense = ExpenseMapper.getDto(expense);
        expenseRepository.deleteById(id);
    }

}
