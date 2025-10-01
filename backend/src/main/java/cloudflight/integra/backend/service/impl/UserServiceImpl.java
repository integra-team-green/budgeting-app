package cloudflight.integra.backend.service.impl;

import cloudflight.integra.backend.entity.User;
import cloudflight.integra.backend.entity.validation.UserValidator;
import cloudflight.integra.backend.entity.validation.ValidationException;
import cloudflight.integra.backend.exception.NotFoundException;
import cloudflight.integra.backend.repository.UserRepository;
import cloudflight.integra.backend.service.UserService;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserValidator userValidator;

    public UserServiceImpl(UserRepository userRepository, UserValidator userValidator) {
        this.userRepository = userRepository;
        this.userValidator = userValidator;
    }

    @Override
    @Transactional(readOnly = true)
    public User getUser(Long id) {
        if (id == null) throw new IllegalArgumentException("User ID must not be null.");

        return userRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("User with id " + id + " not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    @Transactional
    public User addUser(User user) {
        userValidator.validate(user);

        Optional<User> existingUser = userRepository.findByEmail(user.getEmail());
        if (existingUser.isPresent()) {
            throw new ValidationException("Email already exists!");
        }

        if (user.getBalance() == null) {
            user.setBalance(BigDecimal.ZERO);
        }
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public User updateUser(User user) {
        if (user == null) throw new IllegalArgumentException("User must not be null.");
        if (user.getId() == null) throw new IllegalArgumentException("Id must not be null.");

        userValidator.validate(user);

        if (userRepository.findById(user.getId()).isEmpty())
            throw new NotFoundException("User with id " + user.getId() + " not found");

        Optional<User> userWithSameEmail = userRepository.findByEmail(user.getEmail());
        if (userWithSameEmail.isPresent() && !userWithSameEmail.get().getId().equals(user.getId())) {
            throw new ValidationException("Email already exists!");
        }

        return userRepository.save(user);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        if (id == null) throw new IllegalArgumentException("Id must not be null");

        if (userRepository.findById(id).isEmpty()) throw new NotFoundException("User with id " + id + " not found");

        userRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public User getUserByEmail(String email) {
        if (email == null || email.isBlank()) throw new IllegalArgumentException("Email must not be null or blank");

        return userRepository
                .findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User with email " + email + " not found"));
    }

    /**
     * @param email
     * @return .
     * @throws UsernameNotFoundException .
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(), user.getPassword(), Collections.singletonList(new SimpleGrantedAuthority("USER")));
    }
}
