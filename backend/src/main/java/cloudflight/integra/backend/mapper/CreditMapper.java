package cloudflight.integra.backend.mapper;

import cloudflight.integra.backend.dto.CreditDTO;
import cloudflight.integra.backend.entity.Credit;

/**
 * Mapper for Credit and CreditDTO
 *
 * @author Oana Nourescu
 */
public class CreditMapper {

    /**
     * Convert Credit entity to CreditDTO
     *
     * @param entity .
     * @return .
     */
    public static CreditDTO toDto(Credit entity) {
        if (entity == null) return null;
        CreditDTO dto = new CreditDTO();
        dto.setId(entity.getId());
        dto.setAmount(entity.getAmount());
        dto.setLender(entity.getLender());
        dto.setStartDate(entity.getStartDate());
        dto.setDueDate(entity.getDueDate());
        dto.setInterestRate(entity.getInterestRate());
        dto.setDescription(entity.getDescription());
        return dto;
    }

    /**
     * Convert CreditDTO to Credit entity
     *
     * @param dto .
     * @return .
     */
    public static Credit toEntity(CreditDTO dto) {
        if (dto == null) return null;
        return new Credit()
            .withId(dto.getId())
            .withAmount(dto.getAmount())
            .withLender(dto.getLender())
            .withStartDate(dto.getStartDate())
            .withDueDate(dto.getDueDate())
            .withInterestRate(dto.getInterestRate())
            .withDescription(dto.getDescription());
    }
}
