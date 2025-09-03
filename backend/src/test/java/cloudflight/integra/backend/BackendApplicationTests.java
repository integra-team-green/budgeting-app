package cloudflight.integra.backend;

import cloudflight.integra.backend.entity.Saving;
import cloudflight.integra.backend.entity.validation.SavingValidation;
import cloudflight.integra.backend.entity.validation.ValidationException;
import cloudflight.integra.backend.repository.ISavingRepository;
import cloudflight.integra.backend.repository.implementation.InMemorySavingRepository;
import cloudflight.integra.backend.service.ISavingService;
import cloudflight.integra.backend.service.impl.SavingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

@SpringBootTest
class BackendApplicationTests {
	private ISavingRepository<Long, Saving> TESTsavingRepository;
	private ISavingService TESTsavingService;

    @BeforeEach
	void setUp() {
		Saving saving = new Saving(null, new BigDecimal("10000.00"), new Date(), "Apartament", "I wish to have my owm apartament");
		Saving saving2 = new Saving(null, new BigDecimal("2500.00"), new Date(), "Golf 5", "dream car");
		Saving saving3 = new Saving(null, new BigDecimal("5000.00"), new Date(), "Sicily trip");

		TESTsavingRepository = new InMemorySavingRepository();

		TESTsavingRepository.save(saving);
		TESTsavingRepository.save(saving2);
		TESTsavingRepository.save(saving3);

		SavingValidation TESTsavingValidator = new SavingValidation();
		TESTsavingService = new SavingService(TESTsavingRepository, TESTsavingValidator);

    }

	/// Repo Tests
	@Test
	void testSavingRepoFindOne() {
		Saving found = TESTsavingRepository.findOne(1L);

		assert (found.getId().equals(1L));
		assert (found.getGoal().equals("Apartament"));

		System.out.println(found);
		System.out.println("testSavingRepoFindOne PASSED\n");
	}

	@Test
	void tetsSavingRepoFindAll() {
		Iterable<Saving> all = TESTsavingRepository.findAll();

		int count = 0;
		for (Saving s : all) {
			System.out.println(s.toString());
			count++;
		}

		assert (count == 3);

		System.out.println("tetsSavingRepoFindAll PASSED\n");
	}

	@Test
	void testSavingRepoSave() {
		Saving newSaving = new Saving(null, new BigDecimal("3000.00"), new Date(), "New Laptop", "I need a new laptop for work");

		TESTsavingRepository.save(newSaving);

		Saving found = TESTsavingRepository.findOne(4L);

		assert (found.getId().equals(4L));
		assert (found.getGoal().equals("New Laptop"));

		System.out.println(found);
		System.out.println("testSavingRepoSave PASSED\n");
	}

	@Test
	void testSavingRepoDelete() {

		Iterable<Saving> all = TESTsavingRepository.findAll();

		for (Saving s : all) {
			System.out.println(s.toString());
		}

		TESTsavingRepository.delete(2L);

		System.out.println("after delete:");

		int count = 0;
		for (Saving s : all) {
			System.out.println(s.toString());
			count++;
		}

		assert (count == 2);

		System.out.println("testSavingRepoDelete PASSED\n");
	}

	@Test
	void testSavingRepoUpdate() {
		Saving toUpdate = TESTsavingRepository.findOne(3L);

		System.out.println(toUpdate.toString());

		toUpdate.setGoal("Sicily trip - updated");
		toUpdate.setAmount(new BigDecimal("6000.00"));
		toUpdate.setDescription("Mafia description");

		TESTsavingRepository.update(toUpdate);


		Saving found = TESTsavingRepository.findOne(3L);

		System.out.println("after update:");
		System.out.println(found.toString());

		assert (found.getGoal().equals("Sicily trip - updated"));
		assert (found.getAmount().equals(new BigDecimal("6000.00")));

		System.out.println("testSavingRepoUpdate PASSED\n");
	}


	///  Service Tests

	@Test
	void testSavingServiceFindOne() {
		Saving found = TESTsavingService.getSavingById(1L);

		assert (found.getId().equals(1L));

		assertEquals("Apartament", found.getGoal(), "Goal should be Apartament");

		System.out.println(found);
		System.out.println("testSavingServiceFindOne PASSED\n");
	}

