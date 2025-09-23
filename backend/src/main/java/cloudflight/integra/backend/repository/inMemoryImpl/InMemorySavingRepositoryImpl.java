package cloudflight.integra.backend.repository.inMemoryImpl;

import cloudflight.integra.backend.entity.Saving;
import cloudflight.integra.backend.exception.NotFoundException;
import cloudflight.integra.backend.repository.SavingRepository;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public class InMemorySavingRepositoryImpl implements SavingRepository<Long, Saving> {
  private final Map<Long, Saving> savings;

  public InMemorySavingRepositoryImpl() {
    this.savings = new HashMap<>();
  }

  @Override
  public Saving findOne(Long id) {

    if (id == null) {
      throw new IllegalArgumentException("id must not be null");
    }
    if (!savings.containsKey(id)) {
      throw new NotFoundException("entity with id " + id + " does not exist");
    }
    return savings.get(id);
  }

  @Override
  public Iterable<Saving> findAll() {
    return savings.values();
  }

  @Override
  public void save(Saving entity) {
    if (entity == null) {
      throw new IllegalArgumentException("entity must not be null");
    }
    savings.put(entity.getId(), entity);
  }

  @Override
  public void delete(Long id) {
    if (id == null) {
      throw new IllegalArgumentException("id must not be null");
    }
    if (!savings.containsKey(id)) {
      throw new NotFoundException("entity with id " + id + " does not exist");
    }
    savings.remove(id);
  }

  @Override
  public void update(Saving entity) {
    if (entity == null) {
      throw new IllegalArgumentException("entity must not be null");
    }
    if (!savings.containsKey(entity.getId())) {
      throw new NotFoundException("entity with id " + entity.getId() + " does not exist");
    }
    savings.put(entity.getId(), entity);
  }
}
