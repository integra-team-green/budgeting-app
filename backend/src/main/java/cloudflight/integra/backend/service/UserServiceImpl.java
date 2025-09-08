package cloudflight.integra.backend.service;

import cloudflight.integra.backend.entity.User;
import cloudflight.integra.backend.entity.validator.UserValidator;
import cloudflight.integra.backend.entity.validator.ValidationException;
import cloudflight.integra.backend.repository.IUserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements IUserService {
    private final IUserRepository<Long, User> userRepository;
    private final UserValidator userValidator;

    public UserServiceImpl(IUserRepository<Long, User> userRepository, UserValidator userValidator) {
        this.userRepository = userRepository;
        this.userValidator = userValidator;
    }

    @Override
    public User addUser(User user) {
        if (user == null) throw new IllegalArgumentException("User must not be null.");
        userValidator.validate(user);
        userRepository.save(user);
        return user;
    }

    @Override
    public User getUser(Long id) {
        if (id == null) throw new IllegalArgumentException("ID must not be null.");
        User user = userRepository.findOne(id);
        if (user == null) throw new RuntimeException("User not found!");
        return user;
    }

    @Override
    public User updateUser(User user) {
        if (user == null) throw new IllegalArgumentException("User must not be null.");
        if (user.getId() == null) throw new IllegalArgumentException("User ID must not be null for update.");
        userValidator.validate(user);
        User existing = userRepository.findOne(user.getId());
        if (existing == null) throw new RuntimeException("User not found!");
        existing.setName(user.getName());
        existing.setEmail(user.getEmail());
        existing.setPassword(user.getPassword());
        userRepository.update(existing);
        return existing;
    }

    @Override
    public void deleteUser(Long id) {
        if (id == null) throw new IllegalArgumentException("ID must not be null.");
        User user = userRepository.findOne(id);
        if (user == null) throw new RuntimeException("User not found!");
        userRepository.delete(id);
    }
}