	@Test
	void tetsSavingServiceFindAll() {
		Iterable<Saving> all = TESTsavingService.getAllSavings();

		int count = 0;
		for (Saving s : all) {
			System.out.println(s.toString());
			count++;
		}

		assertEquals(3, count, "There should be 3 savings in the service");

		System.out.println("tetsSavingServiceFindAll PASSED\n");
	}

	@Test
	void testSavingServiceSave() {
		Saving newSaving = new Saving(null, new BigDecimal("3000.00"), new Date(), "New Laptop", "I need a new laptop for work");

		TESTsavingService.addSaving(newSaving);

		Saving found = TESTsavingService.getSavingById(4L);

		assert (found.getId().equals(4L));
//		assert (found.getGoal().equals("New Laptop"));

		assertEquals("New Laptop", found.getGoal(), "Goal should be New Laptop");

		System.out.println(found);
		System.out.println("testSavingServiceSave PASSED\n");
	}

	@Test
	void testSavingService_BAD_Save() {

		Calendar calendar = Calendar.getInstance();

//		System.out.println(calendar.get(Calendar.DAY_OF_YEAR));

		calendar.add(Calendar.DAY_OF_YEAR, 30);

//		System.out.println(calendar.get(Calendar.DAY_OF_YEAR));

		Date futureDate = calendar.getTime();

		Saving badSaving = new Saving(null, new BigDecimal("-1500.00"), futureDate, "", "Wrong description");
//		Saving badSaving = new Saving(null, new BigDecimal("-1500.00"), new Date(), "TestGoal", "Wrong description");
		Saving goodSaving = new Saving(null, new BigDecimal("1500.00"), new Date(), "TestGoal", "Wrong description");

		try {
			try {
				TESTsavingService.addSaving(badSaving);
				fail("ValidationException was expected but not thrown");
			} catch (ValidationException e) {
				System.err.println(e.getMessage());
			}

			try {
				TESTsavingService.addSaving(goodSaving);

				System.out.println("saved");
			} catch (ValidationException e) {
				fail("ValidationException should not have been thrown for goodSaving");
			}
		} finally {
			System.out.println("testSavingService_BAD_Save END\n");
		}
	}

	@Test
	void testSavingServiceDelete() {
		Iterable<Saving> all = TESTsavingService.getAllSavings();

		for (Saving s : all) {
			System.out.println(s.toString());
		}

		TESTsavingService.deleteSaving(2L);

		System.out.println("after delete:");

		int count = 0;
		for (Saving s : all) {
			System.out.println(s.toString());
			count++;
		}

		assertEquals(2, count, "Should be 2 savings after delete");

		System.out.println("testSavingServiceDelete PASSED\n");
	}

	@Test
	void testSavingServiceUpdate() {
		Saving toUpdate = TESTsavingService.getSavingById(3L);

		System.out.println(toUpdate.toString());

		toUpdate.setAmount(new BigDecimal("6000.00"));
		toUpdate.setGoal("Sicily trip - updated");
		toUpdate.setDescription("Mafia description");

		TESTsavingService.updateSaving(toUpdate);

		assertEquals("Sicily trip - updated", toUpdate.getGoal(), "Goal should be updated");
		assertEquals(new BigDecimal("6000.00"), toUpdate.getAmount(), "Amount should be updated");

		System.out.println("after update:\n " + toUpdate);

		System.out.println("testSavingServiceUpdate PASSED\n");
	}

	@Test
	void testSavingService_BAD_Update() {
		Saving toUpdate = TESTsavingService.getSavingById(3L);

		System.out.println(toUpdate.toString());

		toUpdate.setAmount(new BigDecimal("-6000.00"));
		toUpdate.setGoal("");
		toUpdate.setDescription("Mafia description");

		try {
			TESTsavingService.updateSaving(toUpdate);
			fail("ValidationException was expected but not thrown");
		} catch (ValidationException e) {
			System.err.println(e.getMessage());
		} finally {
			System.out.println("testSavingService_BAD_Update END\n");
		}
	}

}
