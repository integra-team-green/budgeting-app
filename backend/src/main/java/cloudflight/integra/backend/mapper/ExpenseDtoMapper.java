package cloudflight.integra.backend.mapper;

import cloudflight.integra.backend.dto.ExpenseDto;
import cloudflight.integra.backend.entity.Expense;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper utility class for converting between {@link Expense} and {@link ExpenseDto}.
 * All methods are static, and the class should not be instantiated.
 */
public class ExpenseDtoMapper {

    // Private constructor to prevent instantiation
    private ExpenseDtoMapper() {}

    /**
     * Converts an Expense entity to ExpenseDto.
     *
     * @param expense the entity to convert
     * @return the corresponding DTO
     */
    public static ExpenseDto getDto(Expense expense) {
        return new ExpenseDto(
                expense.getId(),
                expense.getUserId(),
                expense.getAmount(),
                expense.getCategory(),
                expense.getDate(),
                expense.getDescription()
        );
    }

    /**
     * Converts an ExpenseDto to Expense entity.
     *
     * @param expenseDto the DTO to convert
     * @return the corresponding entity
     */
    public static Expense getFromDto(ExpenseDto expenseDto) {
        return new Expense(
                expenseDto.getId(),
                expenseDto.getUserId(),
                expenseDto.getAmount(),
                expenseDto.getCategory(),
                expenseDto.getDate(),
                expenseDto.getDescription()
        );
    }

    /**
     * Converts a list of ExpenseDto to a list of Expense entities.
     *
     * @param expenseDtoList the list of DTOs
     * @return list of entities
     */
    public static List<Expense> getExpenseFromDto(List<ExpenseDto> expenseDtoList) {
        return expenseDtoList.stream()
                .map(ExpenseDtoMapper::getFromDto)
                .collect(Collectors.toList());
    }

    /**
     * Converts a list of Expense entities to a list of ExpenseDto.
     *
     * @param expenseList the list of entities
     * @return list of DTOs
     */
    public static List<ExpenseDto> getExpenseDtoFromExpense(List<Expense> expenseList) {
        return expenseList.stream()
                .map(ExpenseDtoMapper::getDto)
                .collect(Collectors.toList());
    }
}
