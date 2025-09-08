package cloudflight.integra.backend.UserTests;

import cloudflight.integra.backend.entity.User;
import cloudflight.integra.backend.entity.validator.UserValidator;
import cloudflight.integra.backend.entity.validator.ValidationException;
import cloudflight.integra.backend.repository.UserRepositoryInMemoryImpl;
import cloudflight.integra.backend.service.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceImplTest {
    private UserRepositoryInMemoryImpl userRepo;
    private UserValidator userValidator;
    private UserServiceImpl userService;
    private User user1, user2;

    @BeforeEach
    void setUp() {
        userRepo = new UserRepositoryInMemoryImpl();
        userValidator = new UserValidator();
        userService = new UserServiceImpl(userRepo, userValidator);
        user1 = new User(null, "Alice", "alice@email.com", "123");
        user2 = new User(null, "Marc", "marc@yahoo.com", "abcd999");
    }

    @Test
    void testAddUser_Valid() {
        User result = userService.addUser(user1);
        assertNotNull(result.getId());
        assertEquals("Alice", result.getName());
        assertEquals("alice@email.com", result.getEmail());
    }

    @Test
    void testAddUser_InvalidEmail() {
        User invalidUser = new User(null, "Elis", "not-an-email", "1234");
        assertThrows(ValidationException.class, () -> userService.addUser(invalidUser));
    }

    @Test
    void testGetUser_Valid() {
        userService.addUser(user1);
        User found = userService.getUser(user1.getId());
        assertNotNull(found);
        assertEquals("Alice", found.getName());
    }

    @Test
    void testGetUser_NotFound() {
        assertThrows(RuntimeException.class, () -> userService.getUser(999L));
    }

    @Test
    void testUpdateUser_Valid() {
        userService.addUser(user1);
        User updated = new User(user1.getId(), "AliceUpdated", "alice@new.com", "newpass");
        User result = userService.updateUser(updated);
        assertEquals("AliceUpdated", result.getName());
        assertEquals("alice@new.com", result.getEmail());
    }

    @Test
    void testUpdateUser_NotFound() {
        User notExisting = new User(123L, "Ghost", "ghost@email.com", "nopass");
        assertThrows(RuntimeException.class, () -> userService.updateUser(notExisting));
    }

    @Test
    void testUpdateUser_InvalidData() {
        userService.addUser(user1);
        User invalid = new User(user1.getId(), null, "noemail", null);
        assertThrows(ValidationException.class, () -> userService.updateUser(invalid));
    }

    @Test
    void testDeleteUser_Valid() {
        userService.addUser(user1);
        userService.deleteUser(user1.getId());
        assertThrows(RuntimeException.class, () -> userService.getUser(user1.getId()));
    }

    @Test
    void testDeleteUser_NotFound() {
        assertThrows(RuntimeException.class, () -> userService.deleteUser(999L));
    }

    @Test
    void testAddUser_NullUser() {
        assertThrows(IllegalArgumentException.class, () -> userService.addUser(null));
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
}