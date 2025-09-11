package cloudflight.integra.backend.repository.impl;

import cloudflight.integra.backend.dto.ExpenseDto;
import cloudflight.integra.backend.repository.ExpenseRepository;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;


@Repository
public class InMemoryExpenseRepository implements ExpenseRepository {
    private final Map<Long, ExpenseDto> expenses = new HashMap<>();
    private final AtomicLong nextId = new AtomicLong(1);


    @Override
    public ExpenseDto addExpense(ExpenseDto expenseDto) {
        if(expenseDto.getId() == null) {
            expenseDto.setId(nextId.getAndIncrement());
        }
        expenses.put(expenseDto.getId(), expenseDto);
        return expenseDto;
    }


    @Override
    public List<ExpenseDto> findAllByUserId(Long userId) {
        return expenses.values().stream()
                .filter(e -> e.getUserId().equals(userId))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<ExpenseDto> findById(Long id) {

        return Optional.ofNullable(expenses.get(id));
    }


    @Override
    public void deleteById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Id must not be null");
        }
        if (!expenses.containsKey(id)) {
            throw new IllegalArgumentException("Expense with id " + id + " does not exist.");
        }
        expenses.remove(id);
    }

    @Override
    public ExpenseDto updateExpense(ExpenseDto expense) {
        if (expense.getId() == null || !expenses.containsKey(expense.getId())) {
            throw new IllegalArgumentException(
                    "Expense with id " + expense.getId() + " does not exist"
            );
        }
        expenses.put(expense.getId(), expense);
        return expense;
    }



}
