package cloudflight.integra.backend.entity.validation;

import cloudflight.integra.backend.dto.PaymentDTO;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

// varianta buna
@Component
public class PaymentValidator implements Validator<PaymentDTO> {

    /**
     * Method that validates a PaymentDTO object before saving.
     *
     * @param entity PaymentDTO object
     * @throws ValidationException if validation fails
     */
    @Override
    public void validate(PaymentDTO entity) throws ValidationException {
        List<String> errors = new ArrayList<>();

        if (entity.getName() == null || entity.getName().trim().isEmpty()) {
            errors.add("Name must not be empty.");
        }
        if (entity.getStatus() == null) {
            errors.add("Status must not be null.");
        }

        if (entity.getPaymentDate() == null) {
            errors.add("Payment date must not be null.");
        }

        if (entity.getExpenseId() == null) {
            errors.add("Expense ID must not be null.");
        } else if (entity.getExpenseId() <= 0) {
            errors.add("Expense ID must be a positive number.");
        }
        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }
}
