package cloudflight.integra.backend.service;

import cloudflight.integra.backend.dto.SavingDTO;


public interface SavingService {

  /**
   * First validates the saving and then adds it to the repository
   *
   * @param savingDTO - the saving to add
   */
  SavingDTO addSaving(SavingDTO savingDTO);

  /**
   * Returns all savings from the repository
   *
   * @return Iterable of all savings
   */
  Iterable<SavingDTO> getAllSavings();

  /**
   * Returns a saving by its id from the repository
   *
   * @param id - id of the saving to return
   * @return Saving with the given id
   */
  SavingDTO getSavingById(Long id);

  /**
   * Deletes a saving by its id from the repository
   *
   * @param id - id of the saving to delete
   */
  void deleteSaving(Long id);

  /**
   * First validates the saving and then updates it in the repository
   *
   * @param savingDTO - the saving to update
   */
  SavingDTO updateSaving(SavingDTO savingDTO);
}
