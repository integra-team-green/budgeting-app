package cloudflight.integra.backend.payment;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import cloudflight.integra.backend.dto.ExpenseDTO;
import cloudflight.integra.backend.dto.PaymentDTO;
import cloudflight.integra.backend.dto.auth.AuthenticationRequest;
import cloudflight.integra.backend.dto.auth.AuthenticationResponse;
import cloudflight.integra.backend.dto.auth.RegisterRequest;
import cloudflight.integra.backend.entity.Payment;
import cloudflight.integra.backend.entity.User;
import cloudflight.integra.backend.service.ExpenseService;
import cloudflight.integra.backend.service.PaymentService;
import cloudflight.integra.backend.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

// varianta buna
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class PaymentRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private ExpenseService expenseService;

    @Autowired
    private UserService userService;

    private User testUser;
    private ExpenseDTO expense1;
    private ExpenseDTO expense2;
    private PaymentDTO payment1;
    private PaymentDTO payment2;
    private String testToken;

    @BeforeEach
    void setUp() throws Exception {

        // Creează și salvează un user
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setName("Test User");
        String uniqueEmail = "test+" + UUID.randomUUID() + "@example.com";
        registerRequest.setEmail(uniqueEmail);
        registerRequest.setPassword("password123");

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isOk());

        AuthenticationRequest loginRequest = new AuthenticationRequest();
        loginRequest.setEmail(uniqueEmail);
        loginRequest.setPassword("password123");

        MvcResult result = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        testToken =
                objectMapper.readValue(response, AuthenticationResponse.class).getToken();

        testUser = userService.getUserByEmail(uniqueEmail);
        // Creează Expense 1 asociat userului
        expense1 = new ExpenseDTO();
        expense1.setCategory("Rent");
        expense1.setAmount(BigDecimal.valueOf(1000));
        expense1.setDate(LocalDate.now());
        expense1.setFrequency(ExpenseDTO.Frequency.MONTHLY);
        expense1.setPaymentMethod(ExpenseDTO.PaymentMethod.CARD);
        expense1.setUserId(testUser.getId());
        expense1 = expenseService.createExpense(expense1);

        // Creează Payment 1 pentru Expense 1
        payment1 = new PaymentDTO();
        payment1.setName("September Rent");
        payment1.setAmount(BigDecimal.valueOf(500));
        payment1.setStatus(Payment.StatusEnum.PENDING);
        payment1.setPaymentDate(LocalDate.of(2025, 9, 22));
        payment1.setExpenseId(expense1.getId());
        payment1 = paymentService.addPayment(payment1);

        // Creează Expense 2 asociat userului
        expense2 = new ExpenseDTO();
        expense2.setCategory("Utilities");
        expense2.setAmount(BigDecimal.valueOf(200));
        expense2.setDate(LocalDate.now());
        expense2.setFrequency(ExpenseDTO.Frequency.MONTHLY);
        expense2.setPaymentMethod(ExpenseDTO.PaymentMethod.TRANSFER);
        expense2.setUserId(testUser.getId());
        expense2 = expenseService.createExpense(expense2);

        // Creează Payment 2 pentru Expense 2
        payment2 = new PaymentDTO();
        payment2.setName("October Utilities");
        payment2.setAmount(BigDecimal.valueOf(200));
        payment2.setStatus(Payment.StatusEnum.PAID);
        payment2.setPaymentDate(LocalDate.of(2025, 10, 1));
        payment2.setExpenseId(expense2.getId());
        payment2 = paymentService.addPayment(payment2);
    }

    @Test
    void testGetPaymentById() throws Exception {
        mockMvc.perform(get("/api/v1/payments/" + payment1.getId()).header("Authorization", "Bearer " + testToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(payment1.getId()))
                .andExpect(jsonPath("$.name").value("September Rent"))
                .andExpect(jsonPath("$.amount").value(500))
                .andExpect(jsonPath("$.status").value("PENDING"))
                .andExpect(jsonPath("$.expenseId").value(expense1.getId()));
    }

    @Test
    void testCreatePayment() throws Exception {
        ExpenseDTO newExpense = new ExpenseDTO();
        newExpense.setCategory("Netflix");
        newExpense.setAmount(BigDecimal.valueOf(50));
        newExpense.setDate(LocalDate.now());
        newExpense.setFrequency(ExpenseDTO.Frequency.MONTHLY);
        newExpense.setPaymentMethod(ExpenseDTO.PaymentMethod.CARD);
        newExpense.setUserId(testUser.getId());
        newExpense = expenseService.createExpense(newExpense);

        PaymentDTO dto = new PaymentDTO();
        dto.setName("Netflix Payment");
        dto.setAmount(BigDecimal.valueOf(50));
        dto.setPaymentDate(LocalDate.of(2025, 11, 1));
        dto.setStatus(Payment.StatusEnum.PENDING);
        dto.setExpenseId(newExpense.getId());

        mockMvc.perform(post("/api/v1/payments")
                        .header("Authorization", "Bearer " + testToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("Netflix Payment"))
                .andExpect(jsonPath("$.status").value("PENDING"))
                .andExpect(jsonPath("$.expenseId").value(newExpense.getId()));
    }

    @Test
    void testUpdatePayment() throws Exception {
        PaymentDTO dto = new PaymentDTO();
        dto.setId(payment1.getId());
        dto.setName("September Rent Updated");
        dto.setAmount(BigDecimal.valueOf(550));
        dto.setPaymentDate(LocalDate.of(2025, 9, 30));
        dto.setStatus(Payment.StatusEnum.PAID);
        dto.setExpenseId(expense1.getId());

        mockMvc.perform(put("/api/v1/payments/" + payment1.getId())
                        .header("Authorization", "Bearer " + testToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("September Rent Updated"))
                .andExpect(jsonPath("$.amount").value(550))
                .andExpect(jsonPath("$.status").value("PAID"))
                .andExpect(jsonPath("$.expenseId").value(expense1.getId()));
    }

    @Test
    void testDeletePayment() throws Exception {
        mockMvc.perform(delete("/api/v1/payments/" + payment2.getId()).header("Authorization", "Bearer " + testToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(payment2.getId()))
                .andExpect(jsonPath("$.name").value("October Utilities"));
    }

    @Test
    void testCreatePaymentWithInvalidData() throws Exception {
        PaymentDTO invalidDto = new PaymentDTO();
        invalidDto.setName("");
        invalidDto.setAmount(null);
        invalidDto.setPaymentDate(null);
        invalidDto.setStatus(null);
        invalidDto.setExpenseId(null);

        mockMvc.perform(post("/api/v1/payments")
                        .header("Authorization", "Bearer " + testToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest()); // se așteaptă cod 400
    }

    @Test
    void testUpdatePaymentWithInvalidData() throws Exception {
        PaymentDTO invalidDto = new PaymentDTO();
        invalidDto.setId(payment1.getId());
        invalidDto.setName("");
        invalidDto.setAmount(BigDecimal.valueOf(-100));
        invalidDto.setPaymentDate(LocalDate.of(2025, 1, 1));
        invalidDto.setStatus(Payment.StatusEnum.PENDING);
        invalidDto.setExpenseId(expense1.getId());

        mockMvc.perform(put("/api/v1/payments/" + payment1.getId())
                        .header("Authorization", "Bearer " + testToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest()); // se așteaptă cod 400
    }
}
