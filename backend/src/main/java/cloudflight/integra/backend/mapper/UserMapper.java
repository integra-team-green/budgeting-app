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
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setPassword(user.getPassword());
        dto.setCreatedAt(user.getCreatedAt());
        return dto;
    }

    /**
     * Converts a UserDto to a User entity.
     * @param dto the UserDto to convert
     * @return the corresponding User entity
     * */
    public User fromDto(UserDto dto) {
        return new User(dto.getId(), dto.getName(), dto.getEmail(), dto.getPassword());
    }
}