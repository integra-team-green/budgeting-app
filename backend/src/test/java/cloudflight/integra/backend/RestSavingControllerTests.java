package cloudflight.integra.backend;

import cloudflight.integra.backend.controller.SavingController;
import cloudflight.integra.backend.dto.SavingDTO;
import cloudflight.integra.backend.entity.Saving;
import cloudflight.integra.backend.entity.validation.SavingValidation;
import cloudflight.integra.backend.mapper.SavingMapper;
import cloudflight.integra.backend.repository.ISavingRepository;
import cloudflight.integra.backend.repository.implementation.InMemorySavingRepository;
import cloudflight.integra.backend.service.ISavingService;
import cloudflight.integra.backend.service.impl.SavingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.Date;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class RestSavingControllerTests {
    private ISavingService TESTsavingService;

    private MockMvc TESTmockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        Saving saving = new Saving(1L, new BigDecimal("10000.00"), new Date(), "Apartament", "I wish to have my owm apartament");
        Saving saving2 = new Saving(2L, new BigDecimal("2500.00"), new Date(), "Golf 5", "dream car");
        Saving saving3 = new Saving(4L, new BigDecimal("5000.00"), new Date(), "Sicily trip");

        ISavingRepository<Long, Saving> TESTsavingRepository = new InMemorySavingRepository();

        TESTsavingRepository.save(saving);
        TESTsavingRepository.save(saving2);
        TESTsavingRepository.save(saving3);

        SavingValidation TESTsavingValidator = new SavingValidation();
        TESTsavingService = new SavingService(TESTsavingRepository, TESTsavingValidator);

        SavingController TESTrestSavingController = new SavingController(TESTsavingService);

        TESTmockMvc = MockMvcBuilders.standaloneSetup(TESTrestSavingController)
            .build();

    }


    @Test
    void testGetById() throws Exception {
        TESTmockMvc.perform(get("/api/v1/savings/4"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(4L))
            .andExpect(jsonPath("$.date").exists())
            .andExpect(jsonPath("$.amount").value(5000.0))
            .andExpect(jsonPath("$.goal").value("Sicily trip"));
    }

    @Test
    void testGetById_NotFound() {
        try {
            TESTmockMvc.perform(get("/api/v1/savings/999"))
                .andDo(print())
                .andExpect(status().isNotFound());

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }


    @Test
    void testGetAll() throws Exception {
        TESTmockMvc.perform(get("/api/v1/savings"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(3)))
            .andExpect(jsonPath("$[0].id").exists())
            .andExpect(jsonPath("$[0].date").exists())
            .andExpect(jsonPath("$[0].amount").exists())
            .andExpect(jsonPath("$[0].goal").exists())
            .andExpect(jsonPath("$[1].id").exists())
            .andExpect(jsonPath("$[1].date").exists())
            .andExpect(jsonPath("$[1].amount").exists())
            .andExpect(jsonPath("$[1].goal").exists())
            .andExpect(jsonPath("$[2].id").exists())
            .andExpect(jsonPath("$[2].date").exists())
            .andExpect(jsonPath("$[2].amount").exists())
            .andExpect(jsonPath("$[2].goal").exists());
    }


    @Test
    void testAdd() throws Exception {
        Date date = new Date();

        Saving saving = new Saving(5L, new BigDecimal("3000.00"), new Date(), "New Laptop");
        SavingDTO savingDTO = SavingMapper.toDTO(saving);


        TESTmockMvc.perform(post("/api/v1/savings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(savingDTO)))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(5L))
            .andExpect(jsonPath("$.amount").value(3000.00))
            .andExpect(jsonPath("$.date").value(date))
            .andExpect(jsonPath("$.goal").value("New Laptop"));

        System.out.println(savingDTO);
    }

    @Test
    void testAdd_Bad() {
        Saving badSaving = new Saving(10L, new BigDecimal("-1500.00"), new Date(), "", "Wrong description");
        SavingDTO savingDTO = SavingMapper.toDTO(badSaving);

        try {
            TESTmockMvc.perform(post("/api/v1/savings")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(savingDTO)))
                .andDo(print())
                .andExpect(status().isBadRequest());
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    @Test
    void testDelete() throws Exception {
        Iterable<Saving> all = TESTsavingService.getAllSavings();
        Iterable<SavingDTO> savingDTOS = SavingMapper.toDtoList(all);

        for (SavingDTO s : savingDTOS) {
            System.out.println(s.toString());
        }

        TESTmockMvc.perform(get("/api/v1/savings"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(3)))
            .andExpect(jsonPath("$[0].id").exists())
            .andExpect(jsonPath("$[0].date").exists())
            .andExpect(jsonPath("$[0].amount").exists())
            .andExpect(jsonPath("$[0].goal").exists())
            .andExpect(jsonPath("$[1].id").exists())
            .andExpect(jsonPath("$[1].date").exists())
            .andExpect(jsonPath("$[1].amount").exists())
            .andExpect(jsonPath("$[1].goal").exists())
            .andExpect(jsonPath("$[2].id").exists())
            .andExpect(jsonPath("$[2].date").exists())
            .andExpect(jsonPath("$[2].amount").exists())
            .andExpect(jsonPath("$[2].goal").exists());

        TESTmockMvc.perform(delete("/api/v1/savings/2"))
            .andDo(print())
            .andExpect(status().isNoContent());

        System.out.println("after delete: ");

        Iterable<Saving> updatedAll = TESTsavingService.getAllSavings();
        Iterable<SavingDTO> updatedSavingDTOS = SavingMapper.toDtoList(updatedAll);

        int count = 0;
        for (SavingDTO s : updatedSavingDTOS) {
            System.out.println(s.toString());
            count++;
        }

        assertEquals(2, count, "Should be 2 savings after delete");

        TESTmockMvc.perform(get("/api/v1/savings"))
            .andDo(print())
            .andExpect(jsonPath("$", hasSize(2)))
            .andExpect(jsonPath("$[0].id").exists())
            .andExpect(jsonPath("$[0].date").exists())
            .andExpect(jsonPath("$[0].amount").exists())
            .andExpect(jsonPath("$[0].goal").exists())
            .andExpect(jsonPath("$[1].id").exists())
            .andExpect(jsonPath("$[1].date").exists())
            .andExpect(jsonPath("$[1].amount").exists())
            .andExpect(jsonPath("$[1].goal").exists());

    }

    @Test
    void testUpdate() throws Exception {
        Iterable<Saving> all = TESTsavingService.getAllSavings();
        Iterable<SavingDTO> savingDTOS = SavingMapper.toDtoList(all);

        for (SavingDTO s : savingDTOS) {
            System.out.println(s.toString());
        }

        TESTmockMvc.perform(get("/api/v1/savings"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(3)))
            .andExpect(jsonPath("$[0].id").exists())
            .andExpect(jsonPath("$[0].date").exists())
            .andExpect(jsonPath("$[0].amount").exists())
            .andExpect(jsonPath("$[0].goal").exists())
            .andExpect(jsonPath("$[1].id").exists())
            .andExpect(jsonPath("$[1].date").exists())
            .andExpect(jsonPath("$[1].amount").exists())
            .andExpect(jsonPath("$[1].goal").exists())
            .andExpect(jsonPath("$[2].id").exists())
            .andExpect(jsonPath("$[2].date").exists())
            .andExpect(jsonPath("$[2].amount").exists())
            .andExpect(jsonPath("$[2].goal").exists());


        Saving saving = new Saving(4L, new BigDecimal("6000.00"), new Date(), "Sicily trip - updated", "Mafia description");
        SavingDTO savingDTO = SavingMapper.toDTO(saving);

        TESTmockMvc.perform(put("/api/v1/savings/4")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(savingDTO)))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(4L))
            .andExpect(jsonPath("$.amount").value(6000.00))
            .andExpect(jsonPath("$.goal").value("Sicily trip - updated"))
            .andExpect(jsonPath("$.description").value("Mafia description"));

        System.out.println("after update: ");

        for (SavingDTO s : savingDTOS) {
            System.out.println(s.toString());
        }

        TESTmockMvc.perform(get("/api/v1/savings"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(3)))
            .andExpect(jsonPath("$[0].id").exists())
            .andExpect(jsonPath("$[0].date").exists())
            .andExpect(jsonPath("$[0].amount").exists())
            .andExpect(jsonPath("$[0].goal").exists())
            .andExpect(jsonPath("$[1].id").exists())
            .andExpect(jsonPath("$[1].date").exists())
            .andExpect(jsonPath("$[1].amount").exists())
            .andExpect(jsonPath("$[1].goal").exists())
            .andExpect(jsonPath("$[2].id").exists())
            .andExpect(jsonPath("$[2].date").exists())
            .andExpect(jsonPath("$[2].amount").exists())
            .andExpect(jsonPath("$[2].goal").exists());
    }

    @Test
    void testUpdate_Bad() {
        Saving saving = new Saving(4L, new BigDecimal("-6000.00"), new Date(), "", "Mafia description");
        SavingDTO savingDTO = SavingMapper.toDTO(saving);

        try {
            TESTmockMvc.perform(put("/api/v1/savings/4")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(savingDTO)))
                .andDo(print())
                .andExpect(status().isBadRequest());
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
