package cloudflight.integra.backend.service.impl;

import static cloudflight.integra.backend.mapper.ExpenseMapper.toDTO;

import cloudflight.integra.backend.dto.ExpenseDTO;
import cloudflight.integra.backend.entity.Expense;
import cloudflight.integra.backend.entity.User;
import cloudflight.integra.backend.entity.validation.ExpenseValidator;
import cloudflight.integra.backend.exception.NotFoundException;
import cloudflight.integra.backend.mapper.ExpenseMapper;
import cloudflight.integra.backend.repository.ExpenseRepository;
import cloudflight.integra.backend.repository.UserRepository;
import cloudflight.integra.backend.service.ExpenseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ExpenseServiceImpl implements ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final ExpenseValidator expenseValidator;
    private final UserRepository userRepository;

    public ExpenseServiceImpl(
            ExpenseRepository expenseRepository, ExpenseValidator expenseValidator, UserRepository userRepository) {
        this.expenseRepository = expenseRepository;
        this.expenseValidator = expenseValidator;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public ExpenseDTO createExpense(ExpenseDTO dto) {
        User user = userRepository
                .findById(dto.getUserId())
                .orElseThrow(() -> new NotFoundException("User with id " + dto.getUserId() + " not found"));

        Expense expense = ExpenseMapper.toEntity(dto);
        expense.setUser(user);

        expenseValidator.validate(expense);

        Expense savedExpense = expenseRepository.save(expense);
        return toDTO(savedExpense);
    }

    @Override
    @Transactional(readOnly = true)
    public Iterable<ExpenseDTO> getAllExpenses() {
        return ExpenseMapper.toDTOList(expenseRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public ExpenseDTO getExpense(Long id) {
        Expense expense = expenseRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("Expense with id " + id + " not found"));
        return toDTO(expense);
    }

    @Override
    @Transactional
    public void updateExpense(ExpenseDTO expenseDTO) {
        Expense existingExpense = expenseRepository
                .findById(expenseDTO.getId())
                .orElseThrow(() -> new NotFoundException("Expense with id " + expenseDTO.getId() + " not found"));

        User user = userRepository
                .findById(expenseDTO.getUserId())
                .orElseThrow(() -> new NotFoundException("User with id " + expenseDTO.getUserId() + " not found"));

        Expense expense = ExpenseMapper.toEntity(expenseDTO);
        expense.setUser(user);
        expense.setId(existingExpense.getId());

        expenseValidator.validate(expense);

        expenseRepository.save(expense);
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
