package cloudflight.integra.backend.entity.validation;

import cloudflight.integra.backend.dto.ExpenseDto;
import cloudflight.integra.backend.entity.Expense;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

@Component
public class ExpenseValidation implements Validator<ExpenseDto> {
    @Override
    public void validateException(ExpenseDto expenseDto) throws ValidationExceptionExpense {
        String errors = "";

        if (expenseDto.getAmount() == null || expenseDto.getAmount().compareTo(BigDecimal.ZERO)<=0) {
            errors+="Amount must be greater than or equal to 0 \n";
        }
        if (expenseDto.getCategory() == null || expenseDto.getCategory().isEmpty()) {
            errors+="Category cannot be empty \n";
        }
        if (expenseDto.getDate() == null){
            errors+="Date cannot be null \n";
        }
        if  (expenseDto.getDate().isAfter(LocalDate.now())) {
            errors+="Expense date cannot be in the future";
        }
        if (!errors.isEmpty())
            throw new ValidationExceptionExpense(errors);
    }
}
