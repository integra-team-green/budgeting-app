package cloudflight.integra.backend.controller.problem;

import cloudflight.integra.backend.entity.validation.ValidationException;
import cloudflight.integra.backend.exception.NotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice(annotations = PaymentApiErrorResponses.class)
public class PaymentRestExceptionHandler {

  @ExceptionHandler(ValidationException.class)
  public ResponseEntity<Object> handleValidationException(
      ValidationException e, HttpServletRequest request) {
    return buildErrorResponse(HttpStatus.BAD_REQUEST, "Validation Failed", request, e.getErrors());
  }

  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<Object> handleNotFoundException(
      NotFoundException e, HttpServletRequest request) {
    return buildErrorResponse(HttpStatus.NOT_FOUND, e.getMessage(), request, List.of());
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<Object> handleInvalidRequestBody(
      HttpMessageNotReadableException e, HttpServletRequest request) {
    return buildErrorResponse(
        HttpStatus.BAD_REQUEST,
        "Invalid request body: " + e.getMostSpecificCause().getMessage(),
        request,
        List.of("Check if enum or data types are valid"));
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<Object> handleGenericException(Exception e, HttpServletRequest request) {
    return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), request, List.of());
  }

  private ResponseEntity<Object> buildErrorResponse(
      HttpStatus status, String message, HttpServletRequest request, List<String> details) {

    Map<String, Object> body = new LinkedHashMap<>();
    body.put("timestamp", Instant.now().toString());
    body.put("status", status.value());
    body.put("error", status.getReasonPhrase());
    body.put("message", message);
    body.put("path", request.getRequestURI());
    if (details != null && !details.isEmpty()) {
      body.put("details", details);
    }

    return ResponseEntity.status(status).body(body);
  }
}
