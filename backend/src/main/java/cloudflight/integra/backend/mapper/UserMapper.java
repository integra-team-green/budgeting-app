package cloudflight.integra.backend.mapper;

import cloudflight.integra.backend.dto.UserDTO;
import cloudflight.integra.backend.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    /**
     * Converts a User entity to a UserDto.
     * @param user the User entity to convert
     * @return the corresponding UserDto
     */
    public static UserDTO toDto(User user) {
        UserDTO dto = new UserDTO();
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
    public User fromDto(UserDTO dto) {
        return new User(dto.getId(), dto.getName(), dto.getEmail(), dto.getPassword());
    }
}