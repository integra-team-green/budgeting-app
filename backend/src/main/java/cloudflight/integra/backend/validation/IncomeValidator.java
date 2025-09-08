package cloudflight.integra.backend.validation;

import cloudflight.integra.backend.entity.Income;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class IncomeValidator {
    public void validate(Income income) {
        List<ValidationError> errors = new ArrayList<>();

        if (income.getAmount() == null) {
            errors.add(new ValidationError("amount","Amount cannot be null."));
        } else if (income.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            errors.add(new ValidationError("amount","Amount must be greater than zero."));
        }

        if (income.getDate() == null) {
            errors.add(new ValidationError("date", "Date cannot be null."));
        } else if (income.getDate().after(new Date())) {
            errors.add(new ValidationError("date","Date cannot be in the future."));
        }

        if (income.getSource() == null || income.getSource().isBlank()) {
            errors.add(new ValidationError("source","Source cannot be empty."));
        }

        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }
}
