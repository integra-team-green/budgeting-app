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

	@Test
	void contextLoads() {
	}

}
