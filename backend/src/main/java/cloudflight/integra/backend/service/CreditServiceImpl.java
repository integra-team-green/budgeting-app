package cloudflight.integra.backend.service;


import cloudflight.integra.backend.entity.Credit;
import cloudflight.integra.backend.exception.MoneyMindRuntimeException;
import cloudflight.integra.backend.exception.NotFoundException;
import cloudflight.integra.backend.repository.CreditRepository;
import cloudflight.integra.backend.validation.CreditValidator;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Business logic + validation. Uses repository abstraction (could be in-memory or JPA).
 */
@Service
public class CreditServiceImpl implements CreditService {

    private final CreditRepository repository;

    public CreditServiceImpl(CreditRepository repository) {
        this.repository = repository;
    }

    @Override
    public Credit create(Credit credit) {
        // defensive: ensure id is null
        if (credit.getId() != null) {
            throw new MoneyMindRuntimeException("Id must be null for new entity");
        }
        CreditValidator.validateCredit(credit);
        return repository.save(credit);
    }

    @Override
    public Credit findById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Credit not found");
        }

        return repository.findById(id).orElseThrow(() -> new NotFoundException("credit", id));
    }

    @Override
    public List<Credit> findAll() {
        return repository.findAll();
    }

    @Override
    public Credit update(Long id, Credit credit) {
        if (id == null) {
            throw new IllegalArgumentException("Id cannot be null");
        }
        // ensure the id in path and the payload (if present) match
        if (credit.getId() != null && !credit.getId().equals(id)) {
            throw new IllegalArgumentException("Id in path and payload must match");
        }
        // set id into payload to run common validation
        credit.setId(id);
        CreditValidator.validateCredit(credit);

        if (repository.existsById(id)) {
            throw new NotFoundException("credit", id);
        }
        return repository.save(credit);
    }

    @Override
    public void delete(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Id cannot be null");
        }
        if (repository.existsById(id)) {
            throw new NotFoundException("credit", id);
        }
        repository.deleteById(id);
    }
}