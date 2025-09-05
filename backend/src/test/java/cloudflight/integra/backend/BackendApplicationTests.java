package cloudflight.integra.backend;

import cloudflight.integra.backend.model.Frequency;
import cloudflight.integra.backend.model.Payment;
import cloudflight.integra.backend.model.validator.PaymentValidator;
import cloudflight.integra.backend.repository.IPaymentRepository;

import cloudflight.integra.backend.repository.inMemoryImpl.InMemoryPaymentRepository;
import cloudflight.integra.backend.service.IService;
import cloudflight.integra.backend.service.impl.ServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BackendApplicationTests {
	private IPaymentRepository paymentRepository;
	private IService service;
	private PaymentValidator paymentValidator;
	private Payment p1;
	private Payment p2;
	@BeforeEach
	void setUp() {
		paymentRepository=new InMemoryPaymentRepository();
		paymentValidator=new PaymentValidator();
		p1=new Payment(null,"Internet", BigDecimal.valueOf(50), Frequency.MONTHLY,new Date(),true);
		p2=new Payment(null,"Gym", BigDecimal.valueOf(150), Frequency.MONTHLY,new Date(),true);
		service=new ServiceImpl(paymentRepository,paymentValidator);
	}
	@Test
	void testRepoSaveFindById(){
		paymentRepository.save(p1);
		Optional<Payment> payment=paymentRepository.findById(p1.getId());
		assertEquals(p1.getId(),payment.get().getId());
		assertEquals("Internet",payment.get().getName());
		assertEquals(BigDecimal.valueOf(50),payment.get().getAmount());
		assertEquals(Frequency.MONTHLY,payment.get().getFrequency());
		System.out.println("Test repo save findById finished successfully");
	}
	@Test
	void testRepoFindAll(){
		List<Payment> payments=paymentRepository.findAll();
		assertEquals(0,payments.size());
		paymentRepository.save(p1);
		paymentRepository.save(p2);
		payments=paymentRepository.findAll();
		assertEquals(2,payments.size());
		System.out.println("Test repo find all finished successfully");
	}
	@Test
	void testRepoDelete(){
		paymentRepository.save(p1);
		paymentRepository.save(p2);
		List<Payment> payments=paymentRepository.findAll();
		paymentRepository.delete(2L);
		payments=paymentRepository.findAll();
		assertEquals(1,payments.size());
		assertEquals("Internet",payments.get(0).getName());
		assertEquals(BigDecimal.valueOf(50),payments.get(0).getAmount());
		assertEquals(Frequency.MONTHLY,payments.get(0).getFrequency());
		System.out.println("Test repo delete finished successfully");

	}

	@Test
	void testRepoUpdate(){
		paymentRepository.save(p1);
		paymentRepository.save(p2);
		Payment p3=paymentRepository.findById(p2.getId()).get();
		p3.setAmount(BigDecimal.valueOf(1900));
		p3.setFrequency(Frequency.ONE_TIME);
		paymentRepository.update(p3);
		Payment test=paymentRepository.findById(2L).get();
		assertEquals(p3.getId(),test.getId());
		assertEquals("Gym",test.getName());
		assertEquals(BigDecimal.valueOf(1900),test.getAmount());
		assertEquals(Frequency.ONE_TIME,test.getFrequency());
		System.out.println("Test repo update finished successfully");

	}

	@Test
	void testServiceSaveFindById(){
		service.addPayment(p1);
		Payment test=paymentRepository.findById(p1.getId()).get();
		assertEquals(p1.getId(),test.getId());
		assertEquals("Internet",test.getName());
		assertEquals(BigDecimal.valueOf(50),test.getAmount());
		assertEquals(Frequency.MONTHLY,test.getFrequency());
		System.out.println("Test service save finished successfully");

	}

	@Test
	void testServiceFindAll(){
		service.addPayment(p1);
		service.addPayment(p2);
		List<Payment> payments=paymentRepository.findAll();
		assertEquals(2,payments.size());
		System.out.println("Test service find all finished successfully");
	}
	@Test
	void testServiceDelete(){
		service.addPayment(p1);
		service.addPayment(p2);
		List<Payment> payments=paymentRepository.findAll();
		paymentRepository.delete(p2.getId());
		payments=paymentRepository.findAll();
		assertEquals(1,payments.size());
		assertEquals("Internet",payments.get(0).getName());
		assertEquals(BigDecimal.valueOf(50),payments.get(0).getAmount());
		assertEquals(Frequency.MONTHLY,payments.get(0).getFrequency());
		System.out.println("Test service delete finished successfully");
	}
	@Test
	void testServiceUpdate(){
		service.addPayment(p1);
		service.addPayment(p2);
		List<Payment> payments=paymentRepository.findAll();
		Payment p3=paymentRepository.findById(p2.getId()).get();
		p3.setAmount(BigDecimal.valueOf(1900));
		p3.setFrequency(Frequency.ONE_TIME);
		paymentRepository.update(p3);
		Payment test=paymentRepository.findById(2L).get();
		assertEquals(p3.getId(),test.getId());
		assertEquals("Gym",test.getName());
		assertEquals(BigDecimal.valueOf(1900),test.getAmount());
		assertEquals(Frequency.ONE_TIME,test.getFrequency());
		System.out.println("Test service update finished successfully");

	}


	@Test
	void testRepoDeleteFailed(){
		paymentRepository.save(p1);
		paymentRepository.save(p2);
		paymentRepository.delete(100L);
		assertEquals(2,paymentRepository.findAll().size());
		paymentRepository.delete(2L);
		paymentRepository.delete(2L);
		assertEquals(1,paymentRepository.findAll().size());
		assertEquals("Internet",paymentRepository.findAll().get(0).getName());
		System.out.println("Test repo delete failed, finished successfully");
	}
	@Test
	void testRepoFindByIdFailed(){
		paymentRepository.save(p1);
		paymentRepository.save(p2);
		assertThrows(RuntimeException.class, () -> 	paymentRepository.findById(100L).get());
		System.out.println("Test repo find by id failed,finished successfully");

	}


	@Test
	void testServiceFindByIdFailed(){
		service.addPayment(p1);
		service.addPayment(p2);
		assertThrows(RuntimeException.class, () -> 	service.getPayment(100L));
		System.out.println("Test service find by id failed,finished successfully");
	}
	@Test
	void testServiceUpdateFailed(){
		service.addPayment(p1);
		service.addPayment(p2);
		Payment p3=new Payment(p2.getId(), p2.getName(), BigDecimal.ZERO, Frequency.ONE_TIME, p2.getNextDueDate(), p2.getIsActive());
		assertThrows(RuntimeException.class, () -> 	service.updatePayment(p3));
		Payment test=service.getPayment(p2.getId());
		assertEquals(p2.getId(),test.getId());
		assertEquals("Gym",test.getName());
		assertEquals(BigDecimal.valueOf(150),test.getAmount());
		assertEquals(Frequency.MONTHLY,test.getFrequency());
		System.out.println("Test service update failed, finished successfully");

	}

	@Test
	void testServiceDeleteFailed(){
		service.addPayment(p1);
		service.addPayment(p2);
		assertThrows(RuntimeException.class, () -> service.deletePayment(100L));
		assertEquals(2,service.getPayments().size());
		service.deletePayment(2L);
		assertThrows(RuntimeException.class, () -> service.deletePayment(100L));
		assertEquals(1,service.getPayments().size());
		assertEquals("Internet",service.getPayments().get(0).getName());
		System.out.println("Test service delete failed, finished successfully");
	}

	@Test
	void testServiceSaveFailed(){
		service.addPayment(p1);
		service.addPayment(p2);
		Payment p3=new Payment(null,"Netflix",BigDecimal.valueOf(0),Frequency.YEARLY,new Date(),true);
		Payment p4=new Payment(null,"HBO",BigDecimal.valueOf(0),null,new Date(),true);
		assertThrows(RuntimeException.class, () -> 	service.addPayment(p3));
		assertThrows(RuntimeException.class, () -> 	service.addPayment(p4));
		List<Payment> payments=service.getPayments();
		assertEquals(2,payments.size());
		System.out.println("Test service save failed, finished successfully");

	}

	@Test
	void contextLoads() {
	}

}
