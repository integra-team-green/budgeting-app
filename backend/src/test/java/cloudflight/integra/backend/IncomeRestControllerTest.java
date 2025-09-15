package cloudflight.integra.backend;

import cloudflight.integra.backend.dto.IncomeDTO;
import cloudflight.integra.backend.entity.Income;
import cloudflight.integra.backend.repository.IncomeRepository;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class IncomeRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private IncomeRepository repository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void resetRepository() {
        List<Long> ids = new ArrayList<>();
        repository.getAll().forEach(income -> ids.add(income.getId()));
        ids.forEach(repository::delete);
    }


    @Test
    void createIncome_withValidData_returns201() throws Exception {
        IncomeDTO dto = new IncomeDTO();
        dto.setAmount(new BigDecimal("500"));
        dto.setSource("Extra Job");
        dto.setDate(new Date());
        dto.setDescription("Bonus");

        mockMvc.perform(post("/api/incomes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.source").value("Extra Job"))
                .andExpect(jsonPath("$.amount").value(500));
    }

    @Test
    void getIncome_withNonExistingId_returns404() throws Exception {
        mockMvc.perform(get("/api/incomes/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAllIncomes_returnsList() throws Exception {
        repository.create(new Income(1L, new BigDecimal("100"), "Job1", new Date(), "Desc1"));
        repository.create(new Income(2L, new BigDecimal("200"), "Job2", new Date(), "Desc2"));

        mockMvc.perform(get("/api/incomes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2));
    }

    @Test
    void updateIncome_existingIncome_returns200() throws Exception {
        Income existing = new Income(1L, new BigDecimal("100"), "Job1", new Date(), "Desc1");
        repository.create(existing);

        IncomeDTO dto = new IncomeDTO();
        dto.setAmount(new BigDecimal("150"));
        dto.setSource("Job Updated");
        dto.setDate(new Date());
        dto.setDescription("Desc Updated");

        mockMvc.perform(put("/api/incomes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.amount").value(150))
                .andExpect(jsonPath("$.source").value("Job Updated"));
    }

    @Test
    void deleteIncome_existingIncome_returns204() throws Exception {
        repository.create(new Income(1L, new BigDecimal("100"), "Job1", new Date(), "Desc1"));

        mockMvc.perform(delete("/api/incomes/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteIncome_nonExistingIncome_returns404() throws Exception {
        mockMvc.perform(delete("/api/incomes/999"))
                .andExpect(status().isNotFound());
    }
}
