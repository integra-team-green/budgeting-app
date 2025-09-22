package cloudflight.integra.backend.entity.validation;

import cloudflight.integra.backend.dto.PaymentDTO;
import cloudflight.integra.backend.entity.Frequency;
import cloudflight.integra.backend.entity.Payment;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class PaymentValidator implements Validator<PaymentDTO>{
    /**
     *Method that validates an object that will be saved
     * @param  entity ,type  Payment
     * @throws ValidationException

     */
    @Override
    public void validate(PaymentDTO entity) throws ValidationException {
        List<String> errors = new ArrayList<>();
        if(entity.getAmount().compareTo(BigDecimal.ZERO)<=0)
            errors.add("Amount must be greater than 0");
        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }

    }

}
