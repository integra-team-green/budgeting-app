package cloudflight.integra.backend.mapper;

import cloudflight.integra.backend.dto.IncomeDTO;
import cloudflight.integra.backend.entity.Income;

public class IncomeMapper {

    /**
     * Converts an IncomeDTO to an Income entity.
     *
     * @param dto the IncomeDTO to convert
     * @return the corresponding Income entity
     */
    public static Income toEntity(IncomeDTO dto) {
        Income income = new Income();
        income.setId(dto.getId());
        income.setUserId(dto.getUserId());
        income.setSource(dto.getSource());
        income.setAmount(dto.getAmount());
        income.setDate(dto.getDate());
        income.setDescription(dto.getDescription());
        income.setFrequency(dto.getFrequency());
        income.setEndDate(dto.getEndDate());
        return income;
    }

    /**
     * Converts an Income entity to IncomeDTO
     *
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
        dto.setFrequency(income.getFrequency());
        dto.setEndDate(income.getEndDate());
        dto.setUserId(income.getUserId());

        return dto;
    }
}
