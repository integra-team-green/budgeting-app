package cloudflight.integra.backend.entity.validation;

import cloudflight.integra.backend.entity.Expense;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class ExpenseValidator implements Validator<Expense> {
    @Override
    public void validate(Expense expense) throws ValidationException {
        List<String> errors = new ArrayList<>();

        if (expense.getAmount() == null || expense.getAmount().compareTo(BigDecimal.ZERO)<=0) {
            errors.add("Amount must be greater than or equal to 0");
        }
        if (expense.getCategory() == null || expense.getCategory().isEmpty()) {
            errors.add("Category cannot be empty");
        }
        if (expense.getDate() == null){
            errors.add("Date cannot be null");
        }
        if  (expense.getDate().isAfter(LocalDate.now())) {
            errors.add("Expense date cannot be in the future");
        }
        if (!errors.isEmpty())
            throw new ValidationException(errors);
    }

}
