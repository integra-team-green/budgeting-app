package cloudflight.integra.backend.controller;

import cloudflight.integra.backend.controller.problem.CreditApiErrorResponses;
import cloudflight.integra.backend.dto.CreditDTO;
import cloudflight.integra.backend.entity.Credit;
import cloudflight.integra.backend.mapper.CreditMapper;
import cloudflight.integra.backend.service.CreditService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@CreditApiErrorResponses
@RequestMapping("/api/v1/credits")
public class CreditController {

    private static final Logger log = LoggerFactory.getLogger(CreditController.class);
    private final CreditService creditService;

    public CreditController(CreditService creditService) {
        this.creditService = creditService;
    }

    @PostMapping
    public CreditDTO createCredit(@RequestBody CreditDTO dto) {
        log.info("Creating credit for lender {}", dto.getLender());
        Credit created = creditService.create(CreditMapper.toEntity(dto));
        return CreditMapper.toDto(created);
    }

    @GetMapping("/{id}")
    public CreditDTO getCredit(@PathVariable Long id) {
        log.debug("Fetching credit {}", id);
        return CreditMapper.toDto(
            creditService.findById(id));
    }

    @GetMapping
    public List<CreditDTO> getAllCredits() {
        log.debug("Fetching all credits");
        return creditService.findAll().stream().map(CreditMapper::toDto).collect(Collectors.toList());
    }

    @PutMapping("/{id}")
    public CreditDTO updateCredit(@PathVariable Long id, @RequestBody CreditDTO dto) {
        log.info("Updating credit {} with lender {}", id, dto.getLender());
        Credit updated = creditService.update(id, CreditMapper.toEntity(dto));
        return CreditMapper.toDto(updated);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCredit(@PathVariable Long id) {
        log.warn("Deleting credit {}", id);
        creditService.delete(id);
    }
}