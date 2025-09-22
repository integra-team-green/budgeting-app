package cloudflight.integra.backend.controller;

import cloudflight.integra.backend.controller.problem.UserApiErrorResponses;
import cloudflight.integra.backend.dto.UserDTO;
import cloudflight.integra.backend.entity.User;
import cloudflight.integra.backend.mapper.UserMapper;
import cloudflight.integra.backend.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/api/v1/users")
@UserApiErrorResponses
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public ResponseEntity<Collection<UserDTO>> getAllUsers() {
        logger.info("Received GET request for all users");

        Collection<User> users = userService.getAllUsers();
        Collection<UserDTO> userDTOs = UserMapper.toDtoList(users);
        logger.info("Users retrieved: {}", userDTOs);
        return ResponseEntity.ok(userDTOs);

    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable Long id) {
        logger.info("Received GET request for user with id: {}", id);

        User user = userService.getUser(id);
        logger.info("User retrieved: {}", user);
        return ResponseEntity.ok(UserMapper.toDto(user));

    }

    @PostMapping
    public ResponseEntity<UserDTO> addUser(@RequestBody UserDTO userDto) {
        logger.info("Received POST request to add user: {}", userDto);

        User userToAdd = UserMapper.fromDto(userDto);
        User createdUser = userService.addUser(userToAdd);
        logger.info("User created: {}", createdUser);
        return ResponseEntity.ok(UserMapper.toDto(createdUser));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @RequestBody UserDTO userDto) {
        logger.info("Received PUT request to update user with id: {}. Data: {}", id, userDto);
        if (!id.equals(userDto.getId())) {
            throw new IllegalArgumentException("ID in path and request body do not match.");
        }
        User userToUpdate = UserMapper.fromDto(userDto);
        User updatedUser = userService.updateUser(userToUpdate);
        logger.info("User updated: {}", updatedUser);
        return ResponseEntity.ok(UserMapper.toDto(updatedUser));

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<UserDTO> delete(@PathVariable Long id) {
        logger.info("Received DELETE request for user with id: {}", id);
        userService.deleteUser(id);
        logger.info("User with id {} deleted.", id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/by-email")
    public ResponseEntity<UserDTO> getUserByEmail(@RequestParam String email) {
        logger.info("Received GET request for user with email: {}", email);
        User user = userService.getUserByEmail(email);
        logger.info("User retrieved by email: {}", user);
        return ResponseEntity.ok(UserMapper.toDto(user));
    }
}