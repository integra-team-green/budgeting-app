package cloudflight.integra.backend.entity.validation;

public interface Validator<T> {
    void validateException(T entity) throws ValidationExceptionExpense;
}
