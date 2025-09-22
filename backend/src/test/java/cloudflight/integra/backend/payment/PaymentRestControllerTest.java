package cloudflight.integra.backend.payment;

import cloudflight.integra.backend.dto.ExpenseDTO;
import cloudflight.integra.backend.dto.PaymentDTO;
import cloudflight.integra.backend.entity.Payment;
import cloudflight.integra.backend.service.ExpenseService;
import cloudflight.integra.backend.service.PaymentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class PaymentRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private ExpenseService expenseService;

    private ExpenseDTO expense;
    private PaymentDTO payment1;
    private PaymentDTO payment2;

    @BeforeEach
    void setUp() {
        expense = new ExpenseDTO();
        expense.setCategory("Rent");
        expense.setAmount(BigDecimal.valueOf(1000));
        expense.setDate(LocalDate.now());
        expense = expenseService.createExpense(expense);


        payment1 = new PaymentDTO();
        payment1.setName("September Rent");
        payment1.setAmount(BigDecimal.valueOf(500));
        payment1.setStatus(Payment.StatusEnum.PENDING);
        payment1.setPaymentDate(LocalDate.of(2025, 9, 22));
        payment1.setExpense(expense);
        payment1 = paymentService.addPayment(payment1);


        payment2 = new PaymentDTO();
        payment2.setName("October Rent");
        payment2.setAmount(BigDecimal.valueOf(600));
        payment2.setStatus(Payment.StatusEnum.PAID);
        payment2.setPaymentDate(LocalDate.of(2025, 10, 1));
        payment2.setExpense(expense);
        payment2 = paymentService.addPayment(payment2);
    }

    @Test
    void testGetPaymentById() throws Exception {
        mockMvc.perform(get("/api/v1/payments/" + payment1.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(payment1.getId()))
                .andExpect(jsonPath("$.name").value("September Rent"))
                .andExpect(jsonPath("$.amount").value(500))
                .andExpect(jsonPath("$.status").value("PENDING"))
                .andExpect(jsonPath("$.expense.category").value("Rent"));
    }

    @Test
    void testGetPaymentByIdNotFound() throws Exception {
        mockMvc.perform(get("/api/v1/payments/9999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreatePayment() throws Exception {
        PaymentDTO dto = new PaymentDTO();
        dto.setName("Netflix");
        dto.setAmount(BigDecimal.valueOf(50));
        dto.setPaymentDate(LocalDate.of(2025, 11, 1));
        dto.setStatus(Payment.StatusEnum.PENDING);
        dto.setExpense(expense);

        mockMvc.perform(post("/api/v1/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("Netflix"))
                .andExpect(jsonPath("$.status").value("PENDING"))
                .andExpect(jsonPath("$.expense.category").value("Rent"));
    }

    @Test
    void testUpdatePayment() throws Exception {
        PaymentDTO dto = new PaymentDTO();
        dto.setId(payment1.getId());
        dto.setName("HBO");
        dto.setAmount(BigDecimal.valueOf(550));
        dto.setPaymentDate(LocalDate.of(2025, 9, 30));
        dto.setStatus(Payment.StatusEnum.PAID);
        dto.setExpense(expense); // ✅ ExpenseDTO

        mockMvc.perform(put("/api/v1/payments/" + payment1.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("HBO"))
                .andExpect(jsonPath("$.amount").value(550))
                .andExpect(jsonPath("$.status").value("PAID"))
                .andExpect(jsonPath("$.expense.category").value("Rent"));
    }

    @Test
    void testDeletePayment() throws Exception {
        mockMvc.perform(delete("/api/v1/payments/" + payment2.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(payment2.getId()))
                .andExpect(jsonPath("$.name").value("October Rent"));

        mockMvc.perform(delete("/api/v1/payments/" + payment2.getId()))
                .andExpect(status().isNotFound());
    }
}

