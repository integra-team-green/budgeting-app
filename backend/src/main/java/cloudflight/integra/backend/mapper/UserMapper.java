package cloudflight.integra.backend.mapper;

import cloudflight.integra.backend.dto.UserDTO;
import cloudflight.integra.backend.entity.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class UserMapper {
    /**
     * Converts a User entity to a UserDto.
     * @param user the User entity to convert
     * @return the corresponding UserDto
     */
    public static UserDTO toDto(Optional<User> user) {
        if (user.isPresent()) {
            User u = user.get();
            UserDTO dto = new UserDTO();
            dto.setId(u.getId());
            dto.setName(u.getName());
            dto.setEmail(u.getEmail());
            dto.setPassword(u.getPassword());
            dto.setCreatedAt(u.getCreatedAt());
            dto.setBalance(u.getBalance());
            return dto;
        }
        return null;
    }

    /**
     * Converts a UserDto to a User entity.
     * @param dto the UserDto to convert
     * @return the corresponding User entity
     * */
    public static User fromDto(UserDTO dto) {
        return new User(dto.getId(), dto.getName(), dto.getEmail(), dto.getPassword(), dto.getBalance());
    }

    public static Iterable<UserDTO> toDtoList(Iterable<User> users) {
        List<UserDTO> userDTOS = new ArrayList<>();

        for (User user : users) {
            userDTOS.add(toDto(Optional.ofNullable(user)));
        }

        return userDTOS;
    }
}