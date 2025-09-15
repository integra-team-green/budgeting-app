package cloudflight.integra.backend.repository;

import cloudflight.integra.backend.entity.User;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class UserRepositoryInMemoryImpl implements IUserRepository<Long, User> {
    private final ConcurrentHashMap<Long, User> users = new ConcurrentHashMap<>();
    private final AtomicLong nextId = new AtomicLong(1);

    public void clear() {
        users.clear();
        nextId.set(1);
    }

    @Override
    public Optional<User> findOne(Long id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public Iterable<User> findAll() {
        return users.values();
    }

    @Override
    public void save(User user) {
        if (user.getId() == null) {
            user.setId(nextId.getAndIncrement());
        }
        users.put(user.getId(), user);
    }

    @Override
    public void delete(Long id) {
        users.remove(id);
    }

    @Override
    public void update(User user) {
        users.put(user.getId(), user);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        Collection<User> allUsers = users.values();
        for (User u : allUsers) {
            if (u.getEmail().equalsIgnoreCase(email)) {
                return Optional.of(u);
            }
        }
        return Optional.empty();
    }
}