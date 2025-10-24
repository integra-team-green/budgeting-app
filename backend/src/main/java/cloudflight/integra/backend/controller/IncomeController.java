package cloudflight.integra.backend.controller;

import cloudflight.integra.backend.controller.problem.IncomeApiErrorResponses;
import cloudflight.integra.backend.dto.IncomeDTO;
import cloudflight.integra.backend.service.IncomeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.util.List;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/incomes")
@IncomeApiErrorResponses
public class IncomeController {

    private static final Logger log = LoggerFactory.getLogger(IncomeController.class);

    private final IncomeService incomeService;

    public IncomeController(IncomeService incomeService) {
        this.incomeService = incomeService;
    }

    @PostMapping
    public ResponseEntity<IncomeDTO> createIncome(@RequestBody IncomeDTO dto) {
        log.info("Creating income: {}", dto);
        IncomeDTO saved = incomeService.createIncome(dto);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<IncomeDTO> getIncomeById(@PathVariable Long id) {
        log.debug("Fetching income with id {}", id);
        IncomeDTO income = incomeService.getIncomeById(id);
        return new ResponseEntity<>(income, HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<List<IncomeDTO>> getAllIncomes() {
        log.debug("Fetching all incomes");
        List<IncomeDTO> incomes = StreamSupport.stream(
                        incomeService.getAllIncomes().spliterator(), false)
                .toList();
        return new ResponseEntity<>(incomes, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<IncomeDTO> updateIncome(@PathVariable Long id, @RequestBody IncomeDTO dto) {
        log.info("Updating income with id {}", id);
        dto.setId(id);
        IncomeDTO income = incomeService.updateIncome(dto);
        return new ResponseEntity<>(income, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIncome(@PathVariable Long id) {
        log.warn("Deleting income with id {}", id);
        incomeService.deleteIncome(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
