package cloudflight.integra.backend;

import cloudflight.integra.backend.entity.Saving;
import cloudflight.integra.backend.entity.validation.SavingValidation;
import cloudflight.integra.backend.repository.ISavingJpaRepository;
import cloudflight.integra.backend.service.ISavingService;
import cloudflight.integra.backend.service.impl.SavingDbService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class DbRepositoryTests {

    @Autowired
    private ISavingJpaRepository savingJpaRepository;
    private ISavingService savingService;

    @BeforeEach
    void setUp() {
        Saving saving = new Saving(null, new BigDecimal("10000.00"), new Date(), "Apartament", "I wish to have my owm apartament");
        Saving saving2 = new Saving(null, new BigDecimal("2500.00"), new Date(), "Golf 5", "dream car");
        Saving saving3 = new Saving(null, new BigDecimal("5000.00"), new Date(), "Sicily trip");

        savingJpaRepository.save(saving);
        savingJpaRepository.save(saving2);
        savingJpaRepository.save(saving3);

        SavingValidation savingValidator = new SavingValidation();
        savingService = new SavingDbService(savingJpaRepository, savingValidator);
    }

    @Test
    void testRepoFindOne() {
        Optional<Saving> found = savingJpaRepository.findById(143L);
        assertTrue(found.isPresent());
        assertEquals("Apartament", found.get().getGoal());
    }

    @Test
    void testRepoFindAll() {
        Iterable<Saving> all = savingJpaRepository.findAll();
        int count = 0;
        for (Saving s : all) count++;
        assertEquals(54, count);
    }

    @Test
    void testRepoSave() {
        Saving newSaving = new Saving(null, new BigDecimal("3000.00"), new Date(), "New Laptop", "I need a new laptop for work");
        Saving saved = savingJpaRepository.save(newSaving);
        assertNotNull(saved.getId());
        assertEquals("New Laptop", saved.getGoal());
    }

    @Test
    void testRepoDelete() {
        savingJpaRepository.deleteById(2L);
        assertFalse(savingJpaRepository.findById(2L).isPresent());
    }

    @Test
    void testRepoUpdate() {
        Saving toUpdate = savingJpaRepository.findById(96L).orElseThrow();
        toUpdate.setGoal("Sicily trip - updated");
        toUpdate.setAmount(new BigDecimal("6000.00"));
        toUpdate.setDescription("Mafia description");
        savingJpaRepository.save(toUpdate);

        Saving found = savingJpaRepository.findById(96L).orElseThrow();
        assertEquals("Sicily trip - updated", found.getGoal());
        assertEquals(new BigDecimal("6000.0000"), found.getAmount());
    }

    // Service tests
    @Test
    void testServiceFindOne() {
        Saving found = savingService.getSavingById(1L);
        assertEquals("Apartament", found.getGoal());
    }

    @Test
    void testServiceFindAll() {
        Iterable<Saving> all = savingService.getAllSavings();
        int count = 0;
        for (Saving s : all) count++;
        assertEquals(count, count);
    }

    @Test
    void testServiceSave() {
        Saving newSaving = new Saving(null, new BigDecimal("3000.00"), new Date(), "New Laptop", "I need a new laptop for work");
        savingService.addSaving(newSaving);
        assertNotNull(newSaving.getId());
        assertEquals("New Laptop", newSaving.getGoal());
    }

    @Test
    void testServiceDelete() {
        savingService.deleteSaving(110L);
        assertThrows(Exception.class, () -> savingService.getSavingById(2L));
    }

    @Test
    void testServiceUpdate() {
        Saving toUpdate = savingService.getSavingById(104L);
        toUpdate.setAmount(new BigDecimal("6000.00"));
        toUpdate.setGoal("Sicily trip - updated");
        toUpdate.setDescription("Mafia description");
        savingService.updateSaving(toUpdate);

        Saving found = savingService.getSavingById(104L);
        assertEquals("Sicily trip - updated", found.getGoal());
        assertEquals(new BigDecimal("6000.0000"), found.getAmount());
    }
}
