package cloudflight.integra.backend.entity.validation;

public class ValidationException extends RuntimeException {

    public ValidationException(String message) {
        super(message);
    }

}
