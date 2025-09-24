package cloudflight.integra.backend.income;

import cloudflight.integra.backend.dto.IncomeDTO;
import cloudflight.integra.backend.entity.Frequency;
import cloudflight.integra.backend.entity.Income;
import cloudflight.integra.backend.entity.User;
import cloudflight.integra.backend.repository.IncomeRepository;
import cloudflight.integra.backend.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class IncomeRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private IncomeRepository repository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    private User user1,user2;

    @BeforeEach
    void resetRepository() {
        repository.deleteAll();
        userRepository.deleteAll();
        user1 = userRepository.save(new User(null, "Alice", "alice@email.com", "123"));
        user2 = userRepository.save(new User(null, "Marc", "marc@yahoo.com", "abcd999"));
    }


    @Test
    void createIncome_withValidData_returns201() throws Exception {
        IncomeDTO dto = new IncomeDTO();
        dto.setAmount(new BigDecimal("500"));
        dto.setSource("Extra Job");
        dto.setDate(new Date());
        dto.setDescription("Bonus");
        dto.setUserId(user1.getId());
        dto.setFrequency(Frequency.MONTHLY);
        dto.setUserId(user1.getId());

        mockMvc.perform(post("/api/v1/incomes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.source").value("Extra Job"))
                .andExpect(jsonPath("$.amount").value(500));
    }

    @Test
    void getIncome_withNonExistingId_returns404() throws Exception {
        mockMvc.perform(get("/api/v1/incomes/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAllIncomes_returnsList() throws Exception {
        repository.save(new Income(null,user1.getId(),new BigDecimal("100"), "Job1", new Date(), "Desc1", Frequency.ONE_TIME,null));
        repository.save(new Income(null, user2.getId(),new BigDecimal("200"), "Job2", new Date(), "Desc2",Frequency.ONE_TIME,null));

        mockMvc.perform(get("/api/v1/incomes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].amount").value(100))
                .andExpect(jsonPath("$[0].source").value("Job1"))
                .andExpect(jsonPath("$[1].source").value("Job2"))
                .andExpect(jsonPath("$[1].amount").value(200));
    }

    @Test
    void updateIncome_existingIncome_returns200() throws Exception {
        resetRepository();
        Income income = new Income(null, user1.getId(),new BigDecimal("100"), "Job1", new Date(), "Desc1",Frequency.ONE_TIME,null);
        Income saved= repository.save(income);

        IncomeDTO dto = new IncomeDTO();
        dto.setUserId(user1.getId());
        dto.setAmount(new BigDecimal("150"));
        dto.setSource("Job Updated");
        dto.setDate(new Date());
        dto.setFrequency(Frequency.ONE_TIME);
        dto.setDescription("Desc Updated");

        mockMvc.perform(put("/api/v1/incomes/"+saved.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.amount").value(150))
                .andExpect(jsonPath("$.source").value("Job Updated"))
                .andExpect(jsonPath("$.userId").value(user1.getId()));
    }

    @Test
    void deleteIncome_existingIncome_returns204() throws Exception {
        resetRepository();
        Income saved =repository.save(new Income(null, user1.getId(),new BigDecimal("100"), "Job1", new Date(), "Desc1",Frequency.ONE_TIME,null));

        mockMvc.perform(delete("/api/v1/incomes/"+saved.getId()))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteIncome_nonExistingIncome_returns404() throws Exception {
        mockMvc.perform(delete("/api/v1/incomes/999"))
                .andExpect(status().isNotFound());
    }
}
