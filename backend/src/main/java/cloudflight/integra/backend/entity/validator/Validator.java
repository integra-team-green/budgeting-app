package cloudflight.integra.backend.entity.validator;

public interface Validator<T> {
    void validate(T entity) throws ValidationException;
}
