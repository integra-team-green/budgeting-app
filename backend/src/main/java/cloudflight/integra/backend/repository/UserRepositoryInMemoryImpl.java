package cloudflight.integra.backend.repository;

import cloudflight.integra.backend.entity.User;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class UserRepositoryInMemoryImpl implements IUserRepository<Long, User> {
    private final Map<Long, User> users = new HashMap<>();
    private long nextId = 1;

    public void clear() {
        users.clear();
        nextId = 1;
    }

    @Override
    public User findOne(Long id) {
        return users.get(id);
    }

    @Override
    public Iterable<User> findAll() {
        return users.values();
    }

    @Override
    public void save(User user) {
        if(user.getId() == null) {
            user.setId(nextId++);
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
}