package cloudflight.integra.backend.controller;

import cloudflight.integra.backend.dto.IncomeDTO;
import cloudflight.integra.backend.entity.Income;
import cloudflight.integra.backend.mapper.IncomeMapper;
import cloudflight.integra.backend.service.IncomeService;
import cloudflight.integra.backend.exception.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/api/incomes")
public class IncomeRestController {

    private static final Logger logger = LoggerFactory.getLogger(IncomeRestController.class);

    private final IncomeService incomeService;

    public IncomeRestController(IncomeService incomeService) {
        this.incomeService = incomeService;
    }

    @PostMapping
    public ResponseEntity<IncomeDTO> createIncome(@RequestBody IncomeDTO dto) {
        logger.info("Creating income: {}", dto);
        Income income = IncomeMapper.toEntity(dto);
        incomeService.createIncome(income);
        return new ResponseEntity<>(IncomeMapper.toDTO(income), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<IncomeDTO> getIncomeById(@PathVariable Long id) {
        logger.info("Fetching income with id {}", id);
        Income income = incomeService.getIncomeById(id);
        if (income == null) {
            logger.warn("Income with id {} not found", id);
            throw new NotFoundException("Income with id " + id + " not found");
        }
        return new ResponseEntity<>(IncomeMapper.toDTO(income), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<IncomeDTO>> getAllIncomes() {
        logger.info("Fetching all incomes");
        List<IncomeDTO> dtos = StreamSupport
                .stream(incomeService.getAllIncomes().spliterator(), false)
                .map(IncomeMapper::toDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<IncomeDTO> updateIncome(@PathVariable Long id, @RequestBody IncomeDTO dto) {
        logger.info("Updating income with id {}", id);
        Income income = IncomeMapper.toEntity(dto);
        income.setId(id);
        incomeService.updateIncome(income);
        return new ResponseEntity<>(IncomeMapper.toDTO(income), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIncome(@PathVariable Long id) {
        logger.info("Deleting income with id {}", id);
        Income income = incomeService.getIncomeById(id);
        if (income == null) {
            logger.warn("Income with id {} not found for delete", id);
            throw new NotFoundException("Income with id " + id + " not found");
        }
        incomeService.deleteIncome(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
