package cloudflight.integra.backend.controller;

import cloudflight.integra.backend.controller.problem.UserApiErrorResponses;
import cloudflight.integra.backend.dto.UserDto;
import cloudflight.integra.backend.entity.User;
import cloudflight.integra.backend.mapper.UserMapper;
import cloudflight.integra.backend.service.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@UserApiErrorResponses
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final IUserService userService;
    private final UserMapper userMapper;

    @Autowired
    public UserController(IUserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable Long id) {
        logger.info("Received GET request for user with id: {}", id);
        User user = userService.getUser(id);
        logger.info("User retrieved: {}", user);
        return ResponseEntity.ok(UserMapper.toDto(user));
    }

    @PostMapping
    public ResponseEntity<UserDto> addUser(@RequestBody UserDto userDto) {
        logger.info("Received POST request to add user: {}", userDto);
        User userToAdd = userMapper.fromDto(userDto);
        User createdUser = userService.addUser(userToAdd);
        logger.info("User created: {}", createdUser);
        return ResponseEntity.ok(userMapper.toDto(createdUser));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long id, @RequestBody UserDto userDto) {
        logger.info("Received PUT request to update user with id: {}. Data: {}", id, userDto);
        User userToUpdate = userMapper.fromDto(userDto);
        userToUpdate.setId(id);
        User updatedUser = userService.updateUser(userToUpdate);
        logger.info("User updated: {}", updatedUser);
        return ResponseEntity.ok(userMapper.toDto(updatedUser));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        logger.info("Received DELETE request for user with id: {}", id);
        userService.deleteUser(id);
        logger.info("User with id {} deleted.", id);
        return ResponseEntity.noContent().build();
    }
}