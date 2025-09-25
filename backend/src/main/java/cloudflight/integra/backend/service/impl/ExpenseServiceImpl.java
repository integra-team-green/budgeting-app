package cloudflight.integra.backend.service.impl;

import static cloudflight.integra.backend.mapper.ExpenseMapper.getDto;

import cloudflight.integra.backend.dto.ExpenseDTO;
import cloudflight.integra.backend.entity.Expense;
import cloudflight.integra.backend.entity.User;
import cloudflight.integra.backend.entity.validation.ExpenseValidator;
import cloudflight.integra.backend.exception.NotFoundException;
import cloudflight.integra.backend.mapper.ExpenseMapper;
import cloudflight.integra.backend.repository.ExpenseRepository;
import cloudflight.integra.backend.repository.UserRepository;
import cloudflight.integra.backend.service.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// varianta buna
@Service
public class ExpenseServiceImpl implements ExpenseService {

  private final ExpenseRepository expenseRepository;
  private final ExpenseValidator expenseValidator;

  @Autowired private UserRepository userRepository;

  public ExpenseServiceImpl(
      ExpenseRepository expenseRepository, ExpenseValidator expenseValidator) {
    this.expenseRepository = expenseRepository;
    this.expenseValidator = expenseValidator;
  }

  @Override
  @Transactional
  public ExpenseDTO createExpense(ExpenseDTO dto) {
    Expense expense = new Expense();
    expense.setCategory(dto.getCategory());
    expense.setAmount(dto.getAmount());
    expense.setDate(dto.getDate());

    expense.setFrequency(Expense.Frequency.valueOf(dto.getFrequency().name()));
    expense.setPaymentMethod(Expense.PaymentMethod.valueOf(dto.getPaymentMethod().name()));

    User user =
        userRepository
            .findById(dto.getUserId())
            .orElseThrow(() -> new RuntimeException("User not found with id: " + dto.getUserId()));
    expense.setUser(user);

    Expense savedExpense = expenseRepository.save(expense);

    ExpenseDTO savedDto = new ExpenseDTO();
    savedDto.setId(savedExpense.getId());
    savedDto.setCategory(savedExpense.getCategory());
    savedDto.setAmount(savedExpense.getAmount());
    savedDto.setDate(savedExpense.getDate());
    savedDto.setFrequency(ExpenseDTO.Frequency.valueOf(savedExpense.getFrequency().name()));
    savedDto.setPaymentMethod(
        ExpenseDTO.PaymentMethod.valueOf(savedExpense.getPaymentMethod().name()));
    savedDto.setUserId(savedExpense.getUser().getId());

    return savedDto;
  }

  @Override
  @Transactional(readOnly = true)
  public Iterable<ExpenseDTO> getAllExpenses() {
    return ExpenseMapper.getExpenseDtoFromExpense(expenseRepository.findAll());
  }

  @Override
  @Transactional(readOnly = true)
  public ExpenseDTO getExpense(Long id) {
    Expense expense =
        expenseRepository
            .findById(id)
            .orElseThrow(() -> new NotFoundException("Expense with id " + id + " not found"));
    return getDto(expense);
  }

  @Override
  @Transactional
  public void updateExpense(ExpenseDTO expenseDTO) {
    if (expenseDTO.getId() == null) {
      throw new IllegalArgumentException("Expense ID must not be null for update");
    }

    if (!expenseRepository.existsById(expenseDTO.getId())) {
      throw new NotFoundException("Expense with id " + expenseDTO.getId() + " not found");
    }

    Expense expense = ExpenseMapper.getFromDto(expenseDTO);
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
