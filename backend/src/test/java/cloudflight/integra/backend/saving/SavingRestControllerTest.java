package cloudflight.integra.backend.saving;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import cloudflight.integra.backend.dto.SavingDTO;
import cloudflight.integra.backend.entity.Saving;
import cloudflight.integra.backend.entity.User;
import cloudflight.integra.backend.repository.SavingRepository;
import cloudflight.integra.backend.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.Date;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class SavingRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SavingRepository savingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private User testUser;

    @BeforeEach
    void setUp() {
        savingRepository.deleteAll();
        userRepository.deleteAll();

        testUser = new User(null, "Alice", "alice@email.com", "1234");
        testUser = userRepository.save(testUser);

        Saving s1 = new Saving(
                testUser, new BigDecimal("10000.00"), new Date(), "Apartament", "I wish to have my own apartament");
        Saving s2 = new Saving(testUser, new BigDecimal("2500.00"), new Date(), "Golf 5", "dream car");
        Saving s3 = new Saving(testUser, new BigDecimal("5000.00"), new Date(), "Sicily trip");
        savingRepository.save(s1);
        savingRepository.save(s2);
        savingRepository.save(s3);
    }

    @Test
    void testGetById() throws Exception {
        Saving saving = savingRepository.findAll().iterator().next();
        mockMvc.perform(get("/api/v1/savings/" + saving.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(saving.getId()))
                .andExpect(jsonPath("$.amount").value(saving.getAmount().doubleValue()))
                .andExpect(jsonPath("$.goal").value(saving.getGoal()));
    }

    @Test
    void testGetById_NotFound() throws Exception {
        mockMvc.perform(get("/api/v1/savings/999999"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("not found")));
    }

    @Test
    void testGetAll() throws Exception {
        mockMvc.perform(get("/api/v1/savings")).andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(3)));
    }

    @Test
    void testAddSaving_Success() throws Exception {
        SavingDTO dto = new SavingDTO();
        dto.setUserId(testUser.getId());
        dto.setAmount(BigDecimal.valueOf(3000));
        dto.setDate(new Date());
        dto.setGoal("New Laptop");
        dto.setDescription("For work");

        mockMvc.perform(post("/api/v1/savings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.amount").value(3000.0))
                .andExpect(jsonPath("$.goal").value("New Laptop"));
    }

    @Test
    void testAddSaving_BadRequest() throws Exception {
        SavingDTO badDto = new SavingDTO();
        badDto.setUserId(testUser.getId());
        badDto.setAmount(BigDecimal.valueOf(-1500));
        badDto.setDate(new Date());
        badDto.setGoal("");
        badDto.setDescription("Invalid");

        mockMvc.perform(post("/api/v1/savings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(badDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("Validation Failed"))
                .andExpect(jsonPath("$.details", hasItem("Amount must be non-negative.")))
                .andExpect(
                        jsonPath("$.details", hasItem("Please provide a goal, need to know what you are saving for!")));
    }

    @Test
    void testDeleteSaving() throws Exception {
        Saving saving = savingRepository.findAll().iterator().next();

        mockMvc.perform(delete("/api/v1/savings/" + saving.getId())).andExpect(status().isNoContent());

        mockMvc.perform(get("/api/v1/savings/" + saving.getId())).andExpect(status().isNotFound());
    }

    @Test
    void testUpdateSaving_Success() throws Exception {
        Saving existing = savingRepository.findAll().iterator().next();
        SavingDTO updateDto = new SavingDTO();
        updateDto.setId(existing.getId());
        updateDto.setUserId(testUser.getId());
        updateDto.setAmount(BigDecimal.valueOf(6000));
        updateDto.setDate(new Date());
        updateDto.setGoal("Sicily trip - updated");
        updateDto.setDescription("Mafia description");

        mockMvc.perform(put("/api/v1/savings/" + existing.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(existing.getId()))
                .andExpect(jsonPath("$.goal").value("Sicily trip - updated"))
                .andExpect(jsonPath("$.description").value("Mafia description"));
    }

    @Test
    void testUpdateSaving_BadRequest() throws Exception {
        Saving existing = savingRepository.findAll().iterator().next();
        SavingDTO badDto = new SavingDTO();
        badDto.setId(existing.getId());
        badDto.setUserId(testUser.getId());
        badDto.setAmount(BigDecimal.valueOf(-6000));
        badDto.setDate(new Date());
        badDto.setGoal("");
        badDto.setDescription("Invalid");

        mockMvc.perform(put("/api/v1/savings/" + existing.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(badDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("Validation Failed"))
                .andExpect(jsonPath("$.details", hasItem("Amount must be non-negative.")))
                .andExpect(
                        jsonPath("$.details", hasItem("Please provide a goal, need to know what you are saving for!")));
    }
}
