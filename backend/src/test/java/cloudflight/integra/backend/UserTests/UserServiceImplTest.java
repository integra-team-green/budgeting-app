package cloudflight.integra.backend.UserTests;

import cloudflight.integra.backend.entity.User;
import cloudflight.integra.backend.entity.validator.NotFoundException;
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
        ValidationException ex = assertThrows(ValidationException.class, () -> userService.addUser(invalidUser));
        assertTrue(ex.getMessages().contains("Email is invalid or empty!"));
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
        assertThrows(NotFoundException.class, () -> userService.getUser(999L));
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
        assertThrows(NotFoundException.class, () -> userService.updateUser(notExisting));
    }

    @Test
    void testUpdateUser_InvalidData() {
        userService.addUser(user1);
        User invalid = new User(user1.getId(), null, "noemail", null);
        ValidationException ex = assertThrows(ValidationException.class, () -> userService.updateUser(invalid));
        assertTrue(ex.getMessages().contains("Name cannot be null or empty!"));
        assertTrue(ex.getMessages().contains("Email is invalid or empty!"));
        assertTrue(ex.getMessages().contains("Password cannot be null or empty!"));
    }

    @Test
    void testDeleteUser_Valid() {
        userService.addUser(user1);
        userService.deleteUser(user1.getId());
        assertThrows(NotFoundException.class, () -> userService.getUser(user1.getId()));
    }

    @Test
    void testDeleteUser_NotFound() {
        assertThrows(NotFoundException.class, () -> userService.deleteUser(999L));
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

    @Test
    void testAddUser_DuplicateEmail() {
        userService.addUser(user1);
        User duplicate = new User(null, "Bob", "alice@email.com", "pass2");
        ValidationException ex = assertThrows(ValidationException.class, () -> userService.addUser(duplicate));
        assertTrue(ex.getMessages().contains("Email already exists!"));
    }

    @Test
    void testUpdateUser_DuplicateEmail() {
        userService.addUser(user1);
        userService.addUser(user2);
        User updated = new User(user2.getId(), "Marc", "alice@email.com", "abcd999");
        ValidationException ex = assertThrows(ValidationException.class, () -> userService.updateUser(updated));
        assertTrue(ex.getMessages().contains("Email already exists!"));
    }

    @Test
    void testUpdateUser_SameEmailAllowed() {
        userService.addUser(user1);
        User updated = new User(user1.getId(), "Alice", "alice@email.com", "newpass");
        assertDoesNotThrow(() -> userService.updateUser(updated));
    }
}