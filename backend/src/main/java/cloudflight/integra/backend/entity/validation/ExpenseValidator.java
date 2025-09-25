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

    // Amount must be positive
    if (expense.getAmount() == null || expense.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
      errors.add("Amount must be greater than 0");
    }

    // Category cannot be empty
    if (expense.getCategory() == null || expense.getCategory().isEmpty()) {
      errors.add("Category cannot be empty");
    }

    // Date cannot be null or in the future
    if (expense.getDate() == null) {
      errors.add("Date cannot be null");
    } else if (expense.getDate().isAfter(LocalDate.now())) {
      errors.add("Expense date cannot be in the future");
    }

    // Frequency must not be null
    if (expense.getFrequency() == null) {
      errors.add("Frequency must be set");
    }

    // PaymentMethod must not be null
    if (expense.getPaymentMethod() == null) {
      errors.add("Payment method must be set");
    }

    // Optional: endDate must not be before date (if endDate exists)
    if (expense.getEndDate() == null
        || (expense.getEndDate() != null && expense.getEndDate().isBefore(expense.getDate()))) {
      errors.add("End date cannot be before the expense date");
    }

    // Optional: nextDueDate must not be before date (if nextDueDate exists)
    if (expense.getNextDueDate() == null
        || (expense.getNextDueDate() != null
            && expense.getNextDueDate().isBefore(expense.getDate()))) {
      errors.add("Next due date cannot be before the expense date");
    }

    if (!errors.isEmpty()) {
      throw new ValidationException(errors);
    }
  }
}
