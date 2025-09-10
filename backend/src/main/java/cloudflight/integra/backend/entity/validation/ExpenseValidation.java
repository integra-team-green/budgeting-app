package cloudflight.integra.backend.entity.validation;

import cloudflight.integra.backend.dto.ExpenseDto;
import cloudflight.integra.backend.entity.Expense;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class ExpenseValidation implements Validator<ExpenseDto> {
    @Override
    public void validateException(ExpenseDto expenseDto) throws ValidationExceptionExpense {
        List<String> errors = new ArrayList<>();

        if (expenseDto.getAmount() == null || expenseDto.getAmount().compareTo(BigDecimal.ZERO)<=0) {
            errors.add("Amount must be greater than or equal to 0");
        }
        if (expenseDto.getCategory() == null || expenseDto.getCategory().isEmpty()) {
            errors.add("Category cannot be empty");
        }
        if (expenseDto.getDate() == null){
            errors.add("Date cannot be null");
        }
        if  (expenseDto.getDate().isAfter(LocalDate.now())) {
            errors.add("Expense date cannot be in the future");
        }
        if (!errors.isEmpty())
            throw new ValidationExceptionExpense(errors);
    }
}
