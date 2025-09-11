package cloudflight.integra.backend.UserTests;

import cloudflight.integra.backend.entity.User;
import cloudflight.integra.backend.repository.UserRepositoryInMemoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class UserRepositoryInMemoryImplTest {
    private UserRepositoryInMemoryImpl userRepo;
    private User user1, user2;

    @BeforeEach
    void setUp() {
        userRepo = new UserRepositoryInMemoryImpl();
        user1 = new User(null, "Alice", "alice@email.com", "123");
        user2 = new User(null, "Marc", "marc@yahoo.com", "abcd999");
    }

    @Test
    void testSaveAndFindOne() {
        userRepo.save(user1);
        Optional<User> foundOpt = userRepo.findOne(user1.getId());
        assertTrue(foundOpt.isPresent());
        User found = foundOpt.get();
        assertEquals(user1.getId(), found.getId());
        assertEquals("Alice", found.getName());
        assertEquals("alice@email.com", found.getEmail());
        assertEquals("123", found.getPassword());
        assertEquals(user1.getCreatedAt(), found.getCreatedAt());
    }

    @Test
    void testFindAll() {
        Iterable<User> users = userRepo.findAll();
        int count = 0;
        for (User u : users) {
            count++;
        }
        assertEquals(0, count);
        userRepo.save(user1);
        userRepo.save(user2);
        users = userRepo.findAll();
        count = 0;
        for (User u : users) {
            count++;
        }
        assertEquals(2, count);
    }

    @Test
    void testUpdate() {
        userRepo.save(user1);
        user1.setName("Alice Updated");
        userRepo.update(user1);
        Optional<User> updatedOpt = userRepo.findOne(user1.getId());
        assertTrue(updatedOpt.isPresent());
        User updated = updatedOpt.get();
        assertEquals("Alice Updated", updated.getName());
    }

    @Test
    void testDelete() {
        userRepo.save(user1);
        userRepo.save(user2);
        userRepo.delete(user1.getId());
        assertFalse(userRepo.findOne(user1.getId()).isPresent());
        assertTrue(userRepo.findOne(user2.getId()).isPresent());
    }

    @Test
    void testFindByEmail() {
        userRepo.save(user1);
        userRepo.save(user2);
        Optional<User> foundOpt = userRepo.findByEmail("alice@email.com");
        assertTrue(foundOpt.isPresent());
        assertEquals("Alice", foundOpt.get().getName());
        assertFalse(userRepo.findByEmail("notfound@email.com").isPresent());
    }
}