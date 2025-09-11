package cloudflight.integra.backend.mapper;

import cloudflight.integra.backend.dto.SavingDTO;
import cloudflight.integra.backend.entity.Saving;

import java.util.ArrayList;
import java.util.List;

public class SavingMapper {
    public static SavingDTO toDTO(Saving saving) {
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

    public static Saving toEntity(SavingDTO savingDTO) {
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

    public static Iterable<SavingDTO> toDtoList(Iterable<Saving> savings) {
        List<SavingDTO> savingDTOS = new ArrayList<>();

        for (Saving saving : savings) {
            savingDTOS.add(toDTO(saving));
        }

        return savingDTOS;
    }

    public Iterable<Saving> toEntityList(Iterable<SavingDTO> savingDTOS) {
        List<Saving> savings = new ArrayList<>();

        for (SavingDTO savingDTO : savingDTOS) {
            savings.add(toEntity(savingDTO));
        }

        return savings;
    }
}
