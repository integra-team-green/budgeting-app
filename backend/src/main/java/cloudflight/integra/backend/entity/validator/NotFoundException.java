package cloudflight.integra.backend.entity.validator;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}