package cloudflight.integra.backend.controller;

import cloudflight.integra.backend.model.validator.PaymentNotFoundException;
import cloudflight.integra.backend.model.validator.ValidationException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class RestExceptionHandler {
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<Object> handleCustomException(ValidationException e, HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Validation Failed", request, e.getMessages());
    }

    @ExceptionHandler(PaymentNotFoundException.class)
    public ResponseEntity<Object> handleNotFound(PaymentNotFoundException e, HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, e.getMessage(), request, List.of());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGeneric(Exception e, HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), request, List.of());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Object> handleInvalidEnum(HttpMessageNotReadableException e, HttpServletRequest request) {
        return buildErrorResponse(
                HttpStatus.BAD_REQUEST,
                "Invalid request body: " + e.getMostSpecificCause().getMessage(),
                request,
                List.of("Check if 'frequency' has a valid value")
        );
    }

    private ResponseEntity<Object> buildErrorResponse(HttpStatus status, String message, HttpServletRequest request, List<String> errors) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", Instant.now().toString());
        body.put("message", message);
        body.put("status", status.value());
        body.put("path", request.getRequestURI());
        body.put("errors", status.getReasonPhrase());
        if (!errors.isEmpty()) {
            body.put("details", errors);
        }
        return ResponseEntity.status(status).body(body);
    }
}
