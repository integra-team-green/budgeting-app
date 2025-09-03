package cloudflight.integra.backend.mapper;

import cloudflight.integra.backend.dto.SavingDTO;
import cloudflight.integra.backend.entity.Saving;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ISavingMapper {

    SavingDTO toDTO(Saving saving);

    Saving toEntity(SavingDTO savingDTO);

    Iterable<SavingDTO> toDtoList(Iterable<Saving> savings);

    Iterable<Saving> toEntityList(Iterable<SavingDTO> savingDTOS);
}
