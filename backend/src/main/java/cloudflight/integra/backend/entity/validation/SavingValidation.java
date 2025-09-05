package cloudflight.integra.backend.entity.validation;

import cloudflight.integra.backend.entity.Saving;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class SavingValidation implements Validator<Saving> {

    /**
     * This method validates a Saving object.
     * - amount must be non-negative
     * - goal must be non-empty
     * - date must not be in the future
     *
     * @param saving -
     * @throws ValidationException -
     */
    @Override
    public void validate(Saving saving) throws ValidationException {
        List<String> errors = new ArrayList<>();

        if (saving.getAmount().compareTo(BigDecimal.ZERO) < 0)
            errors.add("Amount must be non-negative.");
        if (saving.getGoal().isEmpty())
            errors.add("Please provide a goal, need to know what you are saving for!");
        if (saving.getDate().after(new Date()))
            errors.add("Date cannot be in the future");
        if (!errors.isEmpty())
            throw new ValidationException(errors);
    }
}
