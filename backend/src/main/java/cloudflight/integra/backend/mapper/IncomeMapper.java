package cloudflight.integra.backend.mapper;

import cloudflight.integra.backend.dto.IncomeDTO;
import cloudflight.integra.backend.entity.Income;

public class IncomeMapper {

    /**
     * Converts an IncomeDTO to an Income entity.
     * @param dto the IncomeDTO to convert
     * @return the corresponding Income entity
     */
    public static Income toEntity(IncomeDTO dto) {
        return new Income(
                dto.getId(),
                dto.getAmount(),
                dto.getSource(),
                dto.getDate(),
                dto.getDescription()
        );
    }

    /**
     * Converts an Income entity to IncomeDTO
     * @param income the entity to convert
     * @return the corresponding IncomeDTO
     */
    public static IncomeDTO toDTO(Income income) {
        IncomeDTO dto = new IncomeDTO();
        dto.setId(income.getId());
        dto.setAmount(income.getAmount());
        dto.setSource(income.getSource());
        dto.setDate(income.getDate());
        dto.setDescription(income.getDescription());
        return dto;
    }
}
