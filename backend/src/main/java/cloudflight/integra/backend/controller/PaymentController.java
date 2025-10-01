package cloudflight.integra.backend.controller;

import cloudflight.integra.backend.controller.problem.PaymentApiErrorResponses;
import cloudflight.integra.backend.dto.PaymentDTO;
import cloudflight.integra.backend.exception.NotFoundException;
import cloudflight.integra.backend.service.PaymentService;
import jakarta.validation.Valid;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/** REST controller for managing payments. Provides CRUD operations for {@link PaymentDTO}. */
@RestController
@PaymentApiErrorResponses
@RequestMapping("/api/v1/payments")
public class PaymentController {

  private static final Logger log = LoggerFactory.getLogger(PaymentController.class);

  private final PaymentService paymentService;

  public PaymentController(PaymentService paymentService) {
    this.paymentService = paymentService;
  }

  @GetMapping("/{id}")
  public ResponseEntity<PaymentDTO> getPaymentById(@PathVariable Long id) {
    log.info("Fetching payment with id={}", id);
    PaymentDTO paymentDto = paymentService.getPaymentById(id);
    log.debug("Found payment: {}", paymentDto);
    return ResponseEntity.ok(paymentDto);
  }

  @PostMapping
  public ResponseEntity<PaymentDTO> createPayment(@Valid @RequestBody PaymentDTO paymentDto) {
    Long expenseId = paymentDto.getExpenseId();
    log.info("Creating new payment for expenseId={}", expenseId);

    PaymentDTO created = paymentService.addPayment(paymentDto);
    log.debug("Created payment: {}", created);
    return new ResponseEntity<>(created, HttpStatus.CREATED);
  }

  @GetMapping
  public ResponseEntity<List<PaymentDTO>> getAllPayments() {
    log.info("Fetching all payments");
    List<PaymentDTO> payments = paymentService.getAllPayments();
    log.debug("Found {} payments", payments.size());
    return ResponseEntity.ok(payments);
  }

  @PutMapping("/{id}")
  public ResponseEntity<PaymentDTO> updatePayment(
      @PathVariable Long id, @Valid @RequestBody PaymentDTO paymentDto) {
    log.info("Updating payment with id={}", id);
    if (!id.equals(paymentDto.getId())) {
      log.warn("ID in path {} does not match ID in body {}", id, paymentDto.getId());
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }
    PaymentDTO updated = paymentService.updatePayment(paymentDto);
    log.debug("Updated payment: {}", updated);
    return ResponseEntity.ok(updated);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<PaymentDTO> deletePayment(@PathVariable Long id) {
    log.info("Deleting payment with id={}", id);
    PaymentDTO deletedPayment = paymentService.deletePayment(id);
    log.debug("Deleted payment: {}", deletedPayment);
    return ResponseEntity.ok(deletedPayment);
  }

  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<String> handleNotFound(NotFoundException ex) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
  }
}
