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
public class TestController {

    @GetMapping("/test")
    public List<String> testEndpoint() {
        return List.of("hello", "world");
    }
}
