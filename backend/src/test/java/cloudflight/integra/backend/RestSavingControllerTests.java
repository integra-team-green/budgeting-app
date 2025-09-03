package cloudflight.integra.backend;

import cloudflight.integra.backend.controller.RestSavingController;
import cloudflight.integra.backend.entity.Saving;
import cloudflight.integra.backend.entity.validation.SavingValidation;
import cloudflight.integra.backend.repository.ISavingRepository;
import cloudflight.integra.backend.repository.implementation.SavingRepository;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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

        ISavingRepository<Long, Saving> TESTsavingRepository = new SavingRepository();

        TESTsavingRepository.save(saving);
        TESTsavingRepository.save(saving2);
        TESTsavingRepository.save(saving3);

        SavingValidation TESTsavingValidator = new SavingValidation();
        TESTsavingService = new SavingService(TESTsavingRepository, TESTsavingValidator);

        RestSavingController TESTrestSavingController = new RestSavingController(TESTsavingService);

        TESTmockMvc = MockMvcBuilders.standaloneSetup(TESTrestSavingController)
                .build();

    }


    @Test
    void testGetById() throws Exception {
        TESTmockMvc.perform(get("/savings/4"))
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
            TESTmockMvc.perform(get("/savings/999"))
                    .andDo(print())
                    .andExpect(status().isNotFound());

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }


    @Test
    void testGetAll() throws Exception {
        TESTmockMvc.perform(get("/savings"))
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


        TESTmockMvc.perform(post("/savings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(saving)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(5L))
                .andExpect(jsonPath("$.amount").value(3000.00))
                .andExpect(jsonPath("$.date").value(date))
                .andExpect(jsonPath("$.goal").value("New Laptop"));

        System.out.println(saving);
    }

    @Test
    void testAdd_Bad() {
        Saving badSaving = new Saving(10L, new BigDecimal("-1500.00"), new Date(), "", "Wrong description");

        try {
            TESTmockMvc.perform(post("/savings")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(badSaving)))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    @Test
    void testDelete() throws Exception {
        Iterable<Saving> all = TESTsavingService.getAllSavings();

        for (Saving s : all) {
            System.out.println(s.toString());
        }

        TESTmockMvc.perform(get("/savings"))
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

        TESTmockMvc.perform(delete("/savings/2"))
                .andDo(print())
                .andExpect(status().isOk());

        System.out.println("after delete: ");

        int count = 0;
        for (Saving s : all) {
            System.out.println(s.toString());
            count++;
        }

        assertEquals(2, count, "Should be 2 savings after delete");

        TESTmockMvc.perform(get("/savings"))
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

        for (Saving s : all) {
            System.out.println(s.toString());
        }

        TESTmockMvc.perform(get("/savings"))
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


        TESTmockMvc.perform(put("/savings/4")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new Saving(4L, new BigDecimal("6000.00"), new Date(), "Sicily trip - updated", "Mafia description"))))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(4L))
                .andExpect(jsonPath("$.amount").value(6000.00))
                .andExpect(jsonPath("$.goal").value("Sicily trip - updated"))
                .andExpect(jsonPath("$.description").value("Mafia description"));

        System.out.println("after update: ");

        for (Saving s : all) {
            System.out.println(s.toString());
        }

        TESTmockMvc.perform(get("/savings"))
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

        try {
            TESTmockMvc.perform(put("/savings/4")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(new Saving(4L, new BigDecimal("-6000.00"), new Date(), "", "Mafia description"))))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
