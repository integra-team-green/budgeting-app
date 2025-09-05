package cloudflight.integra.backend.controller;


import cloudflight.integra.backend.dto.PaymentDTO;
import cloudflight.integra.backend.mapper.PaymentDTOMapper;
import cloudflight.integra.backend.model.Payment;
import cloudflight.integra.backend.service.IService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/payments")
public class TestController {
    private IService IService;

    @Autowired
    private void setService(IService IService) {
        this.IService = IService;
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody PaymentDTO payment){
     try{
        Payment entity= PaymentDTOMapper.getFromDTO(payment);
        Payment saved = IService.addPayment(entity);
        PaymentDTO response = PaymentDTOMapper.getDTO(saved);
        return new ResponseEntity<PaymentDTO>(response, HttpStatus.OK);
     } catch (RuntimeException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
     }

    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id){
        System.out.println("Get by id "+id);
        Payment request= IService.getPayment(id);
        PaymentDTO response = PaymentDTOMapper.getDTO(request);
        if (response==null)
            return new ResponseEntity<String>("Entity not found", HttpStatus.NOT_FOUND);
        else
            return new ResponseEntity<PaymentDTO>(response, HttpStatus.OK);
    }
    @GetMapping
    public ResponseEntity<List<PaymentDTO>> getAll() {
        List<PaymentDTO> dtos = PaymentDTOMapper.getPaymentDTOsFromPayments(
                StreamSupport.stream(IService.getPayments().spliterator(), false)
                        .collect(Collectors.toList())
        );
        return ResponseEntity.ok(dtos);
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody PaymentDTO body){
        System.out.println("Updating payment with id: " + body.getId());
        if (!id.equals(body.getId())) {
            return new ResponseEntity<String>("Path Id and Object Id do not match!", HttpStatus.BAD_REQUEST);
        }
        try {
            Payment entity= PaymentDTOMapper.getFromDTO(body);
            Payment existing_p = IService.getPayment(entity.getId());
            if (existing_p == null)
                return new ResponseEntity<String>("Payment not found", HttpStatus.NOT_FOUND);
            else {
                Payment updated= IService.updatePayment(entity);
                PaymentDTO response = PaymentDTOMapper.getDTO(updated);
                System.out.println("Payment updated ..." + body);
                return new ResponseEntity<PaymentDTO>(response, HttpStatus.OK);
            }
        } catch (Exception ex){
            return new ResponseEntity<String>(ex.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id){
        System.out.println("Deleting payment with id: " + id);
        try {
            Payment p = IService.deletePayment(id);
            PaymentDTO response = PaymentDTOMapper.getDTO(p);
            if (response == null)
                return new ResponseEntity<String>("Payment not found", HttpStatus.NOT_FOUND);
            else {

                System.out.println("Payment deleted ..." + p);
                return new ResponseEntity<PaymentDTO>(response, HttpStatus.OK);
            }
        } catch (Exception ex){
            return new ResponseEntity<String>(ex.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/test")
    public List<String> testEndpoint() {
        return List.of("hello", "world");
    }
}
