package cloudflight.integra.backend.expense;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import cloudflight.integra.backend.dto.ExpenseDTO;
import cloudflight.integra.backend.entity.Expense;
import cloudflight.integra.backend.entity.User;
import cloudflight.integra.backend.repository.ExpenseRepository;
import cloudflight.integra.backend.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(
        properties = {
            "spring.datasource.url=jdbc:h2:mem:testdb",
            "spring.datasource.driver-class-name=org.h2.Driver",
            "spring.datasource.username=sa",
            "spring.datasource.password=",
            "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
            "spring.jpa.hibernate.ddl-auto=create-drop",
            "spring.jpa.show-sql=false"
        })
class RestExpenseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private UserRepository userRepository;

    private User testUser;
    private Expense testExpense;

    @BeforeEach
    void setUp() {
        expenseRepository.deleteAll();
        userRepository.deleteAll();

        testUser = new User();
        testUser.setEmail("test@example.com");
        testUser.setPassword("password123");
        testUser = userRepository.save(testUser);

        testExpense = new Expense();
        testExpense.setUser(testUser);
        testExpense.setUserId(testUser.getId());
        testExpense.setAmount(new BigDecimal("150.00"));
        testExpense.setCategory("Groceries");
        testExpense.setDate(LocalDate.of(2025, 9, 15));
        testExpense.setDescription("Weekly shopping");
        testExpense.setFrequency(Expense.Frequency.ONE_TIME);
        testExpense.setPaymentMethod(Expense.PaymentMethod.CARD);
        testExpense = expenseRepository.save(testExpense);
    }

    @AfterEach
    void tearDown() {
        // Clean up after each test for isolation
        expenseRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @Transactional
    void createExpense_Success() throws Exception {
        // Given
        ExpenseDTO newExpenseDTO = new ExpenseDTO(
                null,
                testUser.getId(),
                new BigDecimal("99.99"),
                "Transport",
                LocalDate.of(2025, 9, 20),
                "Bus monthly pass",
                ExpenseDTO.Frequency.MONTHLY,
                null,
                LocalDate.of(2025, 10, 20),
                ExpenseDTO.PaymentMethod.TRANSFER);

        // When & Then
        mockMvc.perform(post("/api/v1/expenses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newExpenseDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.userId").value(testUser.getId()))
                .andExpect(jsonPath("$.amount").value(99.99))
                .andExpect(jsonPath("$.category").value("Transport"))
                .andExpect(jsonPath("$.description").value("Bus monthly pass"))
                .andExpect(jsonPath("$.frequency").value("MONTHLY"))
                .andExpect(jsonPath("$.paymentMethod").value("TRANSFER"));

        assertThat(expenseRepository.count()).isEqualTo(2);
    }

    @Test
    void createExpense_UserNotFound() throws Exception {
        ExpenseDTO invalidExpenseDTO = new ExpenseDTO(
                null,
                999L,
                new BigDecimal("50.00"),
                "Entertainment",
                LocalDate.of(2025, 9, 20),
                "Cinema",
                ExpenseDTO.Frequency.ONE_TIME,
                null,
                null,
                ExpenseDTO.PaymentMethod.CARD);

        mockMvc.perform(post("/api/v1/expenses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidExpenseDTO)))
                .andExpect(status().isNotFound());

        assertThat(expenseRepository.count()).isEqualTo(1);
    }

    @Test
    void getExpenseById_Success() throws Exception {
        mockMvc.perform(get("/api/v1/expenses/{id}", testExpense.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testExpense.getId()))
                .andExpect(jsonPath("$.userId").value(testUser.getId()))
                .andExpect(jsonPath("$.amount").value(150.00))
                .andExpect(jsonPath("$.category").value("Groceries"))
                .andExpect(jsonPath("$.description").value("Weekly shopping"))
                .andExpect(jsonPath("$.frequency").value("ONE_TIME"))
                .andExpect(jsonPath("$.paymentMethod").value("CARD"));
    }

    @Test
    void getExpenseById_NotFound() throws Exception {
        mockMvc.perform(get("/api/v1/expenses/{id}", 999L)).andExpect(status().isNotFound());
    }

    @Test
    void getAllExpenses_Success() throws Exception {
        Expense expense2 = new Expense();
        expense2.setUser(testUser);
        expense2.setUserId(testUser.getId());
        expense2.setAmount(new BigDecimal("75.50"));
        expense2.setCategory("Entertainment");
        expense2.setDate(LocalDate.of(2025, 9, 18));
        expense2.setDescription("Movie tickets");
        expense2.setFrequency(Expense.Frequency.ONE_TIME);
        expense2.setPaymentMethod(Expense.PaymentMethod.CARD);
        expenseRepository.save(expense2);

        mockMvc.perform(get("/api/v1/expenses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].category", is(oneOf("Groceries", "Entertainment"))))
                .andExpect(jsonPath("$[1].category", is(oneOf("Groceries", "Entertainment"))));
    }

    @Test
    @Transactional
    void updateExpense_Success() throws Exception {
        ExpenseDTO updateDTO = new ExpenseDTO(
                testExpense.getId(),
                testUser.getId(),
                new BigDecimal("200.00"),
                "Groceries",
                LocalDate.of(2025, 9, 15),
                "Updated: Weekly shopping at premium store",
                ExpenseDTO.Frequency.MONTHLY,
                null,
                LocalDate.of(2025, 10, 15),
                ExpenseDTO.PaymentMethod.TRANSFER);

        mockMvc.perform(put("/api/v1/expenses/{id}", testExpense.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testExpense.getId()))
                .andExpect(jsonPath("$.amount").value(200.00))
                .andExpect(jsonPath("$.description").value("Updated: Weekly shopping at premium store"))
                .andExpect(jsonPath("$.frequency").value("MONTHLY"))
                .andExpect(jsonPath("$.paymentMethod").value("TRANSFER"));

        Expense updatedExpense = expenseRepository.findById(testExpense.getId()).orElseThrow();
        assertThat(updatedExpense.getAmount()).isEqualByComparingTo(new BigDecimal("200.00"));
        assertThat(updatedExpense.getFrequency()).isEqualTo(Expense.Frequency.MONTHLY);
    }

    @Test
    void updateExpense_NotFound() throws Exception {
        ExpenseDTO updateDTO = new ExpenseDTO(
                999L,
                testUser.getId(),
                new BigDecimal("100.00"),
                "Test",
                LocalDate.of(2025, 9, 20),
                "Test",
                ExpenseDTO.Frequency.ONE_TIME,
                null,
                null,
                ExpenseDTO.PaymentMethod.CARD);

        mockMvc.perform(put("/api/v1/expenses/{id}", 999L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteExpense_Success() throws Exception {
        mockMvc.perform(delete("/api/v1/expenses/{id}", testExpense.getId())).andExpect(status().isNoContent());

        assertThat(expenseRepository.findById(testExpense.getId())).isEmpty();
        assertThat(expenseRepository.count()).isEqualTo(0);
    }

    @Test
    void deleteExpense_NotFound() throws Exception {
        mockMvc.perform(delete("/api/v1/expenses/{id}", 999L)).andExpect(status().isNotFound());

        assertThat(expenseRepository.count()).isEqualTo(1);
    }
}
