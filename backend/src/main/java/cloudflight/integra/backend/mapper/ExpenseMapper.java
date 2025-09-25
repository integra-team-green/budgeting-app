package cloudflight.integra.backend.mapper;

import cloudflight.integra.backend.dto.ExpenseDTO;
import cloudflight.integra.backend.entity.Expense;
import java.util.List;
import java.util.stream.Collectors;

// varianta buna
/**
 * Mapper utility class for converting between {@link Expense} and {@link ExpenseDTO}. All methods
 * are static, and the class should not be instantiated.
 */
public class ExpenseMapper {

  // Private constructor to prevent instantiation
  private ExpenseMapper() {}

  /**
   * Converts an Expense entity to ExpenseDTO.
   *
   * @param expense the entity to convert
   * @return the corresponding DTO
   */
  public static ExpenseDTO getDto(Expense expense) {
    return new ExpenseDTO(
        expense.getId(),
        expense.getUser().getId(),
        expense.getAmount(),
        expense.getCategory(),
        expense.getDate(),
        expense.getDescription(),
        ExpenseDTO.Frequency.valueOf(expense.getFrequency().name()),
        expense.getEndDate(),
        expense.getNextDueDate(),
        ExpenseDTO.PaymentMethod.valueOf(expense.getPaymentMethod().name()));
  }

  /**
   * Converts an ExpenseDTO to Expense entity. Note: The User object should be set separately in
   * service layer if needed.
   *
   * @param expenseDto the DTO to convert
   * @return the corresponding entity
   */
  public static Expense getFromDto(ExpenseDTO expenseDto) {
    Expense expense = new Expense();
    expense.setId(expenseDto.getId());
    expense.setAmount(expenseDto.getAmount());
    expense.setCategory(expenseDto.getCategory());
    expense.setDate(expenseDto.getDate());
    expense.setDescription(expenseDto.getDescription());
    expense.setFrequency(Expense.Frequency.valueOf(expenseDto.getFrequency().name()));
    expense.setEndDate(expenseDto.getEndDate());
    expense.setNextDueDate(expenseDto.getNextDueDate());
    expense.setPaymentMethod(Expense.PaymentMethod.valueOf(expenseDto.getPaymentMethod().name()));
    return expense;
  }

  /**
   * Converts a list of ExpenseDTOs to a list of Expense entities.
   *
   * @param expenseDtoList the list of DTOs
   * @return list of entities
   */
  public static List<Expense> getExpenseFromDto(List<ExpenseDTO> expenseDtoList) {
    return expenseDtoList.stream().map(ExpenseMapper::getFromDto).collect(Collectors.toList());
  }

  /**
   * Converts a list of Expense entities to a list of ExpenseDTOs.
   *
   * @param expenseList the list of entities
   * @return list of DTOs
   */
  public static List<ExpenseDTO> getExpenseDtoFromExpense(List<Expense> expenseList) {
    return expenseList.stream().map(ExpenseMapper::getDto).collect(Collectors.toList());
  }
}
