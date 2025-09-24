package cloudflight.integra.backend.entity.validation;

import cloudflight.integra.backend.dto.PaymentDTO;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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
        if (entity.getAmount() == null) {
            errors.add("Amount must not be null");
        } else if (entity.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            errors.add("Amount must be greater than 0");
        }

        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }
}
