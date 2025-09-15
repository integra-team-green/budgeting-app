package cloudflight.integra.backend.service;

import cloudflight.integra.backend.entity.User;
import cloudflight.integra.backend.entity.validator.NotFoundException;
import cloudflight.integra.backend.entity.validator.UserValidator;
import cloudflight.integra.backend.entity.validator.ValidationException;
import cloudflight.integra.backend.repository.IUserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
        if (user == null)
            throw new IllegalArgumentException("User must not be null.");
        userValidator.validate(user);
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new ValidationException(java.util.List.of("Email already exists!"));
        }
        userRepository.save(user);
        return user;
    }

    @Override
    public User getUser(Long id) {
        if (id == null)
            throw new IllegalArgumentException("User ID must not be null.");
        return userRepository.findOne(id)
                .orElseThrow(() -> new NotFoundException("User not found!"));
    }

    @Override
    public User updateUser(User user) {
        if (user == null)
            throw new IllegalArgumentException("User must not be null.");
        if (user.getId() == null)
            throw new IllegalArgumentException("User ID must not be null for update.");
        userValidator.validate(user);

        User existing = userRepository.findOne(user.getId())
                .orElseThrow(() -> new NotFoundException("User not found!"));

        userRepository.findByEmail(user.getEmail())
                .filter(u -> !u.getId().equals(user.getId()))
                .ifPresent(u -> {
                    throw new ValidationException(java.util.List.of("Email already exists!"));
                });

        existing.setName(user.getName());
        existing.setEmail(user.getEmail());
        existing.setPassword(user.getPassword());
        userRepository.update(existing);
        return existing;
    }

    @Override
    public void deleteUser(Long id) {
        if (id == null)
            throw new IllegalArgumentException("User ID must not be null.");
        if (userRepository.findOne(id).isEmpty()) {
            throw new NotFoundException("User not found!");
        }
        userRepository.delete(id);
    }
}