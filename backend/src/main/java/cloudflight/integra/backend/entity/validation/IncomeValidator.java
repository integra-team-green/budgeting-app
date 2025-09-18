package cloudflight.integra.backend.entity.validation;

import cloudflight.integra.backend.entity.Income;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class IncomeValidator implements Validator<Income> {

  /**
   * Validates the given Income
   *
   * @param income the Income entity that will be validated
   * @throws ValidationException with the attribute where was invalid data and a description of the
   *     error
   */
  public void validate(Income income) {
    List<String> errors = new ArrayList<>();

    if (income.getAmount() == null) {
      errors.add("Amount cannot be null.");
    } else if (income.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
      errors.add("Amount must be greater than zero.");
    }

    if (income.getDate() == null) {
      errors.add("Date cannot be null.");
    } else if (income.getDate().after(new Date())) {
      errors.add("Date cannot be in the future.");
    }

    if (income.getSource() == null || income.getSource().isBlank()) {
      errors.add("Source cannot be empty.");
    }

    if (!errors.isEmpty()) {
      throw new ValidationException(errors);
    }
  }
}
