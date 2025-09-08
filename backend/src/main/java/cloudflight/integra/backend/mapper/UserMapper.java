package cloudflight.integra.backend.mapper;

import cloudflight.integra.backend.dto.UserDto;
import cloudflight.integra.backend.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    /**
     * Converts a User entity to a UserDto.
     * @param user the User entity to convert
     * @return the corresponding UserDto
     */
    public static UserDto toDto(User user) {
        UserDto dto = new UserDto();
        dto.id = user.getId();
        dto.name = user.getName();
        dto.email = user.getEmail();
        dto.password = user.getPassword();
        dto.createdAt = user.getCreatedAt();
        return dto;
    }

    /**
     * Converts a UserDto to a User entity.
     * @param dto the UserDto to convert
     * @return the corresponding User entity
     * */
    public User fromDto(UserDto dto) {
        return new User(dto.id, dto.name, dto.email, dto.password);
    }
}
