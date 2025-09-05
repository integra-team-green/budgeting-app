package cloudflight.integra.backend.model.validator;

import cloudflight.integra.backend.model.Frequency;
import cloudflight.integra.backend.model.Payment;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class PaymentValidator implements Validator<Payment>{
    /**
     *Method that validates an object that will be saved
     * @param  entity ,type  Payment
     * @throws ValidationException

     */
    @Override
    public void validate(Payment entity) throws ValidationException {
        if(entity.getAmount().compareTo(BigDecimal.ZERO)<=0)
            throw new ValidationException("Amount must be greater than 0");
        if(entity.getFrequency()!= Frequency.MONTHLY && entity.getFrequency()!= Frequency.YEARLY && entity.getFrequency()!=Frequency.ONE_TIME)
            throw new ValidationException("Invalid frequency");
    }
}
