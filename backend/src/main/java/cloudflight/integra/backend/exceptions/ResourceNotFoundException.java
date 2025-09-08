package cloudflight.integra.backend.exceptions;

/**
 * Exception thrown when a resource (like an Expense) is not found in the system.
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
