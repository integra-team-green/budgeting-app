package cloudflight.integra.backend.model.validator;

import java.util.List;

public class ValidationException extends RuntimeException {
    private final List<String> messages;
    public ValidationException(List<String> message) {
        super(String.join(", " , message));
        this.messages = message;
    }
    public List<String> getMessages() {
        return messages;
    }


}
