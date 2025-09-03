package cloudflight.integra.backend.service.impl;

import cloudflight.integra.backend.entity.Saving;
import cloudflight.integra.backend.entity.validation.SavingValidation;
import cloudflight.integra.backend.exception.MoneyMindRuntimeException;
import cloudflight.integra.backend.repository.ISavingRepository;
import cloudflight.integra.backend.service.ISavingService;
import org.springframework.stereotype.Service;

@Service
public class SavingService implements ISavingService {
    private final ISavingRepository<Long, Saving> savingRepository;
    private final SavingValidation savingValidator;

    /**
     * Constructor for SavingService with Validatior and Repository
     *
     * @param savingRepository -
     * @param savingValidator -
     */
    public SavingService(ISavingRepository<Long, Saving> savingRepository, SavingValidation savingValidator) {
        this.savingRepository = savingRepository;
        this.savingValidator = savingValidator;
    }



    @Override
    public void addSaving(Saving saving) {
        if (saving.getId() != null)
            throw new MoneyMindRuntimeException("Id must be null for new entity");
        savingValidator.validate(saving);
        savingRepository.save(saving);
    }


    @Override
    public Iterable<Saving> getAllSavings() {
        return savingRepository.findAll();
    }


    @Override
    public Saving getSavingById(Long id) {
        return savingRepository.findOne(id);
    }


    @Override
    public void deleteSaving(Long id) {
        savingRepository.delete(id);
    }


    @Override
    public void updateSaving(Saving saving) {
        savingValidator.validate(saving);
        savingRepository.update(saving);
    }
}
