package cloudflight.integra.backend.mapper.implementation;

import cloudflight.integra.backend.dto.SavingDTO;
import cloudflight.integra.backend.entity.Saving;
import cloudflight.integra.backend.mapper.ISavingMapper;

import java.util.ArrayList;
import java.util.List;

public class SavingMapper implements ISavingMapper {
    @Override
    public SavingDTO toDTO(Saving saving) {
        if (saving == null) {
            return null;
        }
        return new SavingDTO(
                saving.getId(),
                saving.getAmount(),
                saving.getDate(),
                saving.getGoal(),
                saving.getDescription()
        );
    }

    @Override
    public Saving toEntity(SavingDTO savingDTO) {
        if (savingDTO == null) {
            return null;
        }
        return new Saving(
                savingDTO.getId(),
                savingDTO.getAmount(),
                savingDTO.getDate(),
                savingDTO.getGoal(),
                savingDTO.getDescription()
        );
    }

    @Override
    public Iterable<SavingDTO> toDtoList(Iterable<Saving> savings) {
        List<SavingDTO> savingDTOS = new ArrayList<>();

        for (Saving saving : savings) {
            savingDTOS.add(toDTO(saving));
        }

        return savingDTOS;
    }

    @Override
    public Iterable<Saving> toEntityList(Iterable<SavingDTO> savingDTOS) {
        List<Saving> savings = new ArrayList<>();

        for (SavingDTO savingDTO : savingDTOS) {
            savings.add(toEntity(savingDTO));
        }

        return savings;
    }
}
