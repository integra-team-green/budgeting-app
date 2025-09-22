package cloudflight.integra.backend.controller;
import cloudflight.integra.backend.controller.problem.PaymentApiErrorResponses;
import cloudflight.integra.backend.dto.PaymentDTO;
import cloudflight.integra.backend.entity.validation.ValidationException;
import cloudflight.integra.backend.exception.NotFoundException;
import cloudflight.integra.backend.mapper.PaymentMapper;
import cloudflight.integra.backend.entity.Payment;
import cloudflight.integra.backend.repository.ExpenseRepository;
import cloudflight.integra.backend.service.PaymentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;



@RestController
@PaymentApiErrorResponses
@RequestMapping("/api/v1/payments")
public class PaymentController {

    private final PaymentService paymentService;
    private final ExpenseRepository expenseRepository;
    private static final Logger log = LoggerFactory.getLogger(PaymentController.class);

    public PaymentController(PaymentService paymentService, ExpenseRepository expenseRepository) {
        this.paymentService = paymentService;
        this.expenseRepository = expenseRepository;
    }


    @PostMapping
    public ResponseEntity<PaymentDTO> create(@RequestBody PaymentDTO paymentDTO){
        log.debug("Creating payment {}", paymentDTO);
        Payment payment= PaymentMapper.getFromDTO(paymentDTO, expenseRepository);
        Payment savedPayment = paymentService.addPayment(payment);
        PaymentDTO savedDTO = PaymentMapper.getDTO(savedPayment);
        return ResponseEntity.ok(savedDTO);
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id){
        log.debug("Get by id "+id);
        Payment payment = paymentService.getPayment(id);
        PaymentDTO response = PaymentMapper.getDTO(payment);
        if (payment == null) {
            throw new NotFoundException("Payment with id " + id + " not found");
        }
        return ResponseEntity.ok(PaymentMapper.getDTO(payment));
    }


    @GetMapping
    public ResponseEntity<List<PaymentDTO>> getAllPayments() {
        log.debug("Get all payments");
        List<Payment> payments = paymentService.getPayments();
        List<PaymentDTO> dtos = PaymentMapper.getPaymentDTOsFromPayments(payments);
        return ResponseEntity.ok(dtos);
    }



    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody PaymentDTO paymentDTO){
        log.debug("Update payment {}", paymentDTO);
        if (!id.equals(paymentDTO.getId())) {
            throw new ValidationException(List.of("Path ID and object ID do not match"));
        }
        Payment payment = PaymentMapper.getFromDTO(paymentDTO, expenseRepository);
        if (payment == null) {
            throw new NotFoundException("Payment with id " + id + " not found");
        }
        Payment updated = paymentService.updatePayment(payment);
        return ResponseEntity.ok(PaymentMapper.getDTO(updated));
    }



    @DeleteMapping("/{id}")
    public ResponseEntity<PaymentDTO> delete(@PathVariable Long id){
        log.debug("Delete payment {}", id);

        Payment deletedPayment = paymentService.deletePayment(id);
        PaymentDTO response = PaymentMapper.getDTO(deletedPayment);
        if (response == null)
            throw new NotFoundException("Payment with id " + id + " not found");
        else {

            return new ResponseEntity<PaymentDTO>(response, HttpStatus.OK);
        }

    }

}
