package cloudflight.integra.backend.entity.validation;

public interface Validator<T> {
    void validate(T entity) throws ValidationException;
}