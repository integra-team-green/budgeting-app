package cloudflight.integra.backend.controller.problem;

import cloudflight.integra.backend.entity.validation.ValidationException;
import cloudflight.integra.backend.exception.NotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@ControllerAdvice(annotations = UserApiErrorResponses.class)
public class UserRestExceptionHandler {

  /**
   * Handles validation exceptions and returns a 400 Bad Request response with the exception
   * message.
   */
  @ExceptionHandler(ValidationException.class)
  public ResponseEntity<String> handleValidation(ValidationException ex) {
    return ResponseEntity.badRequest().body(ex.getMessage());
  }

  /**
   * Handles illegal argument exceptions and returns a 400 Bad Request response with the exception
   * message.
   */
  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException ex) {
    return ResponseEntity.badRequest().body(ex.getMessage());
  }

  /** Handles not found exceptions and returns a 404 Not Found response. */
  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<String> handleNotFound(NotFoundException ex) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
  }

  /** Handles other runtime exceptions */
  @ExceptionHandler(RuntimeException.class)
  public ResponseEntity<String> handleRuntime(RuntimeException ex) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body("Server error: " + ex.getMessage());
  }

  /**
   * Handles data integrity violations, such as unique constraint violations, and returns a 400 Bad
   * Request response.
   */
  @ExceptionHandler(DataIntegrityViolationException.class)
  public ResponseEntity<String> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body("Data integrity violation: " + ex.getMessage());
  }
}
