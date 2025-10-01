package cloudflight.integra.backend.service.impl;

import cloudflight.integra.backend.entity.Saving;
import cloudflight.integra.backend.entity.validation.SavingValidator;
import cloudflight.integra.backend.repository.SavingRepository;
import org.springframework.stereotype.Service;

@Service
public class SavingServiceImpl implements cloudflight.integra.backend.service.SavingService {
    private final SavingRepository<Long, Saving> savingRepository;
    private final SavingValidator savingValidator;

    /**
     * Constructor for SavingService with Validatior and Repository
     *
     * @param savingRepository -
     * @param savingValidator -
     */
    public SavingServiceImpl(SavingRepository<Long, Saving> savingRepository, SavingValidator savingValidator) {
        this.savingRepository = savingRepository;
        this.savingValidator = savingValidator;
    }

    @Override
    public void addSaving(Saving saving) {
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
