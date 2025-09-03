package cloudflight.integra.backend.mapper;

import cloudflight.integra.backend.dto.SavingDTO;
import cloudflight.integra.backend.entity.Saving;
import org.mapstruct.Mapper;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public class SavingMapper {

    /**
     * Convert Saving entity to SavingDTO
     *
     * @param saving -
     * @return -
     */
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

    /**
     * Convert SavingDTO to Saving entity
     *
     * @param savingDTO -
     * @return -
     */
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

    /**
     * Convert Iterable of Saving entities to Iterable of SavingDTOs
     *
     * @param savings -
     * @return -
     */
    public static Iterable<SavingDTO> toDtoList(Iterable<Saving> savings) {
        List<SavingDTO> savingDTOS = new ArrayList<>();

        for (Saving saving : savings) {
            savingDTOS.add(toDTO(saving));
        }

        return savingDTOS;
    }

    /**
     * Convert Iterable of SavingDTOs to Iterable of Saving entities
     *
     * @param savingDTOS -
     * @return -
     */
    public static Iterable<Saving> toEntityList(Iterable<SavingDTO> savingDTOS) {
        List<Saving> savings = new ArrayList<>();

        for (SavingDTO savingDTO : savingDTOS) {
            savings.add(toEntity(savingDTO));
        }

        return savings;
    }
}
