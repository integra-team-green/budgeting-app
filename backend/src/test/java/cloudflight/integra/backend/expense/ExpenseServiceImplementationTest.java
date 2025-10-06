package cloudflight.integra.backend.expense;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import cloudflight.integra.backend.dto.ExpenseDTO;
import cloudflight.integra.backend.entity.Expense;
import cloudflight.integra.backend.entity.User;
import cloudflight.integra.backend.entity.validation.ExpenseValidator;
import cloudflight.integra.backend.entity.validation.ValidationException;
import cloudflight.integra.backend.exception.NotFoundException;
import cloudflight.integra.backend.repository.ExpenseRepository;
import cloudflight.integra.backend.repository.UserRepository;
import cloudflight.integra.backend.service.impl.ExpenseServiceImpl;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ExpenseServiceImplementationTest {

    @Mock
    private ExpenseRepository expenseRepository;

    @Mock
    private ExpenseValidator expenseValidator;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ExpenseServiceImpl expenseService;

    private User testUser;
    private ExpenseDTO testExpenseDTO;
    private Expense testExpense;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setEmail("test@example.com");

        testExpenseDTO = new ExpenseDTO(
                null,
                1L,
                new BigDecimal("100.50"),
                "Groceries",
                LocalDate.of(2025, 9, 15),
                "Weekly shopping",
                ExpenseDTO.Frequency.ONE_TIME,
                null,
                null,
                ExpenseDTO.PaymentMethod.CARD);

        testExpense = new Expense(
                1L,
                testUser,
                new BigDecimal("100.50"),
                "Groceries",
                LocalDate.of(2025, 9, 15),
                "Weekly shopping",
                Expense.Frequency.ONE_TIME,
                null,
                null,
                Expense.PaymentMethod.CARD);
        testExpense.setUserId(1L); // Set userId for mapper
    }

    @Test
    void createExpense_Success() {
        Expense savedExpense = new Expense(
                1L,
                testUser,
                new BigDecimal("100.50"),
                "Groceries",
                LocalDate.of(2025, 9, 15),
                "Weekly shopping",
                Expense.Frequency.ONE_TIME,
                null,
                null,
                Expense.PaymentMethod.CARD);
        savedExpense.setUserId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(expenseRepository.save(any(Expense.class))).thenReturn(savedExpense);
        doNothing().when(expenseValidator).validate(any(Expense.class));

        ExpenseDTO result = expenseService.createExpense(testExpenseDTO);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getAmount()).isEqualByComparingTo(new BigDecimal("100.50"));
        assertThat(result.getCategory()).isEqualTo("Groceries");
        assertThat(result.getUserId()).isEqualTo(1L);

        verify(userRepository, times(1)).findById(1L);
        verify(expenseValidator, times(1)).validate(any(Expense.class));
        verify(expenseRepository, times(1)).save(any(Expense.class));
    }

    @Test
    void createExpense_UserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> expenseService.createExpense(testExpenseDTO))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("User with id 1 not found");

        verify(userRepository, times(1)).findById(1L);
        verify(expenseRepository, never()).save(any(Expense.class));
        verify(expenseValidator, never()).validate(any(Expense.class));
    }

    @Test
    void createExpense_ValidationFails() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        doThrow(new ValidationException(List.of("Amount must be greater than 0")))
                .when(expenseValidator)
                .validate(any(Expense.class));

        assertThatThrownBy(() -> expenseService.createExpense(testExpenseDTO))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("Amount must be greater than 0");

        verify(userRepository, times(1)).findById(1L);
        verify(expenseValidator, times(1)).validate(any(Expense.class));
        verify(expenseRepository, never()).save(any(Expense.class));
    }

    @Test
    void getExpense_Success() {
        when(expenseRepository.findById(1L)).thenReturn(Optional.of(testExpense));

        ExpenseDTO result = expenseService.getExpense(1L);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getAmount()).isEqualByComparingTo(new BigDecimal("100.50"));
        assertThat(result.getCategory()).isEqualTo("Groceries");

        verify(expenseRepository, times(1)).findById(1L);
    }

    @Test
    void getExpense_NotFound() {
        when(expenseRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> expenseService.getExpense(99L))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Expense with id 99 not found");

        verify(expenseRepository, times(1)).findById(99L);
    }

    @Test
    void getAllExpenses_Success() {
        Expense expense2 = new Expense(
                2L,
                testUser,
                new BigDecimal("50.00"),
                "Transport",
                LocalDate.of(2025, 9, 16),
                "Bus ticket",
                Expense.Frequency.ONE_TIME,
                null,
                null,
                Expense.PaymentMethod.CARD);

        when(expenseRepository.findAll()).thenReturn(List.of(testExpense, expense2));

        Iterable<ExpenseDTO> result = expenseService.getAllExpenses();

        assertThat(result).hasSize(2);
        verify(expenseRepository, times(1)).findAll();
    }

    @Test
    void updateExpense_Success() {
        ExpenseDTO updateDTO = new ExpenseDTO(
                1L,
                1L,
                new BigDecimal("200.00"),
                "Groceries",
                LocalDate.of(2025, 9, 15),
                "Updated shopping",
                ExpenseDTO.Frequency.MONTHLY,
                null,
                null,
                ExpenseDTO.PaymentMethod.TRANSFER);

        when(expenseRepository.findById(1L)).thenReturn(Optional.of(testExpense));
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(expenseRepository.save(any(Expense.class))).thenReturn(testExpense);
        doNothing().when(expenseValidator).validate(any(Expense.class));

        expenseService.updateExpense(updateDTO);

        verify(expenseRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).findById(1L);
        verify(expenseValidator, times(1)).validate(any(Expense.class));
        verify(expenseRepository, times(1)).save(any(Expense.class));
    }

    @Test
    void updateExpense_ExpenseNotFound() {
        testExpenseDTO.setId(99L);
        when(expenseRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> expenseService.updateExpense(testExpenseDTO))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Expense with id 99 not found");

        verify(expenseRepository, times(1)).findById(99L);
        verify(expenseRepository, never()).save(any(Expense.class));
    }

    @Test
    void deleteExpense_Success() {
        when(expenseRepository.existsById(1L)).thenReturn(true);
        doNothing().when(expenseRepository).deleteById(1L);

        expenseService.deleteExpense(1L);

        verify(expenseRepository, times(1)).existsById(1L);
        verify(expenseRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteExpense_NotFound() {
        when(expenseRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> expenseService.deleteExpense(99L))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Expense with id 99 not found");

        verify(expenseRepository, times(1)).existsById(99L);
        verify(expenseRepository, never()).deleteById(anyLong());
    }
}
