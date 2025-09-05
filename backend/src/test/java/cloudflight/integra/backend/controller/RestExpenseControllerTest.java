package cloudflight.integra.backend.controller;


import cloudflight.integra.backend.entity.Expense;
import cloudflight.integra.backend.entity.validation.ExpenseValidation;
import cloudflight.integra.backend.entity.validation.Validator;
import cloudflight.integra.backend.service.ExpenseService;
import cloudflight.integra.backend.service.impl.ExpenseServiceImplementation;
import cloudflight.integra.backend.repository.impl.InMemoryExpenseRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDate;


import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class RestExpenseControllerTests {

    private ExpenseService expenseService;
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        InMemoryExpenseRepository repository = new InMemoryExpenseRepository();
        ExpenseValidation validator = new ExpenseValidation();
        repository.saveExpense(new Expense(1L, 1L, new BigDecimal("50.00"), "Food", LocalDate.now(), "Lunch"));
        repository.saveExpense(new Expense(2L, 1L, new BigDecimal("150.00"), "Transport", LocalDate.now(), "Taxi"));
        repository.saveExpense(new Expense(3L, 2L, new BigDecimal("200.00"), "Shopping", LocalDate.now(), "Clothes"));


        expenseService = new ExpenseServiceImplementation(repository, validator);
        RestExpenseController controller = new RestExpenseController(expenseService);

        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules(); // VERY IMPORTANT pentru LocalDate
    }

    @Test
    void testGetAllExpenses() throws Exception {
        mockMvc.perform(get("/expenses").param("userId", "1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void testGetExpenseById() throws Exception {
        mockMvc.perform(get("/expenses/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.amount").value(50.00))
                .andExpect(jsonPath("$.category").value("Food"));
        mockMvc.perform(get("/expenses/2"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.amount").value(150.00))
                .andExpect(jsonPath("$.category").value("Transport"));
        mockMvc.perform(get("/expenses/10"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateExpense() throws Exception {
        Expense newExpense = new Expense(4L, 1L, new BigDecimal("75.00"), "Groceries", LocalDate.now(), "Weekly shopping");

        mockMvc.perform(post("/expenses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newExpense)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(4))
                .andExpect(jsonPath("$.amount").value(75.00))
                .andExpect(jsonPath("$.category").value("Groceries"));
    }

    @Test
    void testUpdateExpense() throws Exception {
        Expense updated = new Expense(1L, 1L, new BigDecimal("55.00"), "Food", LocalDate.now(), "Lunch updated");

        mockMvc.perform(put("/expenses/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.amount").value(55.00))
                .andExpect(jsonPath("$.description").value("Lunch updated"));
    }

    @Test
    void testDeleteExpense() throws Exception {
        mockMvc.perform(delete("/expenses/2"))
                .andDo(print())
                .andExpect(status().isOk());

        mockMvc.perform(get("/expenses").param("userId", "1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }
}
