package cloudflight.integra.backend.validation;

import cloudflight.integra.backend.entity.Credit;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class CreditValidator {
    public static void validateCredit(Credit credit) {
        List<ValidationError> errors = new ArrayList<>();

        if (credit.getAmount() == null || credit.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            errors.add(new ValidationError("amount", "Amount must be greater than 0"));
        }
        if (credit.getLender() == null || credit.getLender().isBlank()) {
            errors.add(new ValidationError("lender", "Lender is required"));
        }
        if (credit.getStartDate() == null) {
            errors.add(new ValidationError("startDate", "Start date is required"));
        }
        if (credit.getDueDate() == null) {
            errors.add(new ValidationError("dueDate", "Due date is required"));
        } else if (credit.getStartDate() != null && credit.getDueDate().isBefore(credit.getStartDate())) {
            errors.add(new ValidationError("dueDate", "Due date must be after start date"));
        }
        if (credit.getInterestRate() == null || credit.getInterestRate() < 0) {
            errors.add(new ValidationError("interestRate", "Interest rate cannot be negative"));
        }

        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }
}
