package cloudflight.integra.backend.controller;

import cloudflight.integra.backend.controller.problem.UserApiErrorResponses;
import cloudflight.integra.backend.dto.UserDTO;
import cloudflight.integra.backend.entity.User;
import cloudflight.integra.backend.mapper.UserMapper;
import cloudflight.integra.backend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.util.Collection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@UserApiErrorResponses
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @Operation(
            summary = "Get users",
            operationId = "getUsers",
            responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "OK",
                        content =
                                @Content(
                                        mediaType = "application/json",
                                        schema = @Schema(implementation = UserDTO.class))),
            })
    @GetMapping()
    public ResponseEntity<Collection<UserDTO>> getAllUsers() {
        logger.info("Received GET request for all users");

        Collection<User> users = userService.getAllUsers();
        Collection<UserDTO> userDTOs = UserMapper.toDtoList(users);
        logger.info("Users retrieved: {}", userDTOs);
        return ResponseEntity.ok(userDTOs);
    }

    @Operation(
            summary = "Get user by id",
            operationId = "getUserById",
            responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "OK",
                        content =
                                @Content(
                                        mediaType = "application/json",
                                        schema = @Schema(implementation = UserDTO.class))),
                @ApiResponse(responseCode = "404", description = "Not found")
            })
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable Long id) {
        logger.info("Received GET request for user with id: {}", id);

        User user = userService.getUser(id);
        logger.info("User retrieved: {}", user);
        return ResponseEntity.ok(UserMapper.toDto(user));
    }

    @Operation(
            summary = "Create user",
            operationId = "createUser",
            responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Created",
                        content =
                                @Content(
                                        mediaType = "application/json",
                                        schema = @Schema(implementation = UserDTO.class)))
            })
    @PostMapping
    public ResponseEntity<UserDTO> addUser(@RequestBody UserDTO userDto) {
        logger.info("Received POST request to add user: {}", userDto);

        User userToAdd = UserMapper.fromDto(userDto);
        User createdUser = userService.addUser(userToAdd);
        logger.info("User created: {}", createdUser);
        return ResponseEntity.ok(UserMapper.toDto(createdUser));
    }

    @Operation(
            summary = "Update user",
            operationId = "updateUser",
            responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Updated",
                        content =
                                @Content(
                                        mediaType = "application/json",
                                        schema = @Schema(implementation = UserDTO.class))),
                @ApiResponse(responseCode = "404", description = "Not found")
            })
    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @RequestBody UserDTO userDto) {
        logger.info("Received PUT request to update user with id: {}. Data: {}", id, userDto);
        if (!id.equals(userDto.getId())) {
            throw new IllegalArgumentException("ID in path and request body do not match.");
        }
        User userToUpdate = UserMapper.fromDto(userDto);
        userToUpdate.setPassword(passwordEncoder.encode(userToUpdate.getPassword()));
        User updatedUser = userService.updateUser(userToUpdate);
        logger.info("User updated: {}", updatedUser);
        return ResponseEntity.ok(UserMapper.toDto(updatedUser));
    }

    @Operation(
            summary = "Delete user",
            operationId = "deleteUser",
            responses = {
                @ApiResponse(
                        responseCode = "204",
                        description = "deleted",
                        content =
                                @Content(
                                        mediaType = "application/json",
                                        schema = @Schema(implementation = UserDTO.class))),
                @ApiResponse(responseCode = "404", description = "Not found")
            })
    @DeleteMapping("/{id}")
    public ResponseEntity<UserDTO> delete(@PathVariable Long id) {
        logger.info("Received DELETE request for user with id: {}", id);
        userService.deleteUser(id);
        logger.info("User with id {} deleted.", id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Get user by email",
            operationId = "getUserByEmail",
            responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "OK",
                        content =
                                @Content(
                                        mediaType = "application/json",
                                        schema = @Schema(implementation = UserDTO.class))),
                @ApiResponse(responseCode = "404", description = "Not found")
            })
    @GetMapping("/by-email")
    public ResponseEntity<UserDTO> getUserByEmail(@RequestParam String email) {
        logger.info("Received GET request for user with email: {}", email);
        User user = userService.getUserByEmail(email);
        logger.info("User retrieved by email: {}", user);
        return ResponseEntity.ok(UserMapper.toDto(user));
    }
}
