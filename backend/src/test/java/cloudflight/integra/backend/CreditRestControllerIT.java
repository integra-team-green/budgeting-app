package cloudflight.integra.backend;


import cloudflight.integra.backend.repository.CreditRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CreditRestControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CreditRepository repository;

    @BeforeEach
    void cleanDb() {
        repository.deleteAll();
    }

    @Test
    void createCredit_withValidData_returns201() throws Exception {
        String json = """
            {
              "amount": 5000,
              "lender": "Bank Y",
              "startDate": "%s",
              "dueDate": "%s",
              "interestRate": 3.5,
              "description": "Test credit"
            }
            """.formatted(LocalDate.now(), LocalDate.now().plusMonths(3));

        mockMvc.perform(post("/api/credits")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.lender").value("Bank Y"));
    }

    @Test
    void createCredit_withInvalidAmount_returns400() throws Exception {
        String json = """
            {
              "amount": -200,
              "lender": "Bank Y",
              "startDate": "%s",
              "dueDate": "%s",
              "interestRate": 3.5
            }
            """.formatted(LocalDate.now(), LocalDate.now().plusMonths(3));

        mockMvc.perform(post("/api/credits")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("Validation Failed"))
            .andExpect(jsonPath("$.details[0].code").value("amount"))
            .andExpect(jsonPath("$.details[0].message").value(containsString("greater than 0")));
    }

    @Test
    void getCredit_withNonExistingId_returns404() throws Exception {
        mockMvc.perform(get("/api/credits/12345"))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.message").value(containsString("was not found")));
    }
}
