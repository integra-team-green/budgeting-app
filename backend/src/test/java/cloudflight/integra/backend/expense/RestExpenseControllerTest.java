package cloudflight.integra.backend.expense;

import cloudflight.integra.backend.controller.RestExpenseController;
import cloudflight.integra.backend.dto.ExpenseDTO;
import cloudflight.integra.backend.entity.validation.ExpenseValidator;
import cloudflight.integra.backend.repository.inMemoryImpl.InMemoryExpenseRepository;
import cloudflight.integra.backend.service.ExpenseService;
import cloudflight.integra.backend.service.impl.ExpenseServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Unit tests for {@link RestExpenseController}.
 * Tests CRUD operations via MockMvc using ExpenseDto objects.
 */
class RestExpenseControllerTests {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    /**
     * Sets up the in-memory repository, validator, service, controller, and MockMvc before each test.
     * Configures Spring Validator to properly handle @Valid annotations in controller.
     */
    @BeforeEach
    void setUp() {
        InMemoryExpenseRepository repository = new InMemoryExpenseRepository();

        repository.addExpense(new ExpenseDTO(1L, 1L, new BigDecimal("50.00"), "Food", LocalDate.now(), "Lunch"));
        repository.addExpense(new ExpenseDTO(2L, 1L, new BigDecimal("150.00"), "Transport", LocalDate.now(), "Taxi"));
        repository.addExpense(new ExpenseDTO(3L, 2L, new BigDecimal("200.00"), "Shopping", LocalDate.now(), "Clothes"));

        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.afterPropertiesSet();

        ExpenseService expenseService = new ExpenseServiceImpl(repository, new ExpenseValidator());
        RestExpenseController controller = new RestExpenseController(expenseService);


        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setValidator(validator) // enables @Valid
                .build();

        // ObjectMapper for JSON serialization/deserialization
        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules(); // important for LocalDate
    }

    @Test
    void testGetAllExpenses() throws Exception {
        mockMvc.perform(get("/api/v1/expenses").param("userId", "1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void testGetExpenseById() throws Exception {
        mockMvc.perform(get("/api/v1/expenses/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.amount").value(50.00))
                .andExpect(jsonPath("$.category").value("Food"));

        mockMvc.perform(get("/api/v1/expenses/2"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.amount").value(150.00))
                .andExpect(jsonPath("$.category").value("Transport"));

        mockMvc.perform(get("/api/v1/expenses/10")) // non-existing ID
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateExpense() throws Exception {
        // All fields non-null, amount positive
        ExpenseDTO newExpense = new ExpenseDTO(null, 1L, new BigDecimal("75.00"), "Groceries", LocalDate.now(), "Weekly shopping");

        mockMvc.perform(post("/api/v1/expenses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newExpense)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.amount").value(75.00))
                .andExpect(jsonPath("$.category").value("Groceries"))
                .andExpect(jsonPath("$.description").value("Weekly shopping"));
    }

    @Test
    void testUpdateExpense() throws Exception {
        ExpenseDTO updatedExpense = new ExpenseDTO(1L, 1L, new BigDecimal("55.00"), "Food", LocalDate.now(), "Lunch updated");

        mockMvc.perform(put("/api/v1/expenses/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedExpense)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.amount").value(55.00))
                .andExpect(jsonPath("$.description").value("Lunch updated"));
    }

    @Test
    void testDeleteExpense() throws Exception {
        mockMvc.perform(delete("/api/v1/expenses/2"))
                .andDo(print())
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/v1/expenses").param("userId", "1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }
}
