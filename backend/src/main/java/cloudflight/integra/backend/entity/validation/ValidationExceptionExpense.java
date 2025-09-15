package cloudflight.integra.backend.entity.validation;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ValidationExceptionExpense extends RuntimeException {
    private final List<String> errors;

    public ValidationExceptionExpense(List<String> errors) {
        super("Validation failed with " + errors.size() + " error(s).");
        this.errors = errors;
    }

    public List<String> getErrors() {
        return errors;
    }
}
