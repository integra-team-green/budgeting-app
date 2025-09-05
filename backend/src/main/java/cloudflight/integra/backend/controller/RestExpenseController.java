package cloudflight.integra.backend.controller;

import cloudflight.integra.backend.entity.Expense;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import cloudflight.integra.backend.service.ExpenseService;

import java.util.List;

@RestController
@RequestMapping("/expenses")

public class RestExpenseController {
    private final ExpenseService expenseService;
    @Autowired
    public RestExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getExpenseById(@PathVariable Long id) {
        try{
            Expense expense = expenseService.findById(id);
            return ResponseEntity.ok(expense);
            }catch(RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<Expense> addExpense(@RequestBody Expense expense) {
        Expense created = expenseService.createExpense(expense);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Expense>> getAllExpenses(@RequestParam Long userId) {
        List<Expense> expenses = expenseService.findAllByUserId(userId);
        return ResponseEntity.ok(expenses);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateExpense(@PathVariable Long id, @RequestBody Expense expense) {
        if (!id.equals(expense.getId())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("ID in path and request body do not match.");
        }
        Expense updated = expenseService.updateExpense(id, expense);
        return ResponseEntity.ok(updated);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteExpense(@PathVariable Long id) {
        Expense existing = expenseService.findById(id);
        if (existing == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Expense with ID " + id + " not found.");
        }
        expenseService.deleteExpense(id);
        return ResponseEntity.ok().build();
    }
}
