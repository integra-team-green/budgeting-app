package cloudflight.integra.backend;

import cloudflight.integra.backend.entity.Credit;
import cloudflight.integra.backend.repository.CreditRepository;
import cloudflight.integra.backend.service.CreditService;
import cloudflight.integra.backend.service.CreditServiceImpl;
import cloudflight.integra.backend.validation.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CreditServiceImplTest {

    private CreditRepository repository;
    private CreditService service;

    @BeforeEach
    void setup() {
        repository = Mockito.mock(CreditRepository.class);
        service = new CreditServiceImpl(repository);
    }

    @Test
    void create_withInvalidAmount_shouldThrowValidationException() {
        Credit credit = new Credit()
            .withAmount(BigDecimal.ZERO)
            .withLender("Bank")
            .withStartDate(LocalDate.now())
            .withDueDate(LocalDate.now().plusDays(30))
            .withInterestRate(5.0);

        assertThrows(ValidationException.class, () -> service.create(credit));
    }

    @Test
    void create_withValidCredit_shouldSave() {
        Credit credit = new Credit()
            .withAmount(BigDecimal.valueOf(1000))
            .withLender("Bank")
            .withStartDate(LocalDate.now())
            .withDueDate(LocalDate.now().plusDays(30))
            .withInterestRate(5.0);

        when(repository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Credit saved = service.create(credit);

        assertNotNull(saved);
        verify(repository, times(1)).save(any());
    }
}
