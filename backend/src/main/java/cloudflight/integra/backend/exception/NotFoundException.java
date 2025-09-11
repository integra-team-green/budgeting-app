package cloudflight.integra.backend.exception;


/**
 * Exception thrown when a requested resource cannot be found.
 */
public class NotFoundException extends RuntimeException {

    private final String resourceName;
    private final Object resourceId;

    public NotFoundException(String resourceName, Object resourceId) {
        super(String.format("%s with id %s was not found", resourceName, resourceId));
        this.resourceName = resourceName;
        this.resourceId = resourceId;
    }

    public NotFoundException(String message) {
        super(message);
        this.resourceName = null;
        this.resourceId = null;
    }

    public String getResourceName() {
        return resourceName;
    }

    public Object getResourceId() {
        return resourceId;
    }
}
