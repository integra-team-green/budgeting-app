package cloudflight.integra.backend.controller.problem;


import cloudflight.integra.backend.entity.validation.ValidationException;
import cloudflight.integra.backend.exception.MoneyMindRuntimeException;
import cloudflight.integra.backend.exception.NotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice(annotations = SavingApiErrorResponses.class)
public class SavingRestExceptionHandler {

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<Object> handleCustomValidation(ValidationException ex, HttpServletRequest req) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Validation Failed", req, ex.getErrors());
    }

    @ExceptionHandler(MoneyMindRuntimeException.class)
    public ResponseEntity<Object> handleCustomRuntimeException(MoneyMindRuntimeException ex, HttpServletRequest req) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), req, List.of());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException ex, HttpServletRequest req) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), req, List.of());
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Object> handleNotFound(NotFoundException ex, HttpServletRequest req) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage(), req, List.of());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGeneric(Exception ex, HttpServletRequest req) {
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), req, List.of());
    }

    private ResponseEntity<Object> buildErrorResponse(HttpStatus status, String message,
                                                      HttpServletRequest req, List<String> errors) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", Instant.now().toString());
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("path", req.getRequestURI());
        body.put("message", message);
        if (!errors.isEmpty()) {
            body.put("details", errors);
        }
        return ResponseEntity.status(status).body(body);
    }
}
