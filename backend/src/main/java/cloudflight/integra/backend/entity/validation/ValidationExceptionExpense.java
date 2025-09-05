package cloudflight.integra.backend.entity.validation;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ValidationExceptionExpense extends RuntimeException {
    public ValidationExceptionExpense(String message) {
        super(message);
    }
}
