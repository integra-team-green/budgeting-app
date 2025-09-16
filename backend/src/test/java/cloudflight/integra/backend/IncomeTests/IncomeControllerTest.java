package cloudflight.integra.backend.IncomeTests;

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

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class IncomeControllerTest {

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

        mockMvc.perform(post("/api/v1/incomes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.amount").value(500))
                .andExpect(jsonPath("$.source").value("Extra Job"))
                .andExpect(jsonPath("$.description").value("Bonus"));
    }

    @Test
    void getIncome_withNonExistingId_returns404() throws Exception {
        mockMvc.perform(get("/api/v1/incomes/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAllIncomes_returnsList() throws Exception {
        repository.create(new Income(null, new BigDecimal("100"), "Job1", new Date(), "Desc1"));
        repository.create(new Income(null, new BigDecimal("200"), "Job2", new Date(), "Desc2"));

        mockMvc.perform(get("/api/v1/incomes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].amount", anyOf(is(100), is(200))))
                .andExpect(jsonPath("$[1].amount", anyOf(is(100), is(200))));
    }

    @Test
    void updateIncome_existingIncome_returns200() throws Exception {
        Income income = new Income(null, new BigDecimal("100"), "Job1", new Date(), "Desc1");
        repository.create(income);
        Long id = income.getId();

        IncomeDTO dto = new IncomeDTO();
        dto.setAmount(new BigDecimal("150"));
        dto.setSource("Job Updated");
        dto.setDate(new Date());
        dto.setDescription("Desc Updated");

        mockMvc.perform(put("/api/v1/incomes/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.amount").value(150))
                .andExpect(jsonPath("$.source").value("Job Updated"))
                .andExpect(jsonPath("$.description").value("Desc Updated"));
    }

    @Test
    void deleteIncome_existingIncome_returns204() throws Exception {
        Income income = new Income(null, new BigDecimal("100"), "Job1", new Date(), "Desc1");
        repository.create(income);
        Long id = income.getId();

        mockMvc.perform(delete("/api/v1/incomes/" + id))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteIncome_nonExistingIncome_returns404() throws Exception {
        mockMvc.perform(delete("/api/v1/incomes/999"))
                .andExpect(status().isNotFound());
    }
}
