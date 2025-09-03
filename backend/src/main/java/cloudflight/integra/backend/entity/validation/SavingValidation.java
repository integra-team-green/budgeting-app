package cloudflight.integra.backend.entity.validation;

import cloudflight.integra.backend.entity.Saving;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;

@Component
public class SavingValidation implements Validator<Saving> {

    /**
     * This method validates a Saving object.
     * - amount must be non-negative
     * - goal must be non-empty
     * - date must not be in the future
     *
     * @param saving
     * @throws ValidationException
     */
    @Override
    public void validate(Saving saving) throws ValidationException {

        String errors = "";

        if (saving.getAmount().compareTo(BigDecimal.ZERO) < 0)
            errors += "Amount must be non-negative. \n";
        if (saving.getGoal().isEmpty())
            errors += "Please provide a goal, need to know what you are saving for!\n";
        if (saving.getDate().after(new Date()))
            errors += "Date cannot be in the future";
        if (!errors.isEmpty())
            throw new ValidationException(errors);
    }
}
