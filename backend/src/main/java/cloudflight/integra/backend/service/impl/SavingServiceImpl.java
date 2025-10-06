package cloudflight.integra.backend.service.impl;

import cloudflight.integra.backend.dto.SavingDTO;
import cloudflight.integra.backend.entity.Saving;
import cloudflight.integra.backend.entity.User;
import cloudflight.integra.backend.entity.validation.SavingValidator;
import cloudflight.integra.backend.exception.NotFoundException;
import cloudflight.integra.backend.mapper.SavingMapper;
import cloudflight.integra.backend.repository.SavingRepository;
import cloudflight.integra.backend.repository.UserRepository;
import cloudflight.integra.backend.service.SavingService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SavingServiceImpl implements SavingService {
    private final SavingRepository savingRepository;
    private final SavingValidator savingValidator;
    private final UserRepository userRepository;

    /**
     * Constructor for SavingService with Validatior and Repository
     *
     * @param savingRepository -
     * @param savingValidator -
     */
    public SavingServiceImpl(
            SavingRepository savingRepository, SavingValidator savingValidator, UserRepository userRepository) {
        this.savingRepository = savingRepository;
        this.savingValidator = savingValidator;
        this.userRepository = userRepository;
    }

    @Transactional
    @Override
    public SavingDTO addSaving(SavingDTO savingDTO) {
        Saving saving = SavingMapper.toEntity(savingDTO);
        savingValidator.validate(saving);
        User user = userRepository
                .findById(savingDTO.getUserId())
                .orElseThrow(() -> new NotFoundException("User", savingDTO.getUserId()));
        saving.setUser(user);
        return SavingMapper.toDTO(savingRepository.save(saving));
    }

    @Override
    @Transactional(readOnly = true)
    public Iterable<SavingDTO> getAllSavings() {
        List<SavingDTO> dtos = new ArrayList<>();
        for (Saving saving : savingRepository.findAll()) {
            dtos.add(SavingMapper.toDTO(saving));
        }
        return dtos;
    }

    @Override
    @Transactional(readOnly = true)
    public SavingDTO getSavingById(Long id) {
        if (id == null) throw new IllegalArgumentException("Saving id must not be null.");
        return SavingMapper.toDTO(savingRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("Saving with id " + id + " not found")));
    }

    @Override
    @Transactional
    public void deleteSaving(Long id) {
        if (savingRepository.findById(id).isEmpty()) {
            throw new NotFoundException("Saving with id " + id + " not found for delete");
        }
        savingRepository.deleteById(id);
    }

    @Override
    @Transactional
    public SavingDTO updateSaving(SavingDTO savingDTO) {
        Saving saving = SavingMapper.toEntity(savingDTO);
        savingValidator.validate(saving);

        if (savingRepository.findById(saving.getId()).isEmpty()) {
            throw new NotFoundException("Saving", saving.getId());
        }
        User user = userRepository
                .findById(savingDTO.getUserId())
                .orElseThrow(() -> new NotFoundException("User", savingDTO.getUserId()));
        saving.setUser(user);

        return SavingMapper.toDTO(savingRepository.save(saving));
    }
}
