package cloudflight.integra.backend.user;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import cloudflight.integra.backend.entity.User;
import cloudflight.integra.backend.entity.validation.UserValidator;
import cloudflight.integra.backend.entity.validation.ValidationException;
import cloudflight.integra.backend.exception.NotFoundException;
import cloudflight.integra.backend.repository.UserRepository;
import cloudflight.integra.backend.service.impl.UserServiceImpl;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class UserServiceImplTest {

    @Mock
    private UserRepository userRepo;

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserValidator userValidator;

    private User user1, user2;

    @BeforeEach
    void setUp() {
        user1 = new User(null, "Alice", "alice@email.com", "123");
        user2 = new User(null, "Marc", "marc@yahoo.com", "abcd999");

        when(userRepo.save(any(User.class))).thenAnswer(invocation -> {
            User u = invocation.getArgument(0);
            if (u.getId() == null) u.setId(new Random().nextLong());
            return u;
        });

        when(userRepo.findById(anyLong())).thenAnswer(invocation -> {
            Long id = invocation.getArgument(0);
            if (id.equals(user1.getId())) return Optional.of(user1);
            if (id.equals(user2.getId())) return Optional.of(user2);
            return Optional.empty();
        });
        when(userRepo.findAll()).thenReturn(List.of(user1, user2));
    }

    @Test
    void testAddUser_Valid() {
        User result = userService.addUser(user1);

        assertNotNull(result.getId());
        assertEquals("Alice", result.getName());
        assertEquals("alice@email.com", result.getEmail());

        verify(userRepo).save(any(User.class));
    }

    @Test
    void testAddUser_InvalidEmail() {
        User invalidUser = new User(null, "Elis", "not-an-email", "1234");

        doThrow(new ValidationException(List.of("Email is invalid or empty!")))
                .when(userValidator)
                .validate(any(User.class));

        ValidationException ex = assertThrows(ValidationException.class, () -> userService.addUser(invalidUser));

        assertTrue(ex.getErrors().contains("Email is invalid or empty!"));

        verify(userValidator).validate(any(User.class));
    }

    @Test
    void testGetUser_Valid() {
        User saved = userService.addUser(user1);
        User found = userService.getUser(saved.getId());
        assertNotNull(found);
        assertEquals("Alice", found.getName());
        verify(userRepo, atLeastOnce()).findById(saved.getId());
    }

    @Test
    void testGetUser_NotFound() {
        assertThrows(NotFoundException.class, () -> userService.getUser(999L));
        verify(userRepo).findById(999L);
    }

    @Test
    void testUpdateUser_Valid() {
        User saved = userService.addUser(user1);
        User updated = new User(saved.getId(), "AliceUpdated", "alice@new.com", "newpass");
        User result = userService.updateUser(updated);
        assertEquals("AliceUpdated", result.getName());
        assertEquals("alice@new.com", result.getEmail());
        verify(userRepo, atLeastOnce()).save(any(User.class));
    }

    @Test
    void testUpdateUser_NotFound() {
        User notExisting = new User(123L, "Ghost", "ghost@email.com", "nopass");
        assertThrows(NotFoundException.class, () -> userService.updateUser(notExisting));
    }

    @Test
    void testUpdateUser_InvalidData() {
        userService.addUser(user1);
        User invalid = new User(user1.getId(), null, "noemail", null);
        doThrow(new ValidationException(List.of(
                        "Name cannot be null or empty!",
                        "Email is invalid or empty!",
                        "Password cannot be null or empty!")))
                .when(userValidator)
                .validate(any(User.class));
        ValidationException ex = assertThrows(ValidationException.class, () -> userService.updateUser(invalid));
        assertTrue(ex.getErrors().contains("Name cannot be null or empty!"));
        assertTrue(ex.getErrors().contains("Email is invalid or empty!"));
        assertTrue(ex.getErrors().contains("Password cannot be null or empty!"));
    }

    @Test
    void testDeleteUser_Valid() {
        User saved = userService.addUser(user1);
        assertDoesNotThrow(() -> userService.deleteUser(saved.getId()));
        verify(userRepo).deleteById(saved.getId());
    }

    @Test
    void testDeleteUser_NotFound() {
        assertThrows(NotFoundException.class, () -> userService.deleteUser(999L));
    }

    @Test
    void testAddUser_NullUser() {
        assertThrows(NullPointerException.class, () -> userService.addUser(null));
    }

    @Test
    void testUpdateUser_NullUser() {
        assertThrows(IllegalArgumentException.class, () -> userService.updateUser(null));
    }

    @Test
    void testUpdateUser_NullId() {
        User user = new User(null, "NoId", "noid@email.com", "pass");
        assertThrows(IllegalArgumentException.class, () -> userService.updateUser(user));
    }

    @Test
    void testGetUser_NullId() {
        assertThrows(IllegalArgumentException.class, () -> userService.getUser(null));
    }

    @Test
    void testDeleteUser_NullId() {
        assertThrows(IllegalArgumentException.class, () -> userService.deleteUser(null));
    }

    @Test
    void testAddUser_DuplicateEmail() {
        userService.addUser(user1);
        User duplicate = new User(null, "Bob", "alice@email.com", "pass2");
        doThrow(new ValidationException(List.of("Email already exists!")))
                .when(userValidator)
                .validate(any(User.class));
        ValidationException ex = assertThrows(ValidationException.class, () -> userService.addUser(duplicate));
        assertTrue(ex.getMessage().contains("Email already exists!"));
    }

    @Test
    void testUpdateUser_DuplicateEmail() {
        userService.addUser(user1);
        userService.addUser(user2);
        User updated = new User(user2.getId(), "Marc", "alice@email.com", "abcd999");
        doThrow(new ValidationException(List.of("Email already exists!")))
                .when(userValidator)
                .validate(any(User.class));
        ValidationException ex = assertThrows(ValidationException.class, () -> userService.updateUser(updated));
        assertTrue(ex.getErrors().contains("Email already exists!"));
    }

    @Test
    void testUpdateUser_SameEmailAllowed() {
        userService.addUser(user1);
        User updated = new User(user1.getId(), "Alice", "alice@email.com", "newpass");
        assertDoesNotThrow(() -> userService.updateUser(updated));
    }
}
