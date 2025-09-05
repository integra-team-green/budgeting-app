package cloudflight.integra.backend.repository;

public interface ISavingRepository<ID, Saving> {

    /**
     * * Finds a saving by its ID.
     *
     * @param id the ID of the saving to find
     * @return the saving with the given ID, or null if not found
     * @throws IllegalArgumentException if id is null or saving with the given id does not exist
     */
    Saving findOne(ID id);

    /**
     * * Finds all savings.
     *
     * @return an iterable of all savings
     */
    Iterable<Saving> findAll();

    /**
     * * Saves a saving.
     *
     * @param entity the saving to save
     * @throws IllegalArgumentException if entity is null
     */
    void save(Saving entity);

    /**
     * * Deletes a saving by its ID.
     *
     * @param id the ID of the saving to delete
     * @throws IllegalArgumentException if id is null or saving with the given id does not exist
     */
    void delete(ID id);

    /**
     * * Updates a saving.
     *
     * @param entity the saving to update
     * @throws IllegalArgumentException if entity is null or does not exist
     */
    void update(Saving entity);

}
