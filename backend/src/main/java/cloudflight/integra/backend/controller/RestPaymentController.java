package cloudflight.integra.backend.controller;

import cloudflight.integra.backend.dto.PaymentDTO;
import cloudflight.integra.backend.mapper.PaymentDTOMapper;
import cloudflight.integra.backend.model.Payment;
import cloudflight.integra.backend.model.validator.PaymentNotFoundException;
import cloudflight.integra.backend.model.validator.ValidationException;
import cloudflight.integra.backend.service.IService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/payments")
public class RestPaymentController {
    private cloudflight.integra.backend.service.IService IService;
    private static final Logger log = LoggerFactory.getLogger(RestPaymentController.class);

    @Autowired
    private void setService(IService IService) {
        this.IService = IService;
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody PaymentDTO payment){
        log.debug("Creating payment {}", payment);

        Payment entity= PaymentDTOMapper.getFromDTO(payment);
        Payment saved = IService.addPayment(entity);
        PaymentDTO response = PaymentDTOMapper.getDTO(saved);
        return new ResponseEntity<PaymentDTO>(response, HttpStatus.OK);

    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id){
        log.debug("Get by id "+id);
        Payment request= IService.getPayment(id);
        PaymentDTO response = PaymentDTOMapper.getDTO(request);
        if (request == null) {
            throw new PaymentNotFoundException("Payment with id " + id + " not found");
        }
        return new ResponseEntity<PaymentDTO>(response, HttpStatus.OK);
    }
    @GetMapping
    public ResponseEntity<List<PaymentDTO>> getAll() {
        log.debug("Get all payments");
        List<PaymentDTO> dtos = PaymentDTOMapper.getPaymentDTOsFromPayments(
                StreamSupport.stream(IService.getPayments().spliterator(), false)
                        .collect(Collectors.toList())
        );
        return ResponseEntity.ok(dtos);
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody PaymentDTO body){
        log.debug("Update payment {}", body);
        if (!id.equals(body.getId())) {
            throw new ValidationException(List.of("Path ID and object ID do not match"));
        }
        Payment existing = IService.getPayment(id);
        if (existing == null) {
            throw new PaymentNotFoundException("Payment with id " + id + " not found");
        }

        Payment updated = IService.updatePayment(PaymentDTOMapper.getFromDTO(body));
        return ResponseEntity.ok(PaymentDTOMapper.getDTO(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id){
        log.debug("Delete payment {}", id);

        Payment p = IService.deletePayment(id);
        PaymentDTO response = PaymentDTOMapper.getDTO(p);
        if (response == null)
            throw new PaymentNotFoundException("Payment with id " + id + " not found");
        else {

            return new ResponseEntity<PaymentDTO>(response, HttpStatus.OK);
        }

    }

}
