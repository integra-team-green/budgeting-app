package cloudflight.integra.backend.mapper;

import cloudflight.integra.backend.dto.SavingDTO;
import cloudflight.integra.backend.entity.Saving;
import cloudflight.integra.backend.entity.User;
import java.util.ArrayList;
import java.util.List;

public class SavingMapper {
    public static SavingDTO toDTO(Saving saving) {
        if (saving == null) {
            return null;
        }
        SavingDTO dto = new SavingDTO();
        dto.setId(saving.getId());
        dto.setUserId(saving.getUser() != null ? saving.getUser().getId() : null);
        dto.setAmount(saving.getAmount());
        dto.setDate(saving.getDate());
        dto.setGoal(saving.getGoal());
        dto.setDescription(saving.getDescription());
        return dto;
    }

    public static Saving toEntity(SavingDTO savingDTO) {
        if (savingDTO == null) {
            return null;
        }
        User user = savingDTO.getUserId() != null ? new User(savingDTO.getUserId()) : null;
        Saving saving = new Saving();
        saving.setId(savingDTO.getId());
        saving.setUser(user);
        saving.setAmount(savingDTO.getAmount());
        saving.setDate(savingDTO.getDate());
        saving.setGoal(savingDTO.getGoal());
        saving.setDescription(savingDTO.getDescription());
        return saving;
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
