package cloudflight.integra.backend.payment;

import cloudflight.integra.backend.dto.PaymentDTO;
import cloudflight.integra.backend.entity.Expense;
import cloudflight.integra.backend.entity.Payment;
import cloudflight.integra.backend.entity.Payment.Status;
import cloudflight.integra.backend.repository.ExpenseRepository;
import cloudflight.integra.backend.repository.PaymentRepository;
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
    private PaymentRepository paymentRepository;

    @Autowired
    private ExpenseRepository expenseRepository;

    private Expense expense;
    private Payment payment1;
    private Payment payment2;

    @BeforeEach
    void setUp() {
        paymentRepository.deleteAll();
        expenseRepository.deleteAll();

        expense = new Expense();
        expense.setCategory("Rent");
        expense.setAmount(BigDecimal.valueOf(1000));
        expense.setDate(LocalDate.now());
        expense = expenseRepository.save(expense);

        payment1 = new Payment();
        payment1.setName("September Rent");
        payment1.setAmount(BigDecimal.valueOf(500));
        payment1.setStatus(Status.PENDING);
        payment1.setPaymentDate(LocalDate.of(2025, 9, 22));
        payment1.setExpense(expense);
        payment1 = paymentRepository.save(payment1);

        payment2 = new Payment();
        payment2.setName("October Rent");
        payment2.setAmount(BigDecimal.valueOf(600));
        payment2.setStatus(Status.PAID);
        payment2.setPaymentDate(LocalDate.of(2025, 10, 1));
        payment2.setExpense(expense);
        payment2 = paymentRepository.save(payment2);
    }

    @Test
    void testGetPaymentById() throws Exception {
        mockMvc.perform(get("/api/v1/payments/" + payment1.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(payment1.getId()))
                .andExpect(jsonPath("$.name").value("September Rent"))
                .andExpect(jsonPath("$.amount").value(500))
                .andExpect(jsonPath("$.status").value("PENDING"));
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
        dto.setStatus("PENDING");
        dto.setExpenseId(expense.getId());

        mockMvc.perform(post("/api/v1/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("Netflix"))
                .andExpect(jsonPath("$.status").value("PENDING"));
    }

    @Test
    void testUpdatePayment() throws Exception {
        PaymentDTO dto = new PaymentDTO();
        dto.setId(payment1.getId());
        dto.setName("HBO");
        dto.setAmount(BigDecimal.valueOf(550));
        dto.setPaymentDate(LocalDate.of(2025, 9, 30));
        dto.setStatus("PAID");
        dto.setExpenseId(expense.getId());

        mockMvc.perform(put("/api/v1/payments/" + payment1.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("HBO"))
                .andExpect(jsonPath("$.amount").value(550))
                .andExpect(jsonPath("$.status").value("PAID"));
    }

    @Test
    void testDeletePayment() throws Exception {
        mockMvc.perform(delete("/api/v1/payments/" + payment2.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(delete("/api/v1/payments/" + payment2.getId()))
                .andExpect(status().isNotFound());
    }
}
