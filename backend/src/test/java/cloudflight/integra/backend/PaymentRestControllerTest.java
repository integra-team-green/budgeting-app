package cloudflight.integra.backend;

import cloudflight.integra.backend.dto.PaymentDTO;
import cloudflight.integra.backend.model.Frequency;
import cloudflight.integra.backend.model.Payment;
import cloudflight.integra.backend.repository.IPaymentRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class PaymentRestControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private IPaymentRepository paymentRepository;
    private Payment p1;
    private Payment p2;
    @BeforeEach
    void setUp() {
        List<Long> paymentsIds = new ArrayList<>();
        paymentRepository.findAll().forEach(p -> paymentsIds.add(p.getId()));
        paymentsIds.forEach(paymentRepository::delete);
        p1=new Payment(1L,"Internet", BigDecimal.valueOf(50), Frequency.MONTHLY,new Date(),true);
        p2=new Payment(2L,"Gym", BigDecimal.valueOf(150), Frequency.MONTHLY,new Date(),true);
        paymentRepository.save(p1);
        paymentRepository.save(p2);

    }
    @Test
    void testGetPaymentById() throws Exception {
        mockMvc.perform(get("/api/v1/payments/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(p1.getId()))
                .andExpect(jsonPath("$.name").value(p1.getName()))
                .andExpect(jsonPath("$.amount").value(p1.getAmount()));


    }
    @Test
    void testGetPaymentByIdNotFound() throws Exception {
        mockMvc.perform(get("/api/v1/payments/100"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreatePayment() throws Exception {
        PaymentDTO paymentDTO=new PaymentDTO();
        paymentDTO.setName("Netflix");
        paymentDTO.setAmount(BigDecimal.valueOf(50));
        paymentDTO.setFrequency(Frequency.MONTHLY);
        paymentDTO.setIsActive(true);
        paymentDTO.setNextDueDate(new Date());
        mockMvc.perform(post("/api/v1/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(paymentDTO)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("Netflix"));
    }
    @Test
    void testUpdatePayment() throws Exception {
        PaymentDTO paymentDTO=new PaymentDTO();
        paymentDTO.setId(1L);
        paymentDTO.setName("HBO");
        paymentDTO.setAmount(BigDecimal.valueOf(50));
        paymentDTO.setFrequency(Frequency.MONTHLY);
        paymentDTO.setIsActive(true);
        paymentDTO.setNextDueDate(new Date());
        mockMvc.perform(put("/api/v1/payments/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(paymentDTO)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("HBO"));

    }
    @Test
    void testDeletePayment() throws Exception {
        mockMvc.perform(delete("/api/v1/payments/2"))
                .andDo(print())
                .andExpect(status().isOk());
        mockMvc.perform(delete("/api/v1/payments/2"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
}
