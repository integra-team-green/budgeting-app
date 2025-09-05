package cloudflight.integra.backend.service;

import cloudflight.integra.backend.entity.Saving;

public interface ISavingService {

    /**
     * First validates the saving and then adds it to the repository
     *
     * @param saving -
     */
    void addSaving(Saving saving);

    /**
     * Returns all savings from the repository
     *
     * @return Iterable of all savings
     */
    Iterable<Saving> getAllSavings();

    /**
     * Returns a saving by its id from the repository
     *
     * @param id -
     * @return Saving with the given id
     */
    Saving getSavingById(Long id);

    /**
     * Deletes a saving by its id from the repository
     *
     * @param id -
     */
    void deleteSaving(Long id);

    /**
     * First validates the saving and then updates it in the repository
     *
     * @param saving -
     */
    void updateSaving(Saving saving);
}
