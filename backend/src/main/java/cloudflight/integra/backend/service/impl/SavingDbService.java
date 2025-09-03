// backend/src/main/java/cloudflight/integra/backend/service/impl/SavingDbService.java
package cloudflight.integra.backend.service.impl;

import cloudflight.integra.backend.entity.Saving;
import cloudflight.integra.backend.entity.validation.SavingValidation;
import cloudflight.integra.backend.exception.MoneyMindRuntimeException;
import cloudflight.integra.backend.exception.NotFoundException;
import cloudflight.integra.backend.repository.ISavingJpaRepository;
import cloudflight.integra.backend.service.ISavingService;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Primary
public class SavingDbService implements ISavingService {

    private final ISavingJpaRepository savingRepository;
    private final SavingValidation savingValidator;

    public SavingDbService(ISavingJpaRepository savingRepository, SavingValidation savingValidator) {
        this.savingRepository = savingRepository;
        this.savingValidator = savingValidator;
    }

    @Override
    @Transactional(readOnly = true)
    public Iterable<Saving> getAllSavings() {
        return savingRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Saving getSavingById(Long id) {
        return savingRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Saving not found for id: " + id));
    }

    @Override
    @Transactional
    public void addSaving(Saving saving) {
        if (saving.getId() != null)
            throw new MoneyMindRuntimeException("Id must be null for new entity");
        savingValidator.validate(saving);
        savingRepository.save(saving);
    }

    @Override
    @Transactional
    public void deleteSaving(Long id) {
        getSavingById(id);
        savingRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void updateSaving(Saving saving) {
        savingValidator.validate(saving);
        if (saving.getId() == null) {
            throw new MoneyMindRuntimeException("id must not be null");
        }
        getSavingById(saving.getId());
        savingRepository.save(saving);
    }
}
