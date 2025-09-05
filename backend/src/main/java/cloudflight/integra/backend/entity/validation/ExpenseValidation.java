package cloudflight.integra.backend.entity.validation;

import cloudflight.integra.backend.entity.Expense;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

@Component
public class ExpenseValidation implements Validator<Expense> {
    @Override
    public void validateException(Expense expense) throws ValidationExceptionExpense {
        String errors = "";

        if (expense.getAmount() == null || expense.getAmount().compareTo(BigDecimal.ZERO)<=0) {
            errors+="Amount must be greater than or equal to 0 \n";
        }
        if (expense.getCategory() == null || expense.getCategory().isEmpty()) {
            errors+="Category cannot be empty \n";
        }
        if (expense.getDate() == null){
            errors+="Date cannot be null \n";
        }
        if  (expense.getDate().isAfter(LocalDate.now())) {
            errors+="Expense date cannot be in the future";
        }
        if (!errors.isEmpty())
            throw new ValidationExceptionExpense(errors);
    }
}
