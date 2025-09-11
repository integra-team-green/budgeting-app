package cloudflight.integra.backend.repository;


import java.util.Optional;

/**
 * Repository interface for managing User entities.
 * Provides CRUD operations for in memory storage.
 * @param <ID>   the type of the identifier
 * @param <User> the type of the User entity
 */

public interface IUserRepository<ID, User> {
    /**
     * Finds a User by its identifier.
     *
     * @param id the identifier of the User
     * @return the User entity if found, otherwise null
     */
    Optional<User> findOne(ID id);

    /**
     * Retrieves all User entities.
     *
     * @return an Iterable of all User entities
     */
    Iterable<User> findAll();

    /**
     * Saves a User entity.
     *
     * @param entity the User entity to save
     */
    void save(User entity);


    /**
     * Deletes a User by its identifier.
     *
     * @param id the identifier of the User to delete
     */
    void delete(ID id);


    /**
     * Updates an existing User entity.
     *
     * @param entity the User entity to update
     */
    void update(User entity);

    /**
     * Checks if a user with the given email exists.
     *
     * @param email the email to check
     * @return true if a user with the email exists, false otherwise
     */
    Optional<User> findByEmail(String email);
}
