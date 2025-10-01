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
    public static ExpenseDTO toDTO(Expense expense) {
        ExpenseDTO dto = new ExpenseDTO();
        dto.setId(expense.getId());
        dto.setUserId(expense.getUserId());
        dto.setAmount(expense.getAmount());
        dto.setCategory(expense.getCategory());
        dto.setDate(expense.getDate());
        dto.setDescription(expense.getDescription());

        dto.setFrequency(ExpenseDTO.Frequency.valueOf(expense.getFrequency().name()));

        dto.setPaymentMethod(
                ExpenseDTO.PaymentMethod.valueOf(expense.getPaymentMethod().name()));

        dto.setEndDate(expense.getEndDate());
        dto.setNextDueDate(expense.getNextDueDate());
        return dto;
    }

    /**
     * Converts an ExpenseDTO to Expense entity. Note: The User object should be set separately in
     * service layer if needed.
     *
     * @param dto the DTO to convert
     * @return the corresponding entity
     */
    public static Expense toEntity(ExpenseDTO dto) {
        Expense expense = new Expense();
        expense.setId(dto.getId());
        expense.setUserId(dto.getUserId());
        expense.setAmount(dto.getAmount());
        expense.setCategory(dto.getCategory());
        expense.setDate(dto.getDate());
        expense.setDescription(dto.getDescription());

        if (dto.getFrequency() != null) {
            expense.setFrequency(Expense.Frequency.valueOf(dto.getFrequency().name()));
        } else {
            expense.setFrequency(null);
        }

        if (dto.getPaymentMethod() != null) {
            expense.setPaymentMethod(
                    Expense.PaymentMethod.valueOf(dto.getPaymentMethod().name()));
        } else {
            expense.setPaymentMethod(null);
        }
        expense.setEndDate(dto.getEndDate());
        expense.setNextDueDate(dto.getNextDueDate());
        return expense;
    }

    /**
     * Converts a list of ExpenseDTOs to a list of Expense entities.
     *
     * @param dtoList the list of DTOs
     * @return list of entities
     */
    public static List<Expense> toEntityList(List<ExpenseDTO> dtoList) {
        return dtoList.stream().map(ExpenseMapper::toEntity).collect(Collectors.toList());
    }

    /**
     * Converts a list of Expense entities to a list of ExpenseDTOs.
     *
     * @param entityList the list of entities
     * @return list of DTOs
     */
    public static List<ExpenseDTO> toDTOList(List<Expense> entityList) {
        return entityList.stream().map(ExpenseMapper::toDTO).collect(Collectors.toList());
    }
}
