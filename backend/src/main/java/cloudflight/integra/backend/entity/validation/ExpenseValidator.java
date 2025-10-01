package cloudflight.integra.backend.entity.validation;

import cloudflight.integra.backend.entity.Expense;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class ExpenseValidator implements Validator<Expense> {
    @Override
    public void validate(Expense expense) throws ValidationException {
        List<String> errors = new ArrayList<>();

        if (expense.getAmount() == null || expense.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            errors.add("Amount must be greater than 0");
        }

        if (expense.getCategory() == null || expense.getCategory().isEmpty()) {
            errors.add("Category cannot be empty");
        }

        if (expense.getDate() == null) {
            errors.add("Date cannot be null");
        } else if (expense.getDate().isAfter(LocalDate.now())) {
            errors.add("Expense date cannot be in the future");
        }

        if (expense.getFrequency() == null) {
            errors.add("Frequency must be set");
        }

        if (expense.getPaymentMethod() == null) {
            errors.add("Payment method must be set");
        }

        if (expense.getEndDate() != null && expense.getEndDate().isBefore(expense.getDate())) {
            errors.add("End date cannot be before the expense date");
        }

        if (expense.getNextDueDate() != null && expense.getNextDueDate().isBefore(expense.getDate())) {
            errors.add("Next due date cannot be before the expense date");
        }

        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }
}